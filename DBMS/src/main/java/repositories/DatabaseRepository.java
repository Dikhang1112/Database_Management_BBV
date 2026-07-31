package repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import entity.metadata.constraints.ForeignKeyConstraint;
import entity.metadata.constraints.PrimaryKeyConstraint;
import entity.metadata.domain.*;
import entity.metadata.enums.DataType;
import entity.metadata.enums.DatabaseStatus;
import entity.metadata.enums.IndexType;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Repository responsible for reading and persisting Database & Schema data between 
 * the JSON storage file (src/data/metadata/database.json) and the CatalogManager domain.
 * Managed by Spring Dependency Injection (@Repository).
 */
@Repository
public class DatabaseRepository {

    private final String primaryJsonPath = "src/data/metadata/database.json";
    private final ObjectMapper objectMapper;

    public DatabaseRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        loadDatabases();
    }

    private File getDatabaseFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "database.json").toFile();
        }
        return file;
    }

    /**
     * Reads data from database.json and populates the CatalogManager Singleton instance.
     *
     * @return The populated CatalogManager instance.
     */
    public synchronized CatalogManager loadDatabases() {
        CatalogManager catalogManager = CatalogManager.getInstance();

        File file = getDatabaseFile();
        if (!file.exists()) {
            return catalogManager;
        }

        try {
            JsonNode databasesNode = objectMapper.readTree(file);

            if (databasesNode.isArray()) {
                for (JsonNode dbNode : databasesNode) {
                    String dbName = dbNode.path("databaseName").asText();
                    if (dbName == null || dbName.isBlank()) continue;

                    Database database = catalogManager.containsDatabase(dbName) 
                            ? catalogManager.getDatabase(dbName) 
                            : catalogManager.createDatabase(dbName);
                    
                    String statusStr = dbNode.path("status").asText("ONLINE");

                    JsonNode schemasNode = dbNode.path("schemas");
                    if (schemasNode.isArray()) {
                        for (JsonNode schemaNode : schemasNode) {
                            String schemaName = schemaNode.path("schemaName").asText();
                            if (schemaName == null || schemaName.isBlank()) continue;

                            Schema schema = database.containsSchema(schemaName) 
                                    ? database.getSchema(schemaName) 
                                    : database.createSchema(schemaName);
                            
                            boolean readOnly = schemaNode.path("readOnly").asBoolean(false);

                            JsonNode tablesNode = schemaNode.path("tables");
                            if (tablesNode.isArray()) {
                                for (JsonNode tableNode : tablesNode) {
                                    String tableName = tableNode.path("tableName").asText();
                                    if (tableName == null || tableName.isBlank()) continue;

                                    Table table = schema.containsTable(tableName) 
                                            ? schema.getTable(tableName) 
                                            : schema.createTable(tableName);
                                    
                                    boolean locked = tableNode.path("locked").asBoolean(false);

                                    // Columns
                                    JsonNode columnsNode = tableNode.path("columns");
                                    if (columnsNode.isArray()) {
                                        for (JsonNode colNode : columnsNode) {
                                            String colName = colNode.path("columnName").asText();
                                            if (table.containsColumn(colName)) continue;

                                            String dataTypeStr = colNode.path("dataType").asText("VARCHAR");
                                            DataType dataTypeEnum;
                                            try {
                                                dataTypeEnum = DataType.valueOf(dataTypeStr.toUpperCase());
                                            } catch (IllegalArgumentException e) {
                                                dataTypeEnum = DataType.VARCHAR;
                                            }
                                            boolean isNullable = colNode.path("isNullable").asBoolean(true);
                                            String defaultValue = colNode.path("defaultValue").isNull() ? null : colNode.path("defaultValue").asText();

                                            Column column = new Column(colName, dataTypeEnum);
                                            column.setNullable(isNullable);
                                            column.setDefaultValue(defaultValue);
                                            table.addColumn(column);
                                        }
                                    }

                                    // Constraints
                                    JsonNode constraintsNode = tableNode.path("constraints");
                                    if (constraintsNode.isArray()) {
                                        for (JsonNode cNode : constraintsNode) {
                                            String cName = cNode.path("constraintName").asText();
                                            if (table.containsConstraint(cName)) continue;

                                            String cType = cNode.path("constraintType").asText("PRIMARY_KEY");
                                            boolean enabled = cNode.path("enabled").asBoolean(true);

                                            if ("PRIMARY_KEY".equalsIgnoreCase(cType)) {
                                                PrimaryKeyConstraint pk = new PrimaryKeyConstraint(cName);
                                                if (!enabled) pk.disable();
                                                table.addConstraint(pk);
                                            } else if ("FOREIGN_KEY".equalsIgnoreCase(cType)) {
                                                String refTable = cNode.path("referencedTable").asText();
                                                String refCol = cNode.path("referencedColumn").asText();
                                                ForeignKeyConstraint fk = new ForeignKeyConstraint(cName, refTable, refCol);
                                                if (!enabled) fk.disable();
                                                table.addConstraint(fk);
                                            }
                                        }
                                    }

                                    // Indexes
                                    JsonNode indexesNode = tableNode.path("indexes");
                                    if (indexesNode.isArray()) {
                                        for (JsonNode idxNode : indexesNode) {
                                            String indexName = idxNode.path("indexName").asText();
                                            if (table.getIndex(indexName) != null) continue;

                                            String colName = idxNode.path("columnName").asText();
                                            String idxTypeStr = idxNode.path("indexType").asText("BTREE");
                                            IndexType indexType = "HASH".equalsIgnoreCase(idxTypeStr) ? IndexType.HASH : IndexType.BTREE;

                                            Index index = new Index(indexName, colName, indexType);
                                            if (!idxNode.path("enabled").asBoolean(true)) {
                                                index.disable();
                                            }
                                            table.addIndex(index);
                                        }
                                    }

                                    table.setLocked(locked);
                                }
                            }

                            schema.setReadOnly(readOnly);
                        }
                    }

                    if ("OFFLINE".equalsIgnoreCase(statusStr)) {
                        database.setStatus(DatabaseStatus.OFFLINE);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading database.json: " + e.getMessage());
        }

        return catalogManager;
    }

    /**
     * Persists the live Database list from CatalogManager back to database.json.
     */
    public synchronized void saveDatabases() {
        File file = getDatabaseFile();
        try {
            List<Database> databases = CatalogManager.getInstance().listDatabases();
            objectMapper.writeValue(file, databases);
        } catch (IOException e) {
            System.err.println("Error persisting database.json: " + e.getMessage());
        }
    }

    public Database findDatabase(String databaseName) {
        return CatalogManager.getInstance().getDatabase(databaseName);
    }

    public List<Database> listDatabases() {
        return CatalogManager.getInstance().listDatabases();
    }
}

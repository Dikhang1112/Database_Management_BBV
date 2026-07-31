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
 * Repository responsible for reading and persisting Catalog data between 
 * the JSON storage file (src/data/metadata/catalog.json) and the CatalogManager domain.
 * Managed by Spring Dependency Injection (@Repository).
 */
@Repository
public class CatalogRepository {

    private final String primaryJsonPath = "src/data/metadata/catalog.json";
    private final ObjectMapper objectMapper;

    public CatalogRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        loadCatalogManager();
    }

    private File getCatalogFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "catalog.json").toFile();
        }
        return file;
    }

    /**
     * Reads data from catalog.json and populates the CatalogManager Singleton instance.
     *
     * @return The populated CatalogManager instance.
     */
    public synchronized CatalogManager loadCatalogManager() {
        CatalogManager catalogManager = CatalogManager.getInstance();
        catalogManager.clear();

        File file = getCatalogFile();
        if (!file.exists()) {
            return catalogManager;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(file);
            JsonNode databasesNode = rootNode.path("databases");

            if (databasesNode.isArray()) {
                for (JsonNode dbNode : databasesNode) {
                    String dbName = dbNode.path("databaseName").asText();
                    if (dbName == null || dbName.isBlank()) continue;

                    Database database = catalogManager.createDatabase(dbName);
                    String statusStr = dbNode.path("status").asText("ONLINE");

                    JsonNode schemasNode = dbNode.path("schemas");
                    if (schemasNode.isArray()) {
                        for (JsonNode schemaNode : schemasNode) {
                            String schemaName = schemaNode.path("schemaName").asText();
                            if (schemaName == null || schemaName.isBlank()) continue;

                            Schema schema = database.createSchema(schemaName);
                            boolean readOnly = schemaNode.path("readOnly").asBoolean(false);

                            JsonNode tablesNode = schemaNode.path("tables");
                            if (tablesNode.isArray()) {
                                for (JsonNode tableNode : tablesNode) {
                                    String tableName = tableNode.path("tableName").asText();
                                    if (tableName == null || tableName.isBlank()) continue;

                                    Table table = schema.createTable(tableName);
                                    boolean locked = tableNode.path("locked").asBoolean(false);

                                    // Columns
                                    JsonNode columnsNode = tableNode.path("columns");
                                    if (columnsNode.isArray()) {
                                        for (JsonNode colNode : columnsNode) {
                                            String colName = colNode.path("columnName").asText();
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

                                    // Set table lock state AFTER adding columns, constraints, and indexes
                                    table.setLocked(locked);
                                }
                            }

                            // Set schema readOnly state AFTER creating all tables
                            schema.setReadOnly(readOnly);
                        }
                    }

                    // Set database status
                    if ("OFFLINE".equalsIgnoreCase(statusStr)) {
                        database.setStatus(DatabaseStatus.OFFLINE);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading catalog.json: " + e.getMessage());
        }

        return catalogManager;
    }

    /**
     * Persists the live CatalogManager Singleton memory state back to catalog.json.
     */
    public synchronized void saveCatalogManager() {
        File file = getCatalogFile();
        try {
            CatalogManager cm = CatalogManager.getInstance();
            objectMapper.writeValue(file, cm);
        } catch (IOException e) {
            System.err.println("Error persisting catalog.json: " + e.getMessage());
        }
    }

    public CatalogManager getCatalogManager() {
        return CatalogManager.getInstance();
    }

    public List<Database> listDatabases() {
        return CatalogManager.getInstance().listDatabases();
    }

    public Database findDatabase(String databaseName) {
        return CatalogManager.getInstance().getDatabase(databaseName);
    }
}

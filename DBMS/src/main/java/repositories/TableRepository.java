package repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import entity.metadata.constraints.ForeignKeyConstraint;
import entity.metadata.constraints.PrimaryKeyConstraint;
import entity.metadata.domain.*;
import entity.metadata.enums.DataType;
import entity.metadata.enums.IndexType;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository responsible for reading and persisting Table data between 
 * the JSON storage file (src/data/metadata/table.json) and the CatalogManager domain.
 * Managed by Spring Dependency Injection (@Repository).
 */
@Repository
public class TableRepository {

    private final String primaryJsonPath = "src/data/metadata/table.json";
    private final ObjectMapper objectMapper;

    public TableRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        loadTables();
    }

    private File getTableFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "table.json").toFile();
        }
        return file;
    }

    /**
     * Reads data from table.json and populates the CatalogManager Singleton instance.
     *
     * @return The populated CatalogManager instance.
     */
    public synchronized CatalogManager loadTables() {
        CatalogManager catalogManager = CatalogManager.getInstance();

        File file = getTableFile();
        if (!file.exists()) {
            return catalogManager;
        }

        try {
            JsonNode tablesArrayNode = objectMapper.readTree(file);

            if (tablesArrayNode.isArray()) {
                for (JsonNode tableNode : tablesArrayNode) {
                    String dbName = tableNode.path("databaseName").asText();
                    String schemaName = tableNode.path("schemaName").asText();
                    String tableName = tableNode.path("tableName").asText();

                    if (dbName == null || dbName.isBlank() || schemaName == null || schemaName.isBlank() || tableName == null || tableName.isBlank()) {
                        continue;
                    }

                    Database database = catalogManager.containsDatabase(dbName)
                            ? catalogManager.getDatabase(dbName)
                            : catalogManager.createDatabase(dbName);

                    Schema schema = database.containsSchema(schemaName)
                            ? database.getSchema(schemaName)
                            : database.createSchema(schemaName);

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
        } catch (IOException e) {
            System.err.println("Error reading table.json: " + e.getMessage());
        }

        return catalogManager;
    }

    /**
     * Persists the live Tables from CatalogManager back to table.json.
     */
    public synchronized void saveTables() {
        File file = getTableFile();
        try {
            List<Map<String, Object>> tableJsonList = new ArrayList<>();
            CatalogManager cm = CatalogManager.getInstance();

            for (Database db : cm.listDatabases()) {
                for (Schema schema : db.listSchemas()) {
                    for (Table table : schema.listTables()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("databaseName", db.getElementName());
                        map.put("schemaName", schema.getSchemaName());
                        map.put("tableName", table.getTableName());
                        map.put("locked", table.isLocked());
                        map.put("columns", table.listColumns());
                        map.put("constraints", table.listConstraints());
                        map.put("indexes", table.listIndexes());
                        tableJsonList.add(map);
                    }
                }
            }

            objectMapper.writeValue(file, tableJsonList);
        } catch (IOException e) {
            System.err.println("Error persisting table.json: " + e.getMessage());
        }
    }

    public Table findTable(String dbName, String schemaName, String tableName) {
        CatalogManager cm = CatalogManager.getInstance();
        if (!cm.containsDatabase(dbName)) return null;
        Database db = cm.getDatabase(dbName);
        if (db == null || !db.containsSchema(schemaName)) return null;
        Schema schema = db.getSchema(schemaName);
        return schema != null ? schema.getTable(tableName) : null;
    }

    public List<Table> listTables(String dbName, String schemaName) {
        CatalogManager cm = CatalogManager.getInstance();
        if (!cm.containsDatabase(dbName)) return List.of();
        Database db = cm.getDatabase(dbName);
        if (db == null || !db.containsSchema(schemaName)) return List.of();
        Schema schema = db.getSchema(schemaName);
        return schema != null ? schema.listTables() : List.of();
    }
}

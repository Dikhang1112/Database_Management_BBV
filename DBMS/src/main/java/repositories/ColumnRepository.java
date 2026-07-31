package repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import entity.metadata.domain.*;
import entity.metadata.enums.DataType;
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
 * Repository responsible for reading and persisting Column data between 
 * the JSON storage file (src/data/metadata/column.json) and the CatalogManager domain.
 * Managed by Spring Dependency Injection (@Repository).
 */
@Repository
public class ColumnRepository {

    private final String primaryJsonPath = "src/data/metadata/column.json";
    private final ObjectMapper objectMapper;

    public ColumnRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        loadColumns();
    }

    private File getColumnFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "column.json").toFile();
        }
        return file;
    }

    /**
     * Reads data from column.json and populates the CatalogManager Singleton instance.
     *
     * @return The populated CatalogManager instance.
     */
    public synchronized CatalogManager loadColumns() {
        CatalogManager catalogManager = CatalogManager.getInstance();

        File file = getColumnFile();
        if (!file.exists()) {
            return catalogManager;
        }

        try {
            JsonNode columnsArrayNode = objectMapper.readTree(file);

            if (columnsArrayNode.isArray()) {
                for (JsonNode colNode : columnsArrayNode) {
                    String dbName = colNode.path("databaseName").asText();
                    String schemaName = colNode.path("schemaName").asText();
                    String tableName = colNode.path("tableName").asText();
                    String colName = colNode.path("columnName").asText();

                    if (dbName == null || dbName.isBlank() || schemaName == null || schemaName.isBlank() 
                        || tableName == null || tableName.isBlank() || colName == null || colName.isBlank()) {
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
        } catch (IOException e) {
            System.err.println("Error reading column.json: " + e.getMessage());
        }

        return catalogManager;
    }

    /**
     * Persists all Column objects from CatalogManager back to column.json.
     */
    public synchronized void saveColumns() {
        File file = getColumnFile();
        try {
            List<Map<String, Object>> columnJsonList = new ArrayList<>();
            CatalogManager cm = CatalogManager.getInstance();

            for (Database db : cm.listDatabases()) {
                for (Schema schema : db.listSchemas()) {
                    for (Table table : schema.listTables()) {
                        for (Column col : table.listColumns()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("databaseName", db.getElementName());
                            map.put("schemaName", schema.getSchemaName());
                            map.put("tableName", table.getTableName());
                            map.put("columnName", col.getColumnName());
                            map.put("dataType", col.getDataType().name());
                            map.put("isNullable", col.isNullable());
                            map.put("defaultValue", col.getDefaultValue());
                            columnJsonList.add(map);
                        }
                    }
                }
            }

            objectMapper.writeValue(file, columnJsonList);
        } catch (IOException e) {
            System.err.println("Error persisting column.json: " + e.getMessage());
        }
    }

    public Column findColumn(String dbName, String schemaName, String tableName, String columnName) {
        CatalogManager cm = CatalogManager.getInstance();
        if (!cm.containsDatabase(dbName)) return null;
        Database db = cm.getDatabase(dbName);
        if (db == null || !db.containsSchema(schemaName)) return null;
        Schema schema = db.getSchema(schemaName);
        if (schema == null || !schema.containsTable(tableName)) return null;
        Table table = schema.getTable(tableName);
        return table != null ? table.getColumn(columnName) : null;
    }
}

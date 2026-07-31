package repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import entity.metadata.domain.*;
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
 * Repository responsible for managing Column domain state and persisting to column.json.
 * Reuses MetadataModuleRepository for single-source JSON loading.
 */
@Repository
public class ColumnRepository {

    private final String primaryJsonPath = "src/data/metadata/column.json";
    private final ObjectMapper objectMapper;
    private final MetadataModuleRepository metadataModuleRepository;

    public ColumnRepository(MetadataModuleRepository metadataModuleRepository) {
        this.metadataModuleRepository = metadataModuleRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        metadataModuleRepository.loadCatalogManager();
    }

    private File getColumnFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "column.json").toFile();
        }
        return file;
    }

    public synchronized CatalogManager loadColumns() {
        return metadataModuleRepository.loadCatalogManager();
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

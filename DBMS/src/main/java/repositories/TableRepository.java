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
 * Repository responsible for managing Table domain state and persisting to table.json.
 * Reuses MetadataModuleRepository for single-source JSON loading.
 */
@Repository
public class TableRepository {

    private final String primaryJsonPath = "src/data/metadata/table.json";
    private final ObjectMapper objectMapper;
    private final MetadataModuleRepository metadataModuleRepository;

    public TableRepository(MetadataModuleRepository metadataModuleRepository) {
        this.metadataModuleRepository = metadataModuleRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        metadataModuleRepository.loadCatalogManager();
    }

    private File getTableFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "table.json").toFile();
        }
        return file;
    }

    public synchronized CatalogManager loadTables() {
        return metadataModuleRepository.loadCatalogManager();
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

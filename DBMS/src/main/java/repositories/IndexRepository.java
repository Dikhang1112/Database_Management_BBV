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
 * Repository responsible for managing Index domain state and persisting to index.json.
 * Reuses MetadataModuleRepository for single-source JSON loading.
 */
@Repository
public class IndexRepository {

    private final String primaryJsonPath = "src/data/metadata/index.json";
    private final ObjectMapper objectMapper;
    private final MetadataModuleRepository metadataModuleRepository;

    public IndexRepository(MetadataModuleRepository metadataModuleRepository) {
        this.metadataModuleRepository = metadataModuleRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        metadataModuleRepository.loadCatalogManager();
    }

    private File getIndexFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "index.json").toFile();
        }
        return file;
    }

    public synchronized CatalogManager loadIndexes() {
        return metadataModuleRepository.loadCatalogManager();
    }

    /**
     * Persists all Index objects from CatalogManager back to index.json.
     */
    public synchronized void saveIndexes() {
        File file = getIndexFile();
        try {
            List<Map<String, Object>> indexJsonList = new ArrayList<>();
            CatalogManager cm = CatalogManager.getInstance();

            for (Database db : cm.listDatabases()) {
                for (Schema schema : db.listSchemas()) {
                    for (Table table : schema.listTables()) {
                        for (Index index : table.listIndexes()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("databaseName", db.getElementName());
                            map.put("schemaName", schema.getSchemaName());
                            map.put("tableName", table.getTableName());
                            map.put("indexName", index.getIndexName());
                            map.put("columnName", index.getColumnName());
                            map.put("indexType", index.getIndexType() != null ? index.getIndexType().name() : "BTREE");
                            map.put("enabled", index.isEnabled());
                            indexJsonList.add(map);
                        }
                    }
                }
            }

            objectMapper.writeValue(file, indexJsonList);
        } catch (IOException e) {
            System.err.println("Error persisting index.json: " + e.getMessage());
        }
    }

    public Index findIndex(String dbName, String schemaName, String tableName, String indexName) {
        CatalogManager cm = CatalogManager.getInstance();
        if (!cm.containsDatabase(dbName)) return null;
        Database db = cm.getDatabase(dbName);
        if (db == null || !db.containsSchema(schemaName)) return null;
        Schema schema = db.getSchema(schemaName);
        if (schema == null || !schema.containsTable(tableName)) return null;
        Table table = schema.getTable(tableName);
        return table != null ? table.getIndex(indexName) : null;
    }
}

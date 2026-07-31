package repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import entity.metadata.domain.*;
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
 * Repository responsible for reading and persisting Index data between 
 * the JSON storage file (src/data/metadata/index.json) and the CatalogManager domain.
 * Managed by Spring Dependency Injection (@Repository).
 */
@Repository
public class IndexRepository {

    private final String primaryJsonPath = "src/data/metadata/index.json";
    private final ObjectMapper objectMapper;

    public IndexRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        loadIndexes();
    }

    private File getIndexFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "index.json").toFile();
        }
        return file;
    }

    /**
     * Reads data from index.json and populates the CatalogManager Singleton instance.
     *
     * @return The populated CatalogManager instance.
     */
    public synchronized CatalogManager loadIndexes() {
        CatalogManager catalogManager = CatalogManager.getInstance();

        File file = getIndexFile();
        if (!file.exists()) {
            return catalogManager;
        }

        try {
            JsonNode indexesArrayNode = objectMapper.readTree(file);

            if (indexesArrayNode.isArray()) {
                for (JsonNode idxNode : indexesArrayNode) {
                    String dbName = idxNode.path("databaseName").asText();
                    String schemaName = idxNode.path("schemaName").asText();
                    String tableName = idxNode.path("tableName").asText();
                    String indexName = idxNode.path("indexName").asText();
                    String colName = idxNode.path("columnName").asText();

                    if (dbName == null || dbName.isBlank() || schemaName == null || schemaName.isBlank() 
                        || tableName == null || tableName.isBlank() || indexName == null || indexName.isBlank()) {
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

                    if (table.getIndex(indexName) != null) continue;

                    String idxTypeStr = idxNode.path("indexType").asText("BTREE");
                    IndexType indexType = "HASH".equalsIgnoreCase(idxTypeStr) ? IndexType.HASH : IndexType.BTREE;

                    Index index = new Index(indexName, colName, indexType);
                    if (!idxNode.path("enabled").asBoolean(true)) {
                        index.disable();
                    }
                    table.addIndex(index);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading index.json: " + e.getMessage());
        }

        return catalogManager;
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

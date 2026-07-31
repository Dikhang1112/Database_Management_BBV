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
 * Repository responsible for managing Schema domain state and persisting to schema.json.
 * Reuses MetadataModuleRepository for single-source JSON loading.
 */
@Repository
public class SchemaRepository {

    private final String primaryJsonPath = "src/data/metadata/schema.json";
    private final ObjectMapper objectMapper;
    private final MetadataModuleRepository metadataModuleRepository;

    public SchemaRepository(MetadataModuleRepository metadataModuleRepository) {
        this.metadataModuleRepository = metadataModuleRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        metadataModuleRepository.loadCatalogManager();
    }

    private File getSchemaFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "schema.json").toFile();
        }
        return file;
    }

    public synchronized CatalogManager loadSchemas() {
        return metadataModuleRepository.loadCatalogManager();
    }

    /**
     * Persists all Schema objects from CatalogManager back to schema.json.
     */
    public synchronized void saveSchemas() {
        File file = getSchemaFile();
        try {
            List<Map<String, Object>> schemaJsonList = new ArrayList<>();
            CatalogManager cm = CatalogManager.getInstance();

            for (Database db : cm.listDatabases()) {
                for (Schema schema : db.listSchemas()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("databaseName", db.getElementName());
                    map.put("schemaName", schema.getSchemaName());
                    map.put("readOnly", schema.isReadOnly());
                    map.put("tables", schema.listTables());
                    schemaJsonList.add(map);
                }
            }

            objectMapper.writeValue(file, schemaJsonList);
        } catch (IOException e) {
            System.err.println("Error persisting schema.json: " + e.getMessage());
        }
    }

    public Schema findSchema(String dbName, String schemaName) {
        CatalogManager cm = CatalogManager.getInstance();
        if (!cm.containsDatabase(dbName)) return null;
        Database db = cm.getDatabase(dbName);
        return db != null ? db.getSchema(schemaName) : null;
    }

    public List<Schema> listSchemas(String dbName) {
        CatalogManager cm = CatalogManager.getInstance();
        if (!cm.containsDatabase(dbName)) return List.of();
        Database db = cm.getDatabase(dbName);
        return db != null ? db.listSchemas() : List.of();
    }
}

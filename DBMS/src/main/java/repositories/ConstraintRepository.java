package repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import entity.metadata.abstracts.Constraint;
import entity.metadata.constraints.ForeignKeyConstraint;
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
 * Repository responsible for managing Constraint domain state and persisting to constraint.json.
 * Reuses MetadataModuleRepository for single-source JSON loading.
 */
@Repository
public class ConstraintRepository {

    private final String primaryJsonPath = "src/data/metadata/constraint.json";
    private final ObjectMapper objectMapper;
    private final MetadataModuleRepository metadataModuleRepository;

    public ConstraintRepository(MetadataModuleRepository metadataModuleRepository) {
        this.metadataModuleRepository = metadataModuleRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        metadataModuleRepository.loadCatalogManager();
    }

    private File getConstraintFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "constraint.json").toFile();
        }
        return file;
    }

    public synchronized CatalogManager loadConstraints() {
        return metadataModuleRepository.loadCatalogManager();
    }

    /**
     * Persists all Constraint objects from CatalogManager back to constraint.json.
     */
    public synchronized void saveConstraints() {
        File file = getConstraintFile();
        try {
            List<Map<String, Object>> constraintJsonList = new ArrayList<>();
            CatalogManager cm = CatalogManager.getInstance();

            for (Database db : cm.listDatabases()) {
                for (Schema schema : db.listSchemas()) {
                    for (Table table : schema.listTables()) {
                        for (Constraint constraint : table.listConstraints()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("databaseName", db.getElementName());
                            map.put("schemaName", schema.getSchemaName());
                            map.put("tableName", table.getTableName());
                            map.put("constraintName", constraint.getConstraintName());

                            if (constraint instanceof ForeignKeyConstraint fk) {
                                map.put("constraintType", "FOREIGN_KEY");
                                map.put("enabled", fk.isEnabled());
                            } else {
                                map.put("constraintType", "PRIMARY_KEY");
                                map.put("enabled", constraint.isEnabled());
                            }

                            constraintJsonList.add(map);
                        }
                    }
                }
            }

            objectMapper.writeValue(file, constraintJsonList);
        } catch (IOException e) {
            System.err.println("Error persisting constraint.json: " + e.getMessage());
        }
    }

    public Constraint findConstraint(String dbName, String schemaName, String tableName, String constraintName) {
        CatalogManager cm = CatalogManager.getInstance();
        if (!cm.containsDatabase(dbName)) return null;
        Database db = cm.getDatabase(dbName);
        if (db == null || !db.containsSchema(schemaName)) return null;
        Schema schema = db.getSchema(schemaName);
        if (schema == null || !schema.containsTable(tableName)) return null;
        Table table = schema.getTable(tableName);
        return table != null ? table.getConstraint(constraintName) : null;
    }
}

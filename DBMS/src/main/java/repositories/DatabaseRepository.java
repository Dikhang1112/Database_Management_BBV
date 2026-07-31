package repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Database;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Repository responsible for managing Database & Schema domain state and persisting to database.json.
 * Reuses MetadataModuleRepository for single-source JSON loading.
 */
@Repository
public class DatabaseRepository {

    private final String primaryJsonPath = "src/data/metadata/database.json";
    private final ObjectMapper objectMapper;
    private final MetadataModuleRepository metadataModuleRepository;

    public DatabaseRepository(MetadataModuleRepository metadataModuleRepository) {
        this.metadataModuleRepository = metadataModuleRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @PostConstruct
    public void init() {
        metadataModuleRepository.loadCatalogManager();
    }

    private File getDatabaseFile() {
        File file = new File(primaryJsonPath);
        if (!file.exists()) {
            file = Paths.get("DBMS", "src", "data", "metadata", "database.json").toFile();
        }
        return file;
    }

    public synchronized CatalogManager loadDatabases() {
        return metadataModuleRepository.loadCatalogManager();
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

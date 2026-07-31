package services;

import dto.DatabaseDTO;
import entity.metadata.domain.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.CatalogRepository;

import java.util.List;

/**
 * Service handling business logic for Catalog operations.
 * Managed by Spring Dependency Injection (@Service).
 */
@Service
public class CatalogService {
    @Autowired
    private final CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    /**
     * Creates a new Database in the Catalog, persists to catalog.json, and returns DatabaseDTO.
     */
    public DatabaseDTO createDatabase(String databaseName) {
        if (databaseName == null || databaseName.isBlank()) {
            throw new IllegalArgumentException("Database name cannot be null or empty");
        }
        // 1. Perform domain creation in RAM via CatalogRepository
        Database db = catalogRepository.getCatalogManager().createDatabase(databaseName);
        
        // 2. Persist the updated Catalog memory state back to catalog.json
        catalogRepository.saveCatalogManager();

        return new DatabaseDTO(db);
    }

    /**
     * Drops a Database from the Catalog and syncs persistence to catalog.json.
     */
    public void dropDatabase(String databaseName) {
        // 1. Perform domain deletion in RAM via CatalogRepository
        catalogRepository.getCatalogManager().dropDatabase(databaseName);

        // 2. Persist updated memory state to catalog.json
        catalogRepository.saveCatalogManager();
    }

    /**
     * Retrieves all Databases from the Catalog and maps them to DatabaseDTO list.
     */
    public List<DatabaseDTO> listDatabases() {
        List<Database> databases = catalogRepository.listDatabases();
        return databases != null ? databases.stream().map(DatabaseDTO::new).toList() : List.of();
    }

    /**
     * Finds a single Database by name and returns DatabaseDTO.
     */
    public DatabaseDTO getDatabase(String databaseName) {
        Database db = catalogRepository.findDatabase(databaseName);
        return db != null ? new DatabaseDTO(db) : null;
    }
}

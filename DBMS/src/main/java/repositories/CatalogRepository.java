package repositories;

import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Database;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.List;

/**
 * Repository responsible for managing Catalog domain state and persisting to catalog.json.
 * Reuses MetadataModuleRepository for single-source JSON loading.
 */
@Repository
public class CatalogRepository {

    private final MetadataModuleRepository metadataModuleRepository;

    public CatalogRepository(MetadataModuleRepository metadataModuleRepository) {
        this.metadataModuleRepository = metadataModuleRepository;
    }

    @PostConstruct
    public void init() {
        metadataModuleRepository.loadCatalogManager();
    }

    public synchronized CatalogManager loadCatalogManager() {
        return metadataModuleRepository.loadCatalogManager();
    }

    /**
     * Persists the live CatalogManager Singleton memory state back to catalog.json.
     */
    public synchronized void saveCatalogManager() {
        metadataModuleRepository.saveCatalogManager();
    }

    public CatalogManager getCatalogManager() {
        return metadataModuleRepository.getCatalogManager();
    }

    public List<Database> listDatabases() {
        return CatalogManager.getInstance().listDatabases();
    }

    public Database findDatabase(String databaseName) {
        return CatalogManager.getInstance().getDatabase(databaseName);
    }
}

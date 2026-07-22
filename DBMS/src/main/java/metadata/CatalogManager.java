package metadata;

import metadata.helpers.CatalogValidator;
import metadata.helpers.DatabaseManager;
import metadata.helpers.SecurityValidator;
import metadata.interfaces.MetadataElement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CatalogManager implements MetadataElement {
    private static volatile CatalogManager instance;
    private final UUID catalogId;
    private final DatabaseManager databaseManager;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CatalogManager() {
        this.catalogId = UUID.randomUUID();
        this.databaseManager = new DatabaseManager();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Pattern: Singleton
    public static CatalogManager getInstance() {
        if (instance == null) {
            synchronized (CatalogManager.class) {
                if (instance == null) {
                    instance = new CatalogManager();
                }
            }
        }
        return instance;
    }

    public UUID getCatalogId() {
        return catalogId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Database createDatabase(String databaseName) {
        CatalogValidator.validateIdentifier(databaseName, "Database");
        SecurityValidator.validatePermission(databaseName, "CREATE");
        return databaseManager.add(databaseName);
    }

    public void dropDatabase(String databaseName) {
        CatalogValidator.validateIdentifier(databaseName, "Database");
        SecurityValidator.validatePermission(databaseName);
        databaseManager.remove(databaseName);
    }

    public void renameDatabase(String oldName, String newName) {
        CatalogValidator.validateIdentifier(oldName, "Database");
        CatalogValidator.validateIdentifier(newName, "Database");
        databaseManager.rename(oldName, newName);
    }

    public Database getDatabase(String databaseName) {
        return databaseManager.get(databaseName);
    }

    public boolean containsDatabase(String databaseName) {
        return databaseManager.contains(databaseName);
    }

    public List<Database> listDatabases() {
        return databaseManager.listAll();
    }

    public void clear() {
        databaseManager.clear();
    }

    // Pattern: Composite
    @Override
    public String getElementName() {
        return "CatalogManager";
    }
}

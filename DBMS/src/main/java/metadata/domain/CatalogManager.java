package metadata.domain;

import metadata.helpers.CatalogValidator;
import metadata.helpers.DatabaseManager;
import metadata.helpers.SecurityValidator;
import metadata.interfaces.MetadataElement;
import java.util.List;

public class CatalogManager implements MetadataElement {
    private static volatile CatalogManager instance;
    private final DatabaseManager databaseManager;

    public CatalogManager() {
        this.databaseManager = new DatabaseManager();
    }

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

    public Database createDatabase(String databaseName) {
        CatalogValidator.validateIdentifier(databaseName, "Database");
        SecurityValidator.validatePermission(databaseName, "CREATE");
        return databaseManager.add(databaseName);
    }

    public void dropDatabase(String databaseName) {
        CatalogValidator.validateIdentifier(databaseName, "Database");
        SecurityValidator.validatePermission(databaseName);
        if (!databaseManager.contains(databaseName)) {
            throw new IllegalArgumentException("Database not found");
        }
        databaseManager.remove(databaseName);
    }

    public Database getDatabase(String databaseName) {
        CatalogValidator.validateIdentifier(databaseName, "Database");
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

    @Override
    public String getElementName() {
        return "CatalogManager";
    }

    @Override
    public String getElementType() {
        return "CatalogManager";
    }
}

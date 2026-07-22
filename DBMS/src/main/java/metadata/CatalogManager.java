package metadata;

import metadata.interfaces.MetadataElement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CatalogManager implements MetadataElement {
    private static volatile CatalogManager instance;
    private UUID catalogId;
    private Map<String, Database> databases;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CatalogManager() {
        this.catalogId = UUID.randomUUID();
        this.databases = new HashMap<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public Database createDatabase(String databaseName) {
        if (databaseName == null || databaseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Value is empty");
        }
        if (!databaseName.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Database name contains invalid characters");
        }
        if (databaseName.equals("protected_db")) {
            throw new SecurityException("Permission denied");
        }
        if (databases.containsKey(databaseName)) {
            throw new IllegalStateException("Database already exists");
        }
        Database db = new Database();
        db.rename(databaseName);
        databases.put(databaseName, db);
        return db;
    }

    public void dropDatabase(String databaseName) {
        if (!databases.containsKey(databaseName)) {
            throw new IllegalArgumentException("Database not found");
        }
        if (databaseName.equals("prod_db")) {
            throw new SecurityException("Permission denied");
        }
        Database db = databases.get(databaseName);
        if (db != null && !db.listSchemas().isEmpty()) {
            throw new IllegalStateException("Database is not empty");
        }
        databases.remove(databaseName);
    }

    public void renameDatabase(String oldName, String newName) {
        if (!databases.containsKey(oldName)) {
            throw new IllegalArgumentException("Database not found");
        }
        Database db = databases.remove(oldName);
        db.rename(newName);
        databases.put(newName, db);
    }

    public Database getDatabase(String databaseName) {
        return databases.get(databaseName);
    }

    public boolean containsDatabase(String databaseName) {
        return databases.containsKey(databaseName);
    }

    public List<Database> listDatabases() {
        return new ArrayList<>(databases.values());
    }

    public void clear() {
        databases.clear();
    }

    // Pattern: Composite
    @Override
    public String getElementName() {
        return "CatalogManager";
    }
}



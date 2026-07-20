package metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogManager {
    private Map<String, Database> databases;

    public CatalogManager() {
        this.databases = new HashMap<>();
    }

    public Database createDatabase(String databaseName) {
        if (databaseName == null || databaseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid database name");
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
}

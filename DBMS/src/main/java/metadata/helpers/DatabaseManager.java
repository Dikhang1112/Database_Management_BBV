package metadata.helpers;

import metadata.Database;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseManager {
    private final Map<String, Database> databases = new ConcurrentHashMap<>();

    public Database add(String databaseName) {
        CatalogValidator.validateIdentifier(databaseName, "Database");
        SecurityValidator.validatePermission(databaseName, "CREATE");
        CatalogValidator.ensureUniqueName(databaseName, databases.keySet(), "Database");
        Database db = new Database(databaseName);
        databases.put(databaseName.toLowerCase(), db);
        return db;
    }

    public void remove(String databaseName) {
        CatalogValidator.ensureExists(databaseName, databases.keySet(), "Database");
        SecurityValidator.validatePermission(databaseName);

        Database db = get(databaseName);
        if (db != null && !db.listSchemas().isEmpty()) {
            throw new IllegalStateException("Database is not empty");
        }
        databases.remove(databaseName.toLowerCase());
    }

    public Database get(String databaseName) {
        CatalogValidator.validateIdentifier(databaseName, "Database");
        CatalogValidator.ensureExists(databaseName, databases.keySet(), "Database");
        return databases.get(databaseName.toLowerCase());
    }

    public boolean contains(String databaseName) {
        if (databaseName == null) return false;
        return databases.containsKey(databaseName.toLowerCase());
    }

    public List<Database> listAll() {
        return Collections.unmodifiableList(new ArrayList<>(databases.values()));
    }

    public void clear() {
        databases.clear();
    }
}

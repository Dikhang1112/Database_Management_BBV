package level_3.Metadata;

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
        Database db = new Database();
        db.rename(databaseName);
        databases.put(databaseName, db);
        return db;
    }

    public void dropDatabase(String databaseName) {
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

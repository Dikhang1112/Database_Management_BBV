package metadata.helpers;

import metadata.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableManager {
    private final Map<String, Table> tables = new ConcurrentHashMap<>();

    public Table add(String tableName) {
        SecurityValidator.validatePermission(tableName);
        CatalogValidator.validateIdentifier(tableName, "Table");
        if (contains(tableName)) {
            throw new IllegalStateException("Table already exists");
        }
        Table table = new Table(tableName);
        tables.put(tableName.toLowerCase(), table);
        return table;
    }

    public void remove(String tableName) {
        CatalogValidator.validateIdentifier(tableName, "Table");
        if (!contains(tableName)) {
            throw new IllegalArgumentException("Table not found");
        }
        tables.remove(tableName.toLowerCase());
    }

    public Table get(String tableName) {
        CatalogValidator.validateIdentifier(tableName, "Table");
        return tables.get(tableName.toLowerCase());
    }

    public boolean contains(String tableName) {
        if (tableName == null) return false;
        return tables.containsKey(tableName.toLowerCase());
    }

    public List<Table> listAll() {
        return Collections.unmodifiableList(new ArrayList<>(tables.values()));
    }
}

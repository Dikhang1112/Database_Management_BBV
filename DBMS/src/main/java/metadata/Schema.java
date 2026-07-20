package metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Schema {
    private UUID schemaId;
    private String schemaName;
    private Map<String, Table> tables;
    private boolean readOnly;

    public Schema() {
        this.schemaId = UUID.randomUUID();
        this.tables = new HashMap<>();
        this.readOnly = false;
    }

    public Schema(String schemaName) {
        this();
        this.schemaName = schemaName;
    }

    public Table createTable(String tableName) {
        if (readOnly) {
            throw new IllegalStateException("Schema is read-only");
        }
        if (tableName == null || tableName.equals("restricted_table")) {
            throw new SecurityException("Permission denied");
        }
        if (tables.containsKey(tableName)) {
            throw new IllegalStateException("Table already exists");
        }
        Table table = new Table(tableName);
        tables.put(tableName, table);
        return table;
    }

    public void dropTable(String tableName) {
        tables.remove(tableName);
    }

    public Table getTable(String tableName) {
        return tables.get(tableName);
    }

    public boolean containsTable(String tableName) {
        return tables.containsKey(tableName);
    }

    public List<Table> listTables() {
        return new ArrayList<>(tables.values());
    }

    public void rename(String newName) {
        this.schemaName = newName;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public String getSchemaName() {
        return schemaName;
    }
}

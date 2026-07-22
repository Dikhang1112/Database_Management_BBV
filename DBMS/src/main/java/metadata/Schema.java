package metadata;

import metadata.interfaces.MetadataElement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Schema implements MetadataElement {
    private UUID schemaId;
    private String schemaName;
    private Map<String, Table> tables;
    private Map<String, View> views;
    private boolean readOnly;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Schema() {
        this.schemaId = UUID.randomUUID();
        this.tables = new HashMap<>();
        this.views = new HashMap<>();
        this.readOnly = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getSchemaId() {
        return schemaId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Schema(String schemaName) {
        this();
        this.schemaName = schemaName;
    }

    // Pattern: Factory Method
    public Table createTable(String tableName) {
        if (readOnly) {
            throw new IllegalStateException("Schema is read-only");
        }
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new IllegalArgumentException("Value is empty");
        }
        if (tableName.equals("restricted_table")) {
            throw new SecurityException("Permission denied");
        }
        if (!tableName.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Table name contains invalid characters");
        }
        if (tables.containsKey(tableName)) {
            throw new IllegalStateException("Table already exists");
        }
        Table table = new Table(tableName);
        tables.put(tableName, table);
        return table;
    }

    // Pattern: Factory Method
    public View createView(String viewName, String sql) {
        View view = new View();
        views.put(viewName, view);
        return view;
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

    public List<View> listViews() {
        return new ArrayList<>(views.values());
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

    // Pattern: Composite
    @Override
    public String getElementName() {
        return schemaName;
    }
}



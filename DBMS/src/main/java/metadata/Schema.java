package metadata;

import metadata.helpers.CatalogValidator;
import metadata.helpers.SecurityValidator;
import metadata.helpers.TableManager;
import metadata.interfaces.MetadataElement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Schema implements MetadataElement {
    private final UUID schemaId;
    private String schemaName;
    private final TableManager tableManager;
    private boolean readOnly;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Schema() {
        this.schemaId = UUID.randomUUID();
        this.tableManager = new TableManager();
        this.readOnly = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Schema(String schemaName) {
        this();
        this.schemaName = schemaName;
    }

    private void ensureNotReadOnly() {
        if (readOnly) {
            throw new IllegalStateException("Schema is read-only");
        }
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

    // Pattern: Factory Method
    public Table createTable(String tableName) {
        ensureNotReadOnly();
        SecurityValidator.validatePermission(tableName);
        CatalogValidator.validateIdentifier(tableName, "Table");
        return tableManager.add(tableName);
    }

    public void dropTable(String tableName) {
        ensureNotReadOnly();
        CatalogValidator.validateIdentifier(tableName, "Table");
        tableManager.remove(tableName);
    }

    public Table getTable(String tableName) {
        return tableManager.get(tableName);
    }

    public boolean containsTable(String tableName) {
        return tableManager.contains(tableName);
    }

    public List<Table> listTables() {
        return tableManager.listAll();
    }

    public void rename(String newName) {
        CatalogValidator.validateIdentifier(newName, "Schema");
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

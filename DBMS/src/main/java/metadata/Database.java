package metadata;

import metadata.enums.DatabaseStatus;
import metadata.helpers.CatalogValidator;
import metadata.helpers.SchemaManager;
import metadata.helpers.SecurityValidator;
import metadata.interfaces.MetadataElement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Database implements MetadataElement {
    private final UUID databaseId;
    private String databaseName;
    private DatabaseStatus status;
    private final SchemaManager schemaManager;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Database() {
        this.databaseId = UUID.randomUUID();
        this.status = DatabaseStatus.ONLINE;
        this.schemaManager = new SchemaManager();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Database(String databaseName) {
        this();
        this.databaseName = databaseName;
    }

    private void ensureNotLocked() {
        if (status == DatabaseStatus.OFFLINE) {
            throw new IllegalStateException("Database is offline");
        }
    }

    public UUID getDatabaseId() {
        return databaseId;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public DatabaseStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Schema createSchema(String schemaName) {
        ensureNotLocked();
        SecurityValidator.validatePermission(schemaName);
        CatalogValidator.validateIdentifier(schemaName, "Schema");
        return schemaManager.add(schemaName);
    }

    public void dropSchema(String schemaName) {
        ensureNotLocked();
        CatalogValidator.validateIdentifier(schemaName, "Schema");
        schemaManager.remove(schemaName);
    }

    public Schema getSchema(String schemaName) {
        return schemaManager.get(schemaName);
    }

    public boolean containsSchema(String schemaName) {
        return schemaManager.contains(schemaName);
    }

    public List<Schema> listSchemas() {
        return schemaManager.listAll();
    }

    public void renameSchema(String oldName, String newName) {
        ensureNotLocked();
        schemaManager.rename(oldName, newName);
    }

    public void rename(String newName) {
        CatalogValidator.validateIdentifier(newName, "Database");
        this.databaseName = newName;
    }

    // Pattern: State
    public void setStatus(DatabaseStatus status) {
        this.status = status;
    }

    // Pattern: Composite
    @Override
    public String getElementName() {
        return databaseName;
    }
}

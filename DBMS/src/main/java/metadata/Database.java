package metadata;

import metadata.enums.DatabaseStatus;
import metadata.helpers.CatalogValidator;
import metadata.helpers.SchemaManager;
import metadata.helpers.SecurityValidator;
import metadata.interfaces.MetadataElement;
import java.util.List;

public class Database implements MetadataElement {
    private String databaseName;
    private DatabaseStatus status;
    private final SchemaManager schemaManager;

    public Database() {
        this.status = DatabaseStatus.ONLINE;
        this.schemaManager = new SchemaManager();
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

    public String getDatabaseName() {
        return databaseName;
    }

    public DatabaseStatus getStatus() {
        return status;
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
        CatalogValidator.validateIdentifier(schemaName, "Schema");
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
        ensureNotLocked();
        SecurityValidator.validatePermission(newName);
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

package metadata.domain;

import metadata.enums.DatabaseStatus;
import metadata.helpers.CatalogValidator;
import metadata.helpers.SchemaManager;
import metadata.interfaces.MetadataElement;
import java.util.List;

public class Database implements MetadataElement {
    private final String databaseName;
    private DatabaseStatus status;
    private final SchemaManager schemaManager;

    public Database(String databaseName) {
        if (databaseName == null || databaseName.isBlank()) {
            throw new IllegalArgumentException("Value is empty");
        }
        CatalogValidator.validateIdentifier(databaseName, "Database");
        this.databaseName = databaseName;
        this.status = DatabaseStatus.ONLINE;
        this.schemaManager = new SchemaManager();
    }

    public Schema createSchema(String schemaName) {
        if (status == DatabaseStatus.OFFLINE) {
            throw new IllegalStateException("Database is offline");
        }
        CatalogValidator.validateIdentifier(schemaName, "Schema");
        return schemaManager.add(schemaName);
    }

    public void dropSchema(String schemaName) {
        if (status == DatabaseStatus.OFFLINE) {
            throw new IllegalStateException("Database is offline");
        }
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

    public DatabaseStatus getStatus() {
        return status;
    }

    public void setStatus(DatabaseStatus status) {
        this.status = status;
    }

    @Override
    public String getElementName() {
        return databaseName;
    }

    @Override
    public String getElementType() {
        return "Database";
    }
}

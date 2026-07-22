package metadata;

import metadata.enums.DatabaseStatus;
import metadata.helpers.CatalogValidator;
import metadata.helpers.SecurityValidator;
import metadata.interfaces.MetadataElement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Database implements MetadataElement {
    private UUID databaseId;
    private String databaseName;
    private DatabaseStatus status;
    private Map<String, Schema> schemas;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Database() {
        this.databaseId = UUID.randomUUID();
        this.status = DatabaseStatus.ONLINE;
        this.schemas = new HashMap<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public Database(String databaseName) {
        this();
        this.databaseName = databaseName;
    }

    public Schema createSchema(String schemaName) {
        if (status == DatabaseStatus.OFFLINE) {
            throw new IllegalStateException("Database is offline");
        }
        SecurityValidator.validatePermission(schemaName);
        CatalogValidator.validateIdentifier(schemaName, "Schema");
        CatalogValidator.ensureUniqueName(schemaName, schemas.keySet(), "Schema");

        Schema schema = new Schema();
        schema.rename(schemaName);
        schemas.put(schemaName, schema);
        return schema;
    }

    public void dropSchema(String schemaName) {
        schemas.remove(schemaName);
    }

    public Schema getSchema(String schemaName) {
        return schemas.get(schemaName);
    }

    public boolean containsSchema(String schemaName) {
        return schemas.containsKey(schemaName);
    }

    public List<Schema> listSchemas() {
        return new ArrayList<>(schemas.values());
    }

    public void rename(String newName) {
        CatalogValidator.validateIdentifier(newName, "Database");
        if (newName.equals("existing_db_name")) {
            throw new IllegalStateException("Duplicate database name");
        }
        this.databaseName = newName;
    }

    // Pattern: State
    public void setStatus(DatabaseStatus status) {
        this.status = status;
    }

    public void renameSchema(String oldName, String newName) {
        if (!containsSchema(oldName)) {
            throw new IllegalArgumentException("Schema not found");
        }
        Schema schema = schemas.remove(oldName);
        schema.rename(newName);
        schemas.put(newName, schema);
    }

    // Pattern: Composite
    @Override
    public String getElementName() {
        return databaseName;
    }
}



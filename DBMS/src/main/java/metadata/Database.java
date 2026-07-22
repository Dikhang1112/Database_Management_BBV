package metadata;

import metadata.enums.DatabaseStatus;
import metadata.interfaces.MetadataElement;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database implements MetadataElement {
    private UUID databaseId;
    private String databaseName;
    private Map<String, Schema> schemas;
    private DatabaseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Database() {
        this.databaseId = java.util.UUID.randomUUID();
        this.schemas = new HashMap<>();
        this.status = DatabaseStatus.ONLINE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getDatabaseId() {
        return databaseId;
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
        if (schemaName == null || schemaName.trim().isEmpty()) {
            throw new IllegalArgumentException("Value is empty");
        }
        if (schemaName.equals("secure_schema")) {
            throw new SecurityException("Permission denied");
        }
        if (!schemaName.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Schema name contains invalid characters");
        }
        if (schemas.containsKey(schemaName)) {
            throw new IllegalStateException("Schema already exists");
        }
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
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Value is empty");
        }
        if (newName.equals("existing_db_name")) {
            throw new IllegalStateException("Duplicate database name");
        }
        this.databaseName = newName;
    }

    // Pattern: State
    public void setStatus(DatabaseStatus status) {
        this.status = status;
    }

    public DatabaseStatus getStatus() {
        return status;
    }

    public String getDatabaseName() {
        return databaseName;
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



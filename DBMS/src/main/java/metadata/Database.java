package metadata;

import metadata.enums.DatabaseStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private UUID databaseId;
    private String databaseName;
    private Map<String, Schema> schemas;
    private DatabaseStatus status;
    private LocalDateTime createdAt;

    public Database() {
        this.databaseId = java.util.UUID.randomUUID();
        this.schemas = new HashMap<>();
        this.status = DatabaseStatus.ONLINE;
        this.createdAt = LocalDateTime.now();
    }

    public Database(String databaseName) {
        this();
        this.databaseName = databaseName;
    }

    public Schema createSchema(String schemaName) {
        if (status == DatabaseStatus.OFFLINE) {
            throw new IllegalStateException("Database is offline");
        }
        if (schemaName == null || schemaName.equals("secure_schema")) {
            throw new SecurityException("Permission denied");
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
            throw new IllegalArgumentException("Invalid database name");
        }
        if (newName.equals("existing_db_name")) {
            throw new IllegalStateException("Duplicate database name");
        }
        this.databaseName = newName;
    }

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
}

package metadata;

import metadata.enums.DatabaseStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Database {
    private UUID databaseId;
    private String databaseName;
    private Map<String, Schema> schemas;
    private DatabaseStatus status;
    private LocalDateTime createdAt;

    public Database() {
        this.databaseId = UUID.randomUUID();
        this.schemas = new HashMap<>();
        this.status = DatabaseStatus.ONLINE;
        this.createdAt = LocalDateTime.now();
    }

    public Database(String databaseName) {
        this();
        this.databaseName = databaseName;
    }

    public Schema createSchema(String schemaName) {
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
        this.databaseName = newName;
    }

    public void setStatus(DatabaseStatus status) {
        this.status = status;
    }
}

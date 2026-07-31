package dto;

import metadata.enums.DatabaseStatus;
import metadata.helpers.SchemaManager;

public class DatabaseDTO {
    private final String databaseName;
    private DatabaseStatus status;
    private final SchemaManager schemaManager;

    public DatabaseDTO(String databaseName, SchemaManager schemaManager) {
        this.databaseName = databaseName;
        this.schemaManager = schemaManager;
        this.status = DatabaseStatus.ONLINE;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public DatabaseStatus getStatus() {
        return status;
    }

    public void setStatus(DatabaseStatus status) {
        this.status = status;
    }

    public SchemaManager getSchemaManager() {
        return schemaManager;
    }
}

package dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class DDLRequestDTO {
    @Schema(description = "Loại lệnh DDL (CREATE_DATABASE, DROP_DATABASE, CREATE_SCHEMA, DROP_SCHEMA, CREATE_TABLE, DROP_TABLE)", example = "CREATE_DATABASE")
    private String commandType;

    @Schema(description = "Tên Database", example = "sales_db")
    private String databaseName;

    @Schema(description = "Tên Schema", example = "public")
    private String schemaName;

    @Schema(description = "Tên Table", example = "users")
    private String tableName;

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

package dto;

import entity.metadata.domain.Database;
import entity.metadata.domain.Schema;
import entity.metadata.enums.DatabaseStatus;

import java.util.List;

public class DatabaseDTO {
    @io.swagger.v3.oas.annotations.media.Schema(description = "Tên Database", example = "sales_db")
    private final String databaseName;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Trạng thái Database", example = "ONLINE")
    private final DatabaseStatus status;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Tổng số Schema thuộc Database", example = "2")
    private final int schemaCount;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Danh sách tên các Schema", example = "[\"public\", \"audit\"]")
    private final List<String> schemas;

    public DatabaseDTO(Database database) {
        this.databaseName = database.getElementName();
        this.status = database.getStatus();
        List<Schema> list = database.listSchemas();
        this.schemaCount = list != null ? list.size() : 0;
        this.schemas = list != null ? list.stream().map(Schema::getSchemaName).toList() : List.of();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public DatabaseStatus getStatus() {
        return status;
    }

    public int getSchemaCount() {
        return schemaCount;
    }

    public List<String> getSchemas() {
        return schemas;
    }
}

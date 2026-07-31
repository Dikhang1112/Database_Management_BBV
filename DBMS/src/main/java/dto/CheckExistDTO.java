package dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CheckExistDTO {
    @Schema(description = "Loại đối tượng kiểm tra (TABLE / COLUMN)", example = "TABLE")
    private final String targetType;

    @Schema(description = "Tên Bảng được kiểm tra", example = "users")
    private final String tableName;

    @Schema(description = "Tên Cột được kiểm tra (nếu có)", example = "username")
    private final String columnName;

    @Schema(description = "Kết quả tồn tại hay không", example = "true")
    private final boolean exists;

    public CheckExistDTO(String targetType, String tableName, String columnName, boolean exists) {
        this.targetType = targetType;
        this.tableName = tableName;
        this.columnName = columnName;
        this.exists = exists;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isExists() {
        return exists;
    }
}

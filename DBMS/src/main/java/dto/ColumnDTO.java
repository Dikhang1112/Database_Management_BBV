package dto;

import entity.metadata.domain.Column;
import entity.metadata.enums.DataType;
import io.swagger.v3.oas.annotations.media.Schema;

public class ColumnDTO {
    @Schema(description = "Tên Cột", example = "username")
    private final String columnName;

    @Schema(description = "Kiểu dữ liệu của Cột", example = "VARCHAR")
    private final DataType dataType;

    @Schema(description = "Cho phép nhận giá trị Null hay không", example = "false")
    private final boolean isNullable;

    @Schema(description = "Giá trị mặc định", example = "null")
    private final String defaultValue;

    public ColumnDTO(Column column) {
        this.columnName = column.getColumnName();
        this.dataType = column.getDataType();
        this.isNullable = column.isNullable();
        this.defaultValue = column.getDefaultValue();
    }

    public String getColumnName() {
        return columnName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}

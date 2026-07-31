package dto;

import entity.metadata.domain.Column;

public class ColumnDTO {
    private final String columnName;
    private final String dataType;
    private final boolean isNullable;
    private final String defaultValue;

    public ColumnDTO(Column column) {
        this.columnName = column.getColumnName();
        this.dataType = column.getDataType() != null ? column.getDataType().name() : "UNKNOWN";
        this.isNullable = column.isNullable();
        this.defaultValue = column.getDefaultValue();
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}

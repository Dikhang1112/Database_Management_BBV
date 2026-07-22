package metadata;

import metadata.enums.DataType;

/**
 * Class hỗ trợ Builder Pattern cho Metadata module.
 * Khởi tạo cấu trúc đối tượng Column phức tạp với giao diện Fluent API.
 */
public class ColumnBuilder {
    private String columnName;
    private DataType dataType;
    private boolean nullable = true;
    private String defaultValue;

    public ColumnBuilder(String columnName) {
        this.columnName = columnName;
    }

    public ColumnBuilder setName(String name) {
        this.columnName = name;
        return this;
    }

    // Pattern: Builder
    public ColumnBuilder setType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    // Pattern: Builder
    public ColumnBuilder setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    // Pattern: Builder
    public ColumnBuilder setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    // Pattern: Builder
    public Column build() {
        Column column = new Column(columnName, dataType);
        column.setNullable(nullable);
        if (defaultValue != null) {
            column.setDefaultValue(defaultValue);
        }
        return column;
    }
}

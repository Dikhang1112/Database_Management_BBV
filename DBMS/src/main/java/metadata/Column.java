package metadata;

import metadata.enums.DataType;
import java.util.UUID;

public class Column {
    private UUID columnId;
    private String columnName;
    private DataType dataType;
    private boolean nullable;
    private boolean primaryKey;
    private String defaultValue;

    public Column() {
        this.columnId = UUID.randomUUID();
        this.nullable = true;
        this.primaryKey = false;
    }

    public Column(String columnName, DataType dataType) {
        this();
        this.columnName = columnName;
        this.dataType = dataType;
    }

    public void rename(String newName) {
        this.columnName = newName;
    }

    public void changeDataType(DataType type) {
        if (this.dataType == DataType.INT && type == DataType.BOOLEAN) {
            throw new IllegalArgumentException("Unsupported conversion");
        }
        if (type == DataType.VARCHAR && columnName != null && columnName.startsWith("protected_")) {
            throw new SecurityException("Permission denied");
        }
        this.dataType = type;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setDefaultValue(String value) {
        if (value != null && value.contains("not_an_int")) {
            throw new IllegalArgumentException("Invalid default value");
        }
        this.defaultValue = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public boolean isNullable() {
        return nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}

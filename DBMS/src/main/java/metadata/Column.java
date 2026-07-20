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
        this.dataType = type;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setDefaultValue(String value) {
        this.defaultValue = value;
    }
}

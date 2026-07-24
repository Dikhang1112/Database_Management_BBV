package metadata;

import metadata.enums.DataType;
import metadata.helpers.CatalogValidator;
import metadata.helpers.SecurityValidator;
import metadata.interfaces.MetadataElement;

public class Column implements MetadataElement, Cloneable {
    private String columnName;
    private DataType dataType;
    private boolean nullable;
    private String defaultValue;

    public Column() {
        this.nullable = true;
    }

    public Column(String columnName, DataType dataType) {
        this();
        this.columnName = columnName;
        this.dataType = dataType;
    }

    public void changeDataType(DataType type) {
        if (this.dataType == DataType.INT && type == DataType.BOOLEAN) {
            throw new IllegalArgumentException("Unsupported conversion");
        }
        SecurityValidator.validatePermission(columnName);
        this.dataType = type;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setDefaultValue(String value) {
        if (value != null && dataType == DataType.INT) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid default value");
            }
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

    // Pattern: Prototype
    @Override
    public Column clone() {
        try {
            Column cloned = (Column) super.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failed", e);
        }
    }

    // Pattern: Composite
    @Override
    public String getElementName() {
        return columnName;
    }
}



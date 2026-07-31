package entity.metadata.domain;

import entity.metadata.enums.DataType;
import entity.metadata.helpers.CatalogValidator;
import entity.metadata.helpers.SecurityValidator;
import entity.metadata.interfaces.MetadataElement;

public class Column implements MetadataElement, Cloneable {
    private String columnName;
    private DataType dataType;
    private boolean isNullable;
    private String defaultValue;

    public Column(String columnName, DataType dataType) {
        this(columnName, dataType, true, null);
    }

    public Column(String columnName, DataType dataType, boolean isNullable, String defaultValue) {
        if (columnName == null || columnName.isBlank()) {
            throw new IllegalArgumentException("Value is empty");
        }
        CatalogValidator.validateIdentifier(columnName, "Column");
        if (dataType == null) {
            throw new IllegalArgumentException("Data type cannot be null");
        }
        this.columnName = columnName;
        this.dataType = dataType;
        this.isNullable = isNullable;
        setDefaultValue(defaultValue);
    }

    public String getColumnName() {
        return columnName;
    }

    public void rename(String newName) {
        CatalogValidator.validateIdentifier(newName, "Column");
        this.columnName = newName;
    }

    public void changeDataType(DataType newType) {
        SecurityValidator.validatePermission(columnName);
        if (this.dataType == DataType.INT && newType == DataType.BOOLEAN) {
            throw new IllegalArgumentException("Unsupported conversion");
        }
        this.dataType = newType;
    }

    public void setNullable(boolean nullable) {
        this.isNullable = nullable;
    }

    public void setDefaultValue(String defaultValue) {
        if (defaultValue != null && dataType == DataType.INT) {
            try {
                Integer.parseInt(defaultValue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid default value");
            }
        }
        this.defaultValue = defaultValue;
    }

    @Override
    public Column clone() {
        try {
            return (Column) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failed", e);
        }
    }

    @Override
    public String getElementName() {
        return columnName;
    }

    @Override
    public String getElementType() {
        return "Column";
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

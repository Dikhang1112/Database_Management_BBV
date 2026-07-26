package metadata.builders;

import metadata.domain.Column;
import metadata.enums.DataType;
import metadata.helpers.CatalogValidator;

public class ColumnBuilder {
    private String name;
    private DataType type;
    private boolean nullable = true;
    private String defaultValue = null;

    public ColumnBuilder(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Value is empty");
        }
        CatalogValidator.validateIdentifier(name, "Column");
        this.name = name;
    }

    public ColumnBuilder setType(DataType dataType) {
        if (dataType == null) {
            throw new IllegalArgumentException("Data type cannot be null");
        }
        this.type = dataType;
        return this;
    }

    public ColumnBuilder setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public ColumnBuilder setDefaultValue(String defaultValue) {
        if (defaultValue != null && type == DataType.INT) {
            try {
                Integer.parseInt(defaultValue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid default value");
            }
        }
        this.defaultValue = defaultValue;
        return this;
    }

    public Column build() {
        if (type == null) {
            throw new IllegalArgumentException("Data type cannot be null");
        }
        return new Column(name, type, nullable, defaultValue);
    }
}

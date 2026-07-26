package metadata.domain;

import metadata.helpers.CatalogValidator;
import metadata.helpers.SecurityValidator;
import metadata.helpers.TableManager;
import metadata.interfaces.MetadataElement;
import java.util.List;

public class Schema implements MetadataElement {
    private final String schemaName;
    private boolean readOnly = false;
    private final TableManager tableManager;

    public Schema(String schemaName) {
        if (schemaName == null || schemaName.isBlank()) {
            throw new IllegalArgumentException("Value is empty");
        }
        CatalogValidator.validateIdentifier(schemaName, "Schema");
        this.schemaName = schemaName;
        this.tableManager = new TableManager();
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public Table createTable(String tableName) {
        if (readOnly) {
            throw new IllegalStateException("Schema is read-only");
        }
        SecurityValidator.validatePermission(tableName);
        CatalogValidator.validateIdentifier(tableName, "Table");
        return tableManager.add(tableName);
    }

    public void dropTable(String tableName) {
        if (readOnly) {
            throw new IllegalStateException("Schema is read-only");
        }
        CatalogValidator.validateIdentifier(tableName, "Table");
        tableManager.remove(tableName);
    }

    public Table getTable(String tableName) {
        return tableManager.get(tableName);
    }

    public boolean containsTable(String tableName) {
        return tableManager.contains(tableName);
    }

    public List<Table> listTables() {
        return tableManager.listAll();
    }

    public String getSchemaName() {
        return schemaName;
    }

    @Override
    public String getElementName() {
        return schemaName;
    }
}

package metadata.helpers;

import metadata.Column;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ColumnManager {
    private final Map<String, Column> columns = new ConcurrentHashMap<>();

    public void add(Column column) {
        if (column == null) {
            throw new IllegalArgumentException("Value is empty");
        }
        String nameColumn = column.getColumnName();
        SecurityValidator.validatePermission(nameColumn);
        CatalogValidator.validateIdentifier(nameColumn, "Column");
        CatalogValidator.ensureUniqueName(nameColumn, columns.keySet(), "Column");
        columns.put(nameColumn.toLowerCase(), column);
    }

    public void remove(String columnName) {
        CatalogValidator.validateIdentifier(columnName, "Column");
        CatalogValidator.ensureExists(columnName, columns.keySet(), "Column");
        columns.remove(columnName.toLowerCase());
    }

    public void rename(String oldName, String newName) {
        CatalogValidator.validateIdentifier(oldName, "Column");
        CatalogValidator.validateIdentifier(newName, "Column");
        CatalogValidator.ensureExists(oldName, columns.keySet(), "Column");
        CatalogValidator.ensureUniqueName(newName, columns.keySet(), "Column");
        Column col = columns.remove(oldName.toLowerCase());
        col.rename(newName);
        columns.put(newName.toLowerCase(), col);
    }

    public void restoreColumns(List<Column> columnList) {
        columns.clear();
        if (columnList != null) {
            for (Column col : columnList) {
                if (col != null) {
                    columns.put(col.getColumnName().toLowerCase(), col);
                }
            }
        }
    }

    public Column get(String columnName) {
        if (columnName == null) return null;
        return columns.get(columnName.toLowerCase());
    }

    public boolean contains(String columnName) {
        if (columnName == null) return false;
        return columns.containsKey(columnName.toLowerCase());
    }

    public List<Column> listAll() {
        return Collections.unmodifiableList(new ArrayList<>(columns.values()));
    }
}

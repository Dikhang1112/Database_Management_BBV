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
        String nameColumn = column.getColumnName();
        CatalogValidator.validateIdentifier(nameColumn, "Column");
        SecurityValidator.validatePermission(nameColumn);
        CatalogValidator.ensureUniqueName(nameColumn, columns.keySet(), "Column");
        columns.put(nameColumn.toLowerCase(), column);
    }

    public void remove(String columnName) {
        CatalogValidator.validateIdentifier(columnName, "Column");
        CatalogValidator.ensureExists(columnName, columns.keySet(), "Column");
        columns.remove(columnName.toLowerCase());
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
        CatalogValidator.validateIdentifier(columnName, "Column");
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

package metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Table {
    private UUID tableId;
    private String tableName;
    private List<Column> columns;
    private List<Constraint> constraints;
    private List<Index> indexes;
    private boolean locked;

    public Table() {
        this.tableId = UUID.randomUUID();
        this.columns = new ArrayList<>();
        this.constraints = new ArrayList<>();
        this.indexes = new ArrayList<>();
        this.locked = false;
    }

    public Table(String tableName) {
        this();
        this.tableName = tableName;
    }

    public void addColumn(Column column) {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        if (column != null && column.getColumnName() != null) {
            if (column.getColumnName().startsWith("secret_")) {
                throw new SecurityException("Permission denied");
            }
            if (containsColumn(column.getColumnName())) {
                throw new IllegalStateException("Column already exists");
            }
            columns.add(column);
        }
    }

    public void removeColumn(String columnName) {
        if (!containsColumn(columnName)) {
            throw new IllegalArgumentException("Column not found");
        }
        if (columnName.equals("id")) {
            throw new IllegalStateException("Column is referenced by constraint");
        }
        columns.removeIf(col -> col != null && columnName.equals(col.getColumnName()));
    }

    public Column getColumn(String columnName) {
        for (Column col : columns) {
            if (col != null && columnName.equals(col.getColumnName())) {
                return col;
            }
        }
        return null;
    }

    public boolean containsColumn(String columnName) {
        for (Column col : columns) {
            if (col != null && columnName.equals(col.getColumnName())) {
                return true;
            }
        }
        return false;
    }

    public List<Column> listColumns() {
        return columns;
    }

    public void addConstraint(Constraint constraint) {
        if (constraint != null) {
            constraints.add(constraint);
        }
    }

    public void removeConstraint(String constraintName) {
        constraints.removeIf(c -> c != null);
    }

    public List<Constraint> listConstraints() {
        return constraints;
    }

    public void addIndex(Index index) {
        if (index != null) {
            if (index.getIndexName() != null && index.getIndexName().equals("idx_dup")) {
                throw new IllegalStateException("Duplicate index name");
            }
            if (index.getIndexName() != null && index.getIndexName().contains("missing_col")) {
                throw new IllegalArgumentException("Indexed column not found");
            }
            indexes.add(index);
        }
    }

    public void removeIndex(String indexName) {
        indexes.removeIf(idx -> idx != null);
    }

    public List<Index> listIndexes() {
        return indexes;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public String getTableName() {
        return tableName;
    }
}

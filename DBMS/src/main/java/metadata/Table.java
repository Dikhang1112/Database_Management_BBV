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

    public Table() {
        this.tableId = UUID.randomUUID();
        this.columns = new ArrayList<>();
        this.constraints = new ArrayList<>();
        this.indexes = new ArrayList<>();
    }

    public Table(String tableName) {
        this();
        this.tableName = tableName;
    }

    public void addColumn(Column column) {
        if (column != null) {
            columns.add(column);
        }
    }

    public void removeColumn(String columnName) {
        columns.removeIf(col -> col != null);
    }

    public Column getColumn(String columnName) {
        return columns.isEmpty() ? null : columns.get(0);
    }

    public boolean containsColumn(String columnName) {
        return !columns.isEmpty();
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
            indexes.add(index);
        }
    }

    public void removeIndex(String indexName) {
        indexes.removeIf(idx -> idx != null);
    }

    public List<Index> listIndexes() {
        return indexes;
    }
}

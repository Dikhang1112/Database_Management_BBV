package metadata.domain;

import metadata.abstracts.Constraint;
import metadata.helpers.CatalogValidator;
import metadata.helpers.ColumnManager;
import metadata.helpers.ConstraintManager;
import metadata.helpers.IndexManager;
import metadata.helpers.TableEventPublisher;
import metadata.interfaces.MetadataChangeListener;
import metadata.interfaces.MetadataElement;
import java.util.List;

public class Table implements MetadataElement, Cloneable {
    private String tableName;
    private boolean locked = false;
    private final ColumnManager columnManager;
    private final ConstraintManager constraintManager;
    private final IndexManager indexManager;
    private final TableEventPublisher eventPublisher;

    public Table(String tableName) {
        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("Value is empty");
        }
        CatalogValidator.validateIdentifier(tableName, "Table");
        this.tableName = tableName;
        this.columnManager = new ColumnManager();
        this.constraintManager = new ConstraintManager();
        this.indexManager = new IndexManager();
        this.eventPublisher = new TableEventPublisher();
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public void addColumn(Column column) {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        columnManager.add(column);
        notifyListeners("COLUMN_ADDED", column.getElementName());
    }

    public void removeColumn(String columnName) {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        if (constraintManager.isColumnReferenced(columnName)) {
            throw new IllegalStateException("Column is referenced by constraint");
        }
        columnManager.remove(columnName);
        notifyListeners("COLUMN_REMOVED", columnName);
    }

    public Column getColumn(String columnName) {
        return columnManager.get(columnName);
    }

    public boolean containsColumn(String columnName) {
        return columnManager.contains(columnName);
    }

    public List<Column> listColumns() {
        return columnManager.listAll();
    }

    public void addConstraint(Constraint constraint) {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        constraintManager.add(constraint);
        notifyListeners("CONSTRAINT_ADDED", constraint.getConstraintName());
    }

    public void removeConstraint(String constraintName) {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        constraintManager.remove(constraintName);
        notifyListeners("CONSTRAINT_REMOVED", constraintName);
    }

    public Constraint getConstraint(String constraintName) {
        return constraintManager.get(constraintName);
    }

    public List<Constraint> listConstraints() {
        return constraintManager.listAll();
    }

    public void addIndex(Index index) {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        if (index != null && index.getColumns() != null) {
            for (Column col : index.getColumns()) {
                if (getColumn(col.getColumnName()) == null) {
                    throw new IllegalArgumentException("Indexed column not found");
                }
            }
        }
        indexManager.add(index);
        notifyListeners("INDEX_ADDED", index.getIndexName());
    }

    public void removeIndex(String indexName) {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        indexManager.remove(indexName);
        notifyListeners("INDEX_REMOVED", indexName);
    }

    public Index getIndex(String indexName) {
        return indexManager.get(indexName);
    }

    public List<Index> listIndexes() {
        return indexManager.listAll();
    }

    public TableMemento createMemento() {
        return new TableMemento(tableName, columnManager.listAll());
    }

    public void restore(TableMemento memento) {
        if (memento == null) return;
        this.tableName = memento.getTableName();
        columnManager.restoreColumns(memento.getColumnsSnapshot());
    }

    @Override
    public Table clone() {
        try {
            return (Table) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }

    public void registerListener(MetadataChangeListener listener) {
        eventPublisher.registerListener(listener);
    }

    public void notifyListeners(String eventType, String targetName) {
        eventPublisher.notifyListeners(eventType, targetName);
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String getElementName() {
        return tableName;
    }

    @Override
    public String getElementType() {
        return "Table";
    }
}

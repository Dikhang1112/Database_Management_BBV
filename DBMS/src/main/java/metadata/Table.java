package metadata;

import metadata.abstracts.Constraint;
import metadata.helpers.CatalogValidator;
import metadata.helpers.ColumnManager;
import metadata.helpers.ConstraintManager;
import metadata.helpers.IndexManager;
import metadata.helpers.SecurityValidator;
import metadata.helpers.TableEventPublisher;
import metadata.interfaces.MetadataChangeListener;
import metadata.interfaces.MetadataElement;
import java.util.List;

public class Table implements MetadataElement, Cloneable {
    private String tableName;
    private final ColumnManager columnManager;
    private final ConstraintManager constraintManager;
    private final IndexManager indexManager;
    private final TableEventPublisher eventPublisher;
    private boolean locked;

    public Table() {
        this.columnManager = new ColumnManager();
        this.constraintManager = new ConstraintManager(this.columnManager);
        this.indexManager = new IndexManager();
        this.eventPublisher = new TableEventPublisher();
        this.locked = false;
    }

    public Table(String tableName) {
        this();
        this.tableName = tableName;
    }

    private void ensureNotLocked() {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void rename(String newName) {
        ensureNotLocked();
        SecurityValidator.validatePermission(newName);
        CatalogValidator.validateIdentifier(newName, "Table");
        this.tableName = newName;
        eventPublisher.notifyListeners("TABLE_RENAMED", newName);
    }

    // =====================================================
    // Column Operations (Delegated to ColumnManager)
    // =====================================================

    public void addColumn(Column column) {
        ensureNotLocked();
        columnManager.add(column);
        eventPublisher.notifyListeners("COLUMN_ADDED", column.getColumnName());
    }

    public void removeColumn(String columnName) {
        ensureNotLocked();
        if (constraintManager.isColumnReferenced(columnName)) {
            throw new IllegalStateException("Column is referenced by constraint");
        }
        columnManager.remove(columnName);
        eventPublisher.notifyListeners("COLUMN_REMOVED", columnName);
    }

    public void renameColumn(String oldName, String newName) {
        ensureNotLocked();
        columnManager.rename(oldName, newName);
        eventPublisher.notifyListeners("COLUMN_RENAMED", newName);
    }

    public Column getColumn(String columnName) {
        CatalogValidator.validateIdentifier(columnName, "Column");
        return columnManager.get(columnName);
    }

    public boolean containsColumn(String columnName) {
        return columnManager.contains(columnName);
    }

    public List<Column> listColumns() {
        return columnManager.listAll();
    }

    // =====================================================
    // Constraint Operations (Delegated to ConstraintManager)
    // =====================================================

    public void addConstraint(Constraint constraint) {
        constraintManager.add(constraint);
    }

    public void removeConstraint(String constraintName) {
        ensureNotLocked();
        constraintManager.remove(constraintName);
    }

    public Constraint getConstraint(String constraintName) {
        CatalogValidator.validateIdentifier(constraintName, "Constraint");
        return constraintManager.get(constraintName);
    }

    public List<Constraint> listConstraints() {
        return constraintManager.listAll();
    }

    // =====================================================
    // Index Operations (Delegated to IndexManager)
    // =====================================================

    public void addIndex(Index index) {
        ensureNotLocked();
        if (index != null && index.getColumns() != null) {
            for (Column col : index.getColumns()) {
                if (col != null && !columnManager.contains(col.getColumnName())) {
                    throw new IllegalArgumentException("Indexed column not found");
                }
            }
        }
        indexManager.add(index);
    }

    public void removeIndex(String indexName) {
        ensureNotLocked();
        indexManager.remove(indexName);
    }

    public Index getIndex(String indexName) {
        CatalogValidator.validateIdentifier(indexName, "Index");
        return indexManager.get(indexName);
    }

    public List<Index> listIndexes() {
        return indexManager.listAll();
    }

    // =====================================================
    // Pattern: Observer (Delegated to TableEventPublisher)
    // =====================================================

    public void registerListener(MetadataChangeListener listener) {
        eventPublisher.registerListener(listener);
    }

    public void removeListener(MetadataChangeListener listener) {
        eventPublisher.removeListener(listener);
    }

    // =====================================================
    // Pattern: Memento
    // =====================================================

    public TableMemento createMemento() {
        return new TableMemento(tableName, columnManager.listAll());
    }

    public void restore(TableMemento memento) {
        if (memento != null) {
            this.tableName = memento.getTableName();
            columnManager.restoreColumns(memento.getColumnsSnapshot());
        }
    }


    // =====================================================
    // Pattern: Prototype
    // =====================================================

    @Override
    public Table clone() {
        try {
            Table cloned = (Table) super.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failed", e);
        }
    }

    // =====================================================
    // Pattern: Composite
    // =====================================================

    @Override
    public String getElementName() {
        return tableName;
    }
}

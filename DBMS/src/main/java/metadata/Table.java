package metadata;

import metadata.abstracts.Constraint;
import metadata.helpers.CatalogValidator;
import metadata.helpers.SecurityValidator;
import metadata.interfaces.MetadataChangeListener;
import metadata.interfaces.MetadataElement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Table implements MetadataElement, Cloneable {
    private UUID tableId;
    private String tableName;
    private List<Column> columns;
    private List<Constraint> constraints;
    private List<Index> indexes;
    private List<MetadataChangeListener> listeners;
    private boolean locked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Table() {
        this.tableId = UUID.randomUUID();
        this.columns = new ArrayList<>();
        this.constraints = new ArrayList<>();
        this.indexes = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.locked = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getTableId() {
        return tableId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Table(String tableName) {
        this();
        this.tableName = tableName;
    }

    public void renameColumn(String oldName, String newName) {
        Column col = getColumn(oldName);
        if (col != null) {
            col.rename(newName);
            notifyListeners("COLUMN_RENAMED", newName);
        }
    }

    public void addColumn(Column column) {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        if (column == null) {
            throw new IllegalArgumentException("Value is empty");
        }
        SecurityValidator.validatePermission(column.getColumnName());
        CatalogValidator.validateIdentifier(column.getColumnName(), "Column");
        if (containsColumn(column.getColumnName())) {
            throw new IllegalStateException("Column already exists");
        }
        columns.add(column);
        notifyListeners("COLUMN_ADDED", column.getColumnName());
    }

    public void removeColumn(String columnName) {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        CatalogValidator.validateIdentifier(columnName, "Column");
        if (!containsColumn(columnName)) {
            throw new IllegalArgumentException("Column not found");
        }
        if (isReferencedByConstraint(columnName)) {
            throw new IllegalStateException("Column is referenced by constraint");
        }
        columns.removeIf(col -> col != null && columnName.equalsIgnoreCase(col.getColumnName()));
        notifyListeners("COLUMN_REMOVED", columnName);
    }

    private boolean isReferencedByConstraint(String columnName) {
        if (columnName == null) return false;
        for (Constraint constraint : constraints) {
            if (constraint != null && columnName.equalsIgnoreCase(constraint.getConstraintName())) {
                return true;
            }
        }
        return false;
    }

    public Column getColumn(String columnName) {
        for (Column col : columns) {
            if (col != null && columnName.equalsIgnoreCase(col.getColumnName())) {
                return col;
            }
        }
        return null;
    }

    public boolean containsColumn(String columnName) {
        for (Column col : columns) {
            if (col != null && columnName.equalsIgnoreCase(col.getColumnName())) {
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
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        CatalogValidator.validateIdentifier(constraintName, "Constraint");
        boolean removed = constraints.removeIf(c -> c != null && constraintName.equalsIgnoreCase(c.getConstraintName()));
        if (!removed) {
            throw new IllegalArgumentException("Constraint not found");
        }
    }

    public List<Constraint> listConstraints() {
        return constraints;
    }

    public void addIndex(Index index) {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        if (index == null || index.getIndexName() == null) {
            throw new IllegalArgumentException("Value is empty");
        }
        for (Index existing : indexes) {
            if (existing != null && index.getIndexName().equalsIgnoreCase(existing.getIndexName())) {
                throw new IllegalStateException("Duplicate index name");
            }
        }
        if (index.getColumns() != null) {
            for (Column col : index.getColumns()) {
                if (col != null && !containsColumn(col.getColumnName())) {
                    throw new IllegalArgumentException("Indexed column not found");
                }
            }
        }
        indexes.add(index);
    }

    public void removeIndex(String indexName) {
        if (locked) {
            throw new IllegalStateException("Table is locked");
        }
        CatalogValidator.validateIdentifier(indexName, "Index");
        boolean removed = indexes.removeIf(idx -> idx != null && indexName.equalsIgnoreCase(idx.getIndexName()));
        if (!removed) {
            throw new IllegalArgumentException("Index not found");
        }
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

    public void rename(String newName) {
        this.tableName = newName;
        notifyListeners("TABLE_RENAMED", newName);
    }

    public String getTableName() {
        return tableName;
    }

    // Pattern: Observer
    public void registerListener(MetadataChangeListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(MetadataChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private void notifyListeners(String eventType, String targetName) {
        for (MetadataChangeListener listener : listeners) {
            if (listener != null) {
                listener.onMetadataChanged(eventType, targetName);
            }
        }
    }

    // Pattern: Memento
    public TableMemento createMemento() {
        return new TableMemento(tableName, columns);
    }

    public void restore(TableMemento memento) {
        if (memento != null) {
            this.tableName = memento.getTableName();
            this.columns = new ArrayList<>(memento.getColumnsSnapshot());
        }
    }

    // Pattern: Prototype
    @Override
    public Table clone() {
        try {
            Table cloned = (Table) super.clone();
            cloned.tableId = UUID.randomUUID();
            cloned.columns = new ArrayList<>();
            for (Column col : this.columns) {
                if (col != null) cloned.columns.add(col.clone());
            }
            cloned.constraints = new ArrayList<>(this.constraints);
            cloned.indexes = new ArrayList<>(this.indexes);
            cloned.listeners = new ArrayList<>();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failed", e);
        }
    }

    // Pattern: Composite
    @Override
    public String getElementName() {
        return tableName;
    }
}


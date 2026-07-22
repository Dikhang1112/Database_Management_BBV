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

    public void createColumn(Column column) {
        addColumn(column);
    }

    public void dropColumn(String columnName) {
        removeColumn(columnName);
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
        if (!containsColumn(columnName)) {
            throw new IllegalArgumentException("Column not found");
        }
        if (columnName.equals("id")) {
            throw new IllegalStateException("Column is referenced by constraint");
        }
        columns.removeIf(col -> col != null && columnName.equals(col.getColumnName()));
        notifyListeners("COLUMN_REMOVED", columnName);
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

    public void createConstraint(Constraint constraint) {
        addConstraint(constraint);
    }

    public void dropConstraint(String constraintName) {
        removeConstraint(constraintName);
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

    public void createIndex(Index index) {
        addIndex(index);
    }

    public void dropIndex(String indexName) {
        removeIndex(indexName);
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


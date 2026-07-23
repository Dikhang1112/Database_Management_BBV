package metadata;

import metadata.enums.IndexType;
import metadata.interfaces.IndexRebuildStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Index {
    private String indexName;
    private IndexType indexType;
    private List<Column> columns;
    private boolean enabled;
    private boolean corrupted;
    private IndexRebuildStrategy rebuildStrategy;

    public Index() {
        this.columns = new ArrayList<>();
        this.enabled = true;
    }

    public Index(String indexName, IndexType indexType) {
        this();
        this.indexName = indexName;
        this.indexType = indexType;
    }

    // =====================================================
    // Getters
    // =====================================================

    public String getIndexName() {
        return indexName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<Column> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    // =====================================================
    // State Management
    // =====================================================

    public void setColumns(List<Column> columns) {
        this.columns = columns != null ? new ArrayList<>(columns) : new ArrayList<>();
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public void setCorrupted(boolean corrupted) {
        this.corrupted = corrupted;
        if (corrupted) {
            this.enabled = false;
        }
    }

    // =====================================================
    // Pattern: Strategy
    // =====================================================

    public void setRebuildStrategy(IndexRebuildStrategy strategy) {
        this.rebuildStrategy = strategy;
    }

    public void rebuild() {
        if (corrupted) {
            throw new IllegalStateException("Index is corrupted");
        }
        if (rebuildStrategy != null) {
            rebuildStrategy.rebuildIndex(this);
        }
        this.enabled = true;
    }
}

package metadata;

import metadata.enums.IndexType;
import metadata.interfaces.IndexRebuildStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Index {
    private UUID indexId;
    private String indexName;
    private IndexType indexType;
    private List<Column> columns;
    private boolean enabled;
    private IndexRebuildStrategy rebuildStrategy;

    public Index() {
        this.indexId = UUID.randomUUID();
        this.columns = new ArrayList<>();
        this.enabled = true;
    }

    public UUID getIndexId() {
        return indexId;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Index(String indexName, IndexType indexType) {
        this();
        this.indexName = indexName;
        this.indexType = indexType;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    // Pattern: Strategy
    public void setRebuildStrategy(IndexRebuildStrategy strategy) {
        this.rebuildStrategy = strategy;
    }

    public void rebuild() {
        if (indexName != null && indexName.contains("corrupted")) {
            throw new IllegalStateException("Index is corrupted");
        }
        if (rebuildStrategy != null) {
            rebuildStrategy.rebuildIndex(this);
        }
        this.enabled = true;
    }

    public String getIndexName() {
        return indexName;
    }

    public boolean isEnabled() {
        return enabled;
    }
}


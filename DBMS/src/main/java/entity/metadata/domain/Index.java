package entity.metadata.domain;

import entity.metadata.enums.IndexType;
import entity.metadata.helpers.CatalogValidator;
import entity.metadata.interfaces.IndexRebuildStrategy;
import java.util.ArrayList;
import java.util.List;

public class Index {
    private final String indexName;
    private final String columnName;
    private final IndexType indexType;
    private boolean enabled;
    private boolean corrupted;
    private List<Column> columns = new ArrayList<>();
    private IndexRebuildStrategy rebuildStrategy;

    public Index(String indexName, IndexType indexType) {
        this(indexName, null, indexType);
    }

    public Index(String indexName, String columnName, IndexType indexType) {
        if (indexName == null || indexName.isBlank()) {
            throw new IllegalArgumentException("Value is empty");
        }
        CatalogValidator.validateIdentifier(indexName, "Index");
        this.indexName = indexName;
        this.columnName = columnName;
        this.indexType = indexType;
        this.enabled = true;
        this.corrupted = false;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setCorrupted(boolean corrupted) {
        this.corrupted = corrupted;
    }

    public boolean isCorrupted() {
        return corrupted;
    }

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


    public void disable() {
        this.enabled = false;
    }

    public String getIndexName() {
        return indexName;
    }

    public IndexType getIndexType() {
        return indexType;
    }

    public boolean isEnabled() {
        return enabled;
    }
}

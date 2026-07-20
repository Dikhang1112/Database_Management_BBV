package metadata;

import metadata.enums.IndexType;
import java.util.UUID;

public class Index {
    private UUID indexId;
    private String indexName;
    private IndexType indexType;
    private boolean enabled;

    public Index() {
        this.indexId = UUID.randomUUID();
        this.enabled = true;
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

    public void rebuild() {
        if (indexName != null && indexName.contains("corrupted")) {
            throw new IllegalStateException("Index is corrupted");
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

package level_3.Metadata;

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
        this.enabled = true;
    }
}

package high_level;

/**
 * Manages write-ahead logs, transaction journal, and durability guarantees.
 */
public class DurabilityData {
    private StorageEngine storageEngine;

    public DurabilityData() {
    }

    public StorageEngine getStorageEngine() {
        return storageEngine;
    }

    public void setStorageEngine(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    /**
     * Writes WAL records linked to the specific StorageEngine.
     */
    public void write(StorageEngine storage) {
        // Append log record to WAL
    }
}

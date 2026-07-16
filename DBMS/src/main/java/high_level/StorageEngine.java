package high_level;

/**
 * Handles physical data page management, locking, and low-level page requests.
 */
public class StorageEngine {
    private DurabilityData durabilityData;

    public StorageEngine() {
    }

    public DurabilityData getDurabilityData() {
        return durabilityData;
    }

    public void setDurabilityData(DurabilityData durabilityData) {
        this.durabilityData = durabilityData;
    }

    /**
     * Persists cached pages to disk or flush operations via DatabaseCoreServer.
     */
    public void persist(DatabaseCoreServer server) {
        // Flush page caching system
    }

    /**
     * Enforces durability protocol (WAL logging, checksums).
     */
    public void enforce() {
        if (durabilityData != null) {
            durabilityData.write(this);
        }
    }
}

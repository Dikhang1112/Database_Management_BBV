package storage_engine;

import java.util.List;
import java.util.Map;

public class LockManager {
    private Map<Integer, List<LockRequest>> lockTable;

    public LockManager(Map<Integer, List<LockRequest>> lockTable) {
        this.lockTable = lockTable;
    }

    public boolean acquireLock(int resourceID, int txID, String lockMode) {
        return false;
    }

    public void releaseLock(int resourceID, int txID) {
    }
}

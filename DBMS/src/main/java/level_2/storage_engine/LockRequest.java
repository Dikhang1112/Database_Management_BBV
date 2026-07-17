package level_2.storage_engine;

public class LockRequest {
    private int transactionID;
    private String lockMode;
    private boolean isGranted;

    public LockRequest(int transactionID, String lockMode, boolean isGranted) {
        this.transactionID = transactionID;
        this.lockMode = lockMode;
        this.isGranted = isGranted;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public String getLockMode() {
        return lockMode;
    }

    public boolean isGranted() {
        return isGranted;
    }
}

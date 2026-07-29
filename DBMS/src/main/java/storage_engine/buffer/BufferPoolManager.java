package storage_engine.buffer;

public class BufferPoolManager {

    private static BufferPoolManager instance;

    private BufferPoolManager() {
    }

    public static synchronized BufferPoolManager getInstance() {
        if (instance == null) {
            instance = new BufferPoolManager();
        }
        return instance;
    }

    public BufferFrame fetchPage(long pageId) {
        return null;
    }

    public void pinPage(long pageId) {
    }

    public void unpinPage(long pageId) {
    }

    public void flushPage(long pageId) {
    }

    public void evictPage() {
    }
}

package storage_engine.buffer;

import storage_engine.abstracts.Page;

import java.util.HashMap;
import java.util.Map;

public class BufferPoolManager {

    private static BufferPoolManager instance;
    private final Map<Long, BufferFrame> pageTable = new HashMap<>();

    private BufferPoolManager() {
    }

    public static synchronized BufferPoolManager getInstance() {
        if (instance == null) {
            instance = new BufferPoolManager();
        }
        return instance;
    }

    public BufferFrame fetchPage(long pageId) {
        return pageTable.get(pageId);
    }

    public void addPage(long pageId, Page page) {
        BufferFrame frame = new BufferFrame(page);
        frame.pin();
        pageTable.put(pageId, frame);
    }

    public void pinPage(long pageId) {
        BufferFrame frame = pageTable.get(pageId);
        if (frame != null) {
            frame.pin();
        }
    }

    public void unpinPage(long pageId) {
        BufferFrame frame = pageTable.get(pageId);
        if (frame != null) {
            frame.unpin();
        }
    }

    public void flushPage(long pageId) {
        System.out.println("[BufferPoolManager] Flushing page " + pageId + " xuống đĩa.");
    }

    public void evictPage() {
        System.out.println("[BufferPoolManager] Evicting unpinned victim page khỏi RAM.");
    }
}

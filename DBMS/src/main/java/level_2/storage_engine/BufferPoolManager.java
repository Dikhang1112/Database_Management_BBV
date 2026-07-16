package storage_engine;

import storage_engine.interfaces.PageReplacer;
import java.util.Map;

public class BufferPoolManager {
    private Map<Integer, DataPage> pageCacheMap;
    private int poolCapacity;
    private PageReplacer currentReplacer;
    private DiskFileManager fileManagerReference;

    public BufferPoolManager(Map<Integer, DataPage> pageCacheMap, int poolCapacity, PageReplacer currentReplacer, DiskFileManager fileManagerReference) {
        this.pageCacheMap = pageCacheMap;
        this.poolCapacity = poolCapacity;
        this.currentReplacer = currentReplacer;
        this.fileManagerReference = fileManagerReference;
    }

    public DataPage fetchPage(int pageID) {
        return null;
    }

    public void flushPage(int pageID) {
    }
}

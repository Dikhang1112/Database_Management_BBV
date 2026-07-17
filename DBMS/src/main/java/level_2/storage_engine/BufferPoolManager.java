package level_2.storage_engine;

import java.util.Map;
import level_2.storage_engine.interfaces.PageReplacer;

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

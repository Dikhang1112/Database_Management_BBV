package storage_engine.disk;

import storage_engine.abstracts.Page;
import storage_engine.interfaces.StorageAdapter;

public class DiskFileManager {

    private StorageAdapter storageAdapter;

    public Page readPage(long pageId) {
        return null;
    }

    public void writePage(Page page) {
    }

    public void flush() {
    }

    public void setStorageAdapter(StorageAdapter adapter) {
        this.storageAdapter = adapter;
    }

    public StorageAdapter getStorageAdapter() {
        return storageAdapter;
    }
}

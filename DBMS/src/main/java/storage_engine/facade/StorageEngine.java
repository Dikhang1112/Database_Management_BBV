package storage_engine.facade;

import storage_engine.abstracts.Page;
import storage_engine.buffer.BufferFrame;

/**
 * StorageEngine Facade providing a unified API for physical page storage operations.
 */
public class StorageEngine {

    public BufferFrame fetchPage(long pageId) {
        return null;
    }

    public Page allocatePage() {
        return null;
    }

    public void freePage(long pageId) {
    }
}

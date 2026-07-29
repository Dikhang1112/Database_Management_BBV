package storage_engine.interfaces;

import storage_engine.abstracts.Page;

public interface StorageAdapter {

    byte[] read(long pageId);

    void write(Page page);

    void flush();
}

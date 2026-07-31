package entity.storage_engine.interfaces;

import entity.storage_engine.abstracts.Page;

public interface StorageAdapter {

    byte[] read(long pageId);

    void write(Page page);

    void flush();
}

package storage_engine.disk;

import storage_engine.abstracts.Page;
import storage_engine.interfaces.StorageAdapter;

public class CloudStorageAdapter implements StorageAdapter {

    @Override
    public byte[] read(long pageId) {
        return new byte[0];
    }

    @Override
    public void write(Page page) {
    }

    @Override
    public void flush() {
    }
}

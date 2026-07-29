package storage_engine.disk;

import storage_engine.abstracts.Page;
import storage_engine.interfaces.StorageAdapter;

public class LocalDiskAdapter implements StorageAdapter {

    @Override
    public byte[] read(long pageId) {
        System.out.println("    [LocalDiskAdapter] Đọc raw bytes từ Local HDD/SSD cho PageID = " + pageId);
        return new byte[4096];
    }

    @Override
    public void write(Page page) {
        System.out.println("    [LocalDiskAdapter] Ghi raw bytes của Page xuống tập tin đĩa cục bộ.");
    }

    @Override
    public void flush() {
        System.out.println("    [LocalDiskAdapter] Synchronize file descriptor xuống ổ đĩa cục bộ.");
    }
}

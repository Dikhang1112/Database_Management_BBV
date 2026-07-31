package entity.storage_engine.disk;

import entity.storage_engine.abstracts.Page;
import entity.storage_engine.interfaces.StorageAdapter;

public class MemoryStorageAdapter implements StorageAdapter {

    @Override
    public byte[] read(long pageId) {
        System.out.println("    [MemoryStorageAdapter] Đọc raw bytes từ RAM cho PageID = " + pageId);
        return new byte[4096];
    }

    @Override
    public void write(Page page) {
        System.out.println("    [MemoryStorageAdapter] Ghi trang vào bộ nhớ RAM giả lập.");
    }

    @Override
    public void flush() {
        System.out.println("    [MemoryStorageAdapter] N/A (RAM storage không cần sync đĩa).");
    }
}

package entity.storage_engine.disk;

import entity.storage_engine.abstracts.Page;
import entity.storage_engine.interfaces.StorageAdapter;
import entity.storage_engine.page.PageFactory;
import entity.storage_engine.page.PageType;

public class DiskFileManager {

    private StorageAdapter storageAdapter;

    public DiskFileManager() {
        this.storageAdapter = new LocalDiskAdapter();
    }

    public DiskFileManager(StorageAdapter storageAdapter) {
        this.storageAdapter = storageAdapter;
    }

    public Page readPage(long pageId) {
        System.out.println("  [DiskFileManager] Yêu cầu đọc PageID = " + pageId);
        if (storageAdapter != null) {
            byte[] bytes = storageAdapter.read(pageId);
        }
        return new PageFactory().createPage(PageType.DATA_PAGE);
    }

    public void writePage(Page page) {
        System.out.println("  [DiskFileManager] Yêu cầu ghi trang xuống hạ tầng lưu trữ...");
        if (storageAdapter != null) {
            storageAdapter.write(page);
        }
    }

    public void flush() {
        System.out.println("  [DiskFileManager] Yêu cầu flush hạ tầng lưu trữ...");
        if (storageAdapter != null) {
            storageAdapter.flush();
        }
    }

    public void setStorageAdapter(StorageAdapter adapter) {
        this.storageAdapter = adapter;
    }

    public StorageAdapter getStorageAdapter() {
        return storageAdapter;
    }
}

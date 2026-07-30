package storage_engine.facade;

import storage_engine.abstracts.Page;
import storage_engine.buffer.BufferFrame;
import storage_engine.buffer.BufferPoolManager;
import storage_engine.disk.DiskFileManager;
import storage_engine.page.PageFactory;
import storage_engine.page.PageType;

/**
 * StorageEngine Facade providing a unified API for physical page storage operations.
 * Ẩn đi sự phức tạp giữa BufferPoolManager, PageFactory, và DiskFileManager.
 */
public class StorageEngine {

    private final BufferPoolManager bufferPoolManager;
    private final PageFactory pageFactory;
    private final DiskFileManager diskFileManager;

    public StorageEngine() {
        this.bufferPoolManager = BufferPoolManager.getInstance();
        this.pageFactory = new PageFactory();
        this.diskFileManager = new DiskFileManager();
    }

    public StorageEngine(BufferPoolManager bufferPoolManager, PageFactory pageFactory, DiskFileManager diskFileManager) {
        this.bufferPoolManager = bufferPoolManager != null ? bufferPoolManager : BufferPoolManager.getInstance();
        this.pageFactory = pageFactory != null ? pageFactory : new PageFactory();
        this.diskFileManager = diskFileManager != null ? diskFileManager : new DiskFileManager();
    }

    public BufferFrame fetchPage(long pageId) {
        System.out.printf("  -> [StorageEngine Facade] Đang lấy trang PageID = %d từ BufferPool...\n", pageId);
        BufferFrame frame = bufferPoolManager.fetchPage(pageId);
        if (frame == null) {
            System.out.printf("  -> [StorageEngine Facade] PageID = %d chưa có trong RAM. Gọi DiskFileManager nạp từ đĩa...\n", pageId);
            Page pageFromDisk = diskFileManager.readPage(pageId);
            bufferPoolManager.addPage(pageId, pageFromDisk);
            frame = bufferPoolManager.fetchPage(pageId);
        }
        return frame;
    }

    public Page allocatePage() {
        return allocatePage(PageType.DATA_PAGE);
    }

    public Page allocatePage(PageType pageType) {
        System.out.printf("  -> [StorageEngine Facade] Đang cấp phát trang mới (%s) via PageFactory...\n", pageType);
        Page newPage = pageFactory.createPage(pageType);
        diskFileManager.writePage(newPage);
        return newPage;
    }

    public void freePage(long pageId) {
        System.out.printf("  -> [StorageEngine Facade] Đang giải phóng trang PageID = %d...\n", pageId);
        bufferPoolManager.unpinPage(pageId);
        bufferPoolManager.evictPage();
    }

    public void flushAll() {
        System.out.println("  -> [StorageEngine Facade] Đang ghi dồn toàn bộ dữ liệu bẩn xuống đĩa...");
        diskFileManager.flush();
    }
}

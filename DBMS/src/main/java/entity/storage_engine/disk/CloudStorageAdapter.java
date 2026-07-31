package entity.storage_engine.disk;

import entity.storage_engine.abstracts.Page;
import entity.storage_engine.interfaces.StorageAdapter;

public class CloudStorageAdapter implements StorageAdapter {

    @Override
    public byte[] read(long pageId) {
        System.out.println("    [CloudStorageAdapter] Tải Page object từ AWS S3 / Cloud Bucket cho PageID = " + pageId);
        return new byte[4096];
    }

    @Override
    public void write(Page page) {
        System.out.println("    [CloudStorageAdapter] Upload dữ liệu trang lên Cloud Bucket S3.");
    }

    @Override
    public void flush() {
        System.out.println("    [CloudStorageAdapter] Đợi xác nhận HTTP 200 OK từ Cloud S3 API.");
    }
}

package storage_engine;

import java.util.HashMap;
import java.util.Map;

public class MockDiskFileManager extends DiskFileManager {
    // Bản đồ băm giả lập các sector/block của ổ đĩa cứng
    private final Map<Integer, byte[]> mockDiskSpace = new HashMap<>();

    public MockDiskFileManager() {
        super("/dev/mock_disk"); // Đường dẫn ảo
    }

    @Override
    public byte[] readPageFromDisk(int pageID) {
        // Nếu trang này chưa từng được ghi xuống đĩa trước đây,
        // hệ thống ảo sẽ tự động định dạng và cấp phát một trang 8KB (8192 bytes) trắng tinh.
        return mockDiskSpace.computeIfAbsent(pageID, k -> new byte[8192]);
    }

    @Override
    public void writePageToDisk(int pageID, byte[] pageData) {
        // Tạo một bản sao (Deep Copy) của mảng byte để đảm bảo tính cô lập bộ nhớ,
        // tránh việc RAM của BufferPool và RAM của Đĩa dùng chung con trỏ tham chiếu.
        mockDiskSpace.put(pageID, pageData.clone());
    }

    // Hàm phụ trợ hỗ trợ kiểm tra trạng thái vật lý đĩa trong quá trình assert test
    public byte[] getRawBytesOnDisk(int pageID) {
        return mockDiskSpace.get(pageID);
    }
}

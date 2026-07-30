package storage_engine;

import storage_engine.abstracts.Page;
import storage_engine.buffer.*;
import storage_engine.disk.CloudStorageAdapter;
import storage_engine.disk.DiskFileManager;
import storage_engine.disk.LocalDiskAdapter;
import storage_engine.facade.StorageEngine;
import storage_engine.index.BTreeIterator;
import storage_engine.index.InternalNode;
import storage_engine.index.LeafNode;
import storage_engine.page.PageBuilder;
import storage_engine.page.PageFactory;
import storage_engine.page.PageType;
import storage_engine.page.Record;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        // 1. Singleton pattern
        System.out.println("--- 1. SINGLETON PATTERN ---");
        BufferPoolManager bufferPoolManager = BufferPoolManager.getInstance();
        System.out.println("BufferPoolManager instance initialized successfully.\n");

        // 2. Factory pattern
        System.out.println("--- 2. FACTORY PATTERN ---");
        PageFactory pageFactory = new PageFactory();
        Page dataPage = pageFactory.createPage(PageType.DATA_PAGE);
        System.out.println("Created page via Factory: " + dataPage.getClass().getSimpleName() + "\n");

        // 3. Builder pattern
        System.out.println("--- 3. BUILDER PATTERN ---");
        Page customPage = new PageBuilder()
                .setPageType(PageType.DATA_PAGE)
                .buildHeader(101, 1)
                .buildSlots(8)
                .addRecord(new Record("Sample Record Data".getBytes()))
                .build();

        System.out.println("Built Slotted Page via PageBuilder successfully!");
        System.out.println(" -> Page Header PageID: " + customPage.getHeader().getPageId());
        System.out.println(" -> Slot Count: " + customPage.getHeader().getSlotCount());
        System.out.println(" -> Record Count: " + customPage.getRecords().size() + "\n");

        // 4. Facade pattern
        System.out.println("--- 4. FACADE PATTERN ---");
        StorageEngine storageEngine = new StorageEngine();
        Page allocated = storageEngine.allocatePage(PageType.DATA_PAGE);
        BufferFrame frame = storageEngine.fetchPage(101L);
        storageEngine.freePage(101L);
        storageEngine.flushAll();
        System.out.println();

        // 5. Adapter pattern
        System.out.println("--- 5. ADAPTER PATTERN ---");
        DiskFileManager diskFileManager1 = new DiskFileManager();
        diskFileManager1.setStorageAdapter(new LocalDiskAdapter());
        diskFileManager1.writePage(dataPage);
        diskFileManager1.flush();
        DiskFileManager diskFileManager2 = new DiskFileManager();
        diskFileManager2.setStorageAdapter(new CloudStorageAdapter());
        diskFileManager2.writePage(dataPage);
        diskFileManager2.flush();

        // 6. Composite pattern
        System.out.println("--- 6. COMPOSITE PATTERN (B+Tree Index Hierarchy) ---");
        // 1. Tạo các nút lá chứa dữ liệu (Leaf Nodes)
        LeafNode leaf1 = new LeafNode();
        leaf1.insert(10, "Record_User_10");
        leaf1.insert(20, "Record_User_20");
        LeafNode leaf2 = new LeafNode();
        leaf2.insert(30, "Record_User_30");
        leaf2.insert(40, "Record_User_40");
        // 2. Tạo nút trong điều hướng (Root InternalNode)
        InternalNode rootNode = new InternalNode();
        rootNode.addChild(25, leaf1); // Keys < 25 trỏ sang leaf1
        rootNode.addChild(50, leaf2); // Keys >= 25 trỏ sang leaf2
        // 3. Gọi hàm search() đồng nhất từ nút gốc BTreeNode
        System.out.println("Tìm kiếm Key = 20 qua cây B+Tree Composite:");
        Object val1 = rootNode.search(20);
        System.out.println("\nTìm kiếm Key = 40 qua cây B+Tree Composite:");
        Object val2 = rootNode.search(40);
        System.out.println();

        // 7. Strategy pattern (Page Replacement Strategy)
        System.out.println("--- 7. STRATEGY PATTERN (Page Replacement Strategy) ---");
        ReplacementContext replacementContext = new ReplacementContext();

        System.out.println("1. Sử dụng Chiến lược: LRU (Least Recently Used)");
        replacementContext.setStrategy(new LRUReplacementStrategy());
        replacementContext.evict();

        System.out.println("\n2. Thay đổi chiến lược sang: Clock Algorithm");
        replacementContext.setStrategy(new ClockReplacementStrategy());
        replacementContext.evict();

        System.out.println("\n3. Thay đổi chiến lược sang: FIFO (First In First Out)");
        replacementContext.setStrategy(new FIFOReplacementStrategy());
        replacementContext.evict();
        System.out.println();

        // 8. State pattern (BufferFrame Lifecycle & State Transitions)
        System.out.println("--- 8. STATE PATTERN (BufferFrame Lifecycle) ---");
        BufferFrame bufferFrame = new BufferFrame(dataPage);
        System.out.println("Khởi tạo BufferFrame -> Trạng thái ban đầu: " + bufferFrame.getState());

        System.out.println("\nThao tác 1: Truy vấn nạp trang (Pin page)");
        bufferFrame.pin();

        System.out.println("\nThao tác 2: Sửa đổi dữ liệu trên trang (Mark Dirty)");
        bufferFrame.markDirty();

        System.out.println("\nThao tác 3: Hoàn tất thao tác (Unpin page)");
        bufferFrame.unpin();
        System.out.println("Trạng thái cuối cùng của BufferFrame: " + bufferFrame.getState());
        System.out.println();

        // 9. Template Method pattern (Page I/O Execution Lifecycle)
        System.out.println("--- 9. TEMPLATE METHOD PATTERN (Page I/O Lifecycle) ---");
        Page sampleDataPage = new PageFactory().createPage(PageType.DATA_PAGE);
        byte[] dummyBytes = new byte[4096];
        // Gọi Template Method processPageIO(...) của Page
        sampleDataPage.processPageIO(dummyBytes);
        System.out.println();

        // 10. Iterator pattern (Page Records Iterator & BTree Leaf Iterator)
        System.out.println("--- 10. ITERATOR PATTERN (Page & B+Tree Scanners) ---");

        System.out.println("1. Duyệt các Record trên DataPage qua PageIterator:");
        Iterator<Record> pageIt = customPage.iterator();
        while (pageIt.hasNext()) {
            Record re = pageIt.next();
            System.out.println("  -> Found Record: " + new String(re.getData()));
        }

        System.out.println("\n2. Duyệt dữ liệu trên lá B+Tree qua BTreeIterator:");
        Iterator<Object> btreeIt = new BTreeIterator(leaf1);
        while (btreeIt.hasNext()) {
            Object item = btreeIt.next();
            System.out.println("  -> Found BTree Leaf Entry Value: " + item.toString());
        }
    }
}

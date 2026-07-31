package entity.storage_engine.abstracts;

import entity.storage_engine.page.PageHeader;
import entity.storage_engine.page.PageIterator;
import entity.storage_engine.page.RID;
import entity.storage_engine.page.Record;
import entity.storage_engine.page.SlotDirectory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Page {

    private PageHeader header;
    private SlotDirectory slotDirectory;
    private List<Record> records = new ArrayList<>();

    public PageHeader getHeader() {
        return header;
    }

    public void setHeader(PageHeader header) {
        this.header = header;
    }

    public SlotDirectory getSlotDirectory() {
        return slotDirectory;
    }

    public void setSlotDirectory(SlotDirectory slotDirectory) {
        this.slotDirectory = slotDirectory;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records != null ? records : new ArrayList<>();
    }

    /**
     * Template Method: Khung thuật toán cố định cho chu kỳ vòng đời I/O của 1 Trang dữ liệu.
     */
    public final void processPageIO(byte[] rawBytes) {
        System.out.println("  -> [Template Method] Bắt đầu quy trình xử lý I/O của Page:");
        read();
        deserialize(rawBytes);
        modify();
        byte[] outputBytes = serialize();
        write();
        System.out.println("  -> [Template Method] Hoàn tất quy trình I/O.");
    }

    public void read() {
        System.out.println("     1. [Read] Đọc mảng raw bytes từ đĩa/bộ đệm.");
    }

    public abstract void deserialize(byte[] bytes);

    public void modify() {
        System.out.println("     3. [Modify] Sửa đổi/Thêm bớt bản ghi trên trang dữ liệu trong RAM.");
    }

    public abstract byte[] serialize();

    public void write() {
        System.out.println("     5. [Write] Ghi mảng serialized bytes trở lại đĩa vật lý.");
    }

    public Iterator<Record> iterator() {
        return new PageIterator(records);
    }

    public boolean insertRecord(Record record) {
        if (record != null) {
            records.add(record);
            return true;
        }
        return false;
    }

    public boolean updateRecord(RID rid, Record record) {
        return false;
    }

    public boolean deleteRecord(RID rid) {
        return false;
    }
}

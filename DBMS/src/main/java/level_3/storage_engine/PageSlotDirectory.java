package level_3.storage_engine;

public class PageSlotDirectory {
    public int slotNumber;
    public int recordOffset;
    public int recordLength;

    public PageSlotDirectory() {
    }

    public PageSlotDirectory(int slotNumber, int recordOffset, int recordLength) {
        this.slotNumber = slotNumber;
        this.recordOffset = recordOffset;
        this.recordLength = recordLength;
    }
}

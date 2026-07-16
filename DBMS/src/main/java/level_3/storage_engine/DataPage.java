package storage_engine;

public class DataPage {
    private int pageID;
    private int tableID;
    private byte[] binaryBuffer;
    private SlottedPageFormatter formatter;

    public DataPage(int pageID, int tableID, byte[] binaryBuffer) {
        this.pageID = pageID;
        this.tableID = tableID;
        this.binaryBuffer = binaryBuffer;
        this.formatter = new SlottedPageFormatter(binaryBuffer);
    }

    public boolean insertRecord(byte[] recordBytes) {
        if (!formatter.hasSpaceFor(recordBytes.length)) {
            return false;
        }
        int offset = formatter.insertRecordAtEnd(recordBytes);
        if (offset == -1) {
            return false;
        }
        int slotNumber = formatter.readSlotCount();
        formatter.writeSlot(slotNumber, offset, recordBytes.length);
        return true;
    }

    public byte[] getRecord(int slotNumber) {
        int slotCount = formatter.readSlotCount();
        if (slotNumber < 0 || slotNumber >= slotCount) {
            return null;
        }
        int offset = formatter.getSlotOffset(slotNumber);
        int length = formatter.getSlotLength(slotNumber);
        if (offset == 0 && length == 0) {
            return null; // Slot is deleted or uninitialized
        }
        return formatter.readRecordFromOffset(offset, length);
    }

    public int getPageID() {
        return pageID;
    }

    public int getTableID() {
        return tableID;
    }

    public byte[] getBinaryBuffer() {
        return binaryBuffer;
    }

    public SlottedPageFormatter getFormatter() {
        return formatter;
    }
}

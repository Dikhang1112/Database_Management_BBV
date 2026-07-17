package level_2.storage_engine;

public class DataPage {
    private int pageID;
    private int tableID;
    private int slotCount;
    private byte[] binaryBuffer;

    public DataPage(int pageID, int tableID, int slotCount, byte[] binaryBuffer) {
        this.pageID = pageID;
        this.tableID = tableID;
        this.slotCount = slotCount;
        this.binaryBuffer = binaryBuffer;
    }

    public boolean insertRecord(byte[] recordBytes) {
        return false;
    }

    public byte[] getRecord(int slotNumber) {
        return null;
    }

    public int getPageID() {
        return pageID;
    }

    public int getTableID() {
        return tableID;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public byte[] getBinaryBuffer() {
        return binaryBuffer;
    }
}

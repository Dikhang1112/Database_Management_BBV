package level_3.storage_engine;

public class SlottedPageHeader {
    public int pageID;
    public int slotCount;
    public int freeSpacePointerOffset;
    public int freeSpaceEndOffset;

    public SlottedPageHeader() {
    }

    public SlottedPageHeader(int pageID, int slotCount, int freeSpacePointerOffset, int freeSpaceEndOffset) {
        this.pageID = pageID;
        this.slotCount = slotCount;
        this.freeSpacePointerOffset = freeSpacePointerOffset;
        this.freeSpaceEndOffset = freeSpaceEndOffset;
    }
}

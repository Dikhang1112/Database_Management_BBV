package storage_engine.page;

public class RID {

    private long pageId;
    private int slotNo;

    public RID(long pageId, int slotNo) {
        this.pageId = pageId;
        this.slotNo = slotNo;
    }

    public long getPageId() {
        return pageId;
    }

    public int getSlotNo() {
        return slotNo;
    }
}

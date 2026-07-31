package entity.storage_engine.page;

public class PageHeader {

    private int pageId;
    private int lsn;
    private int slotCount;

    public PageHeader() {
    }

    public PageHeader(int pageId, int lsn, int slotCount) {
        this.pageId = pageId;
        this.lsn = lsn;
        this.slotCount = slotCount;
    }

    public void initialize() {
        if (pageId <= 0) {
            pageId = 1;
        }
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getLsn() {
        return lsn;
    }

    public void setLsn(int lsn) {
        this.lsn = lsn;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        this.slotCount = slotCount;
    }
}

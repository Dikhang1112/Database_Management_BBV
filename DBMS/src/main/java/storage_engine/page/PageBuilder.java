package storage_engine.page;

import storage_engine.abstracts.Page;

import java.util.ArrayList;
import java.util.List;

public class PageBuilder {
    private PageHeader header;
    private SlotDirectory slotDirectory;
    private final List<Record> records = new ArrayList<>();
    private PageType pageType = PageType.DATA_PAGE;

    public PageBuilder setPageType(PageType pageType) {
        if (pageType != null) {
            this.pageType = pageType;
        }
        return this;
    }

    public PageBuilder buildHeader() {
        return buildHeader(1, 0);
    }

    public PageBuilder buildHeader(int pageId, int lsn) {
        this.header = new PageHeader(pageId, lsn, 0);
        this.header.initialize();
        return this;
    }

    public PageBuilder buildSlots() {
        return buildSlots(4);
    }

    public PageBuilder buildSlots(int initialSlots) {
        this.slotDirectory = new SlotDirectory();
        for (int i = 0; i < initialSlots; i++) {
            this.slotDirectory.allocateSlot();
        }
        if (this.header != null) {
            this.header.setSlotCount(initialSlots);
        }
        return this;
    }

    public PageBuilder buildRecords() {
        return this;
    }

    public PageBuilder addRecord(Record record) {
        if (record != null) {
            this.records.add(record);
        }
        return this;
    }

    public PageBuilder buildRecords(List<Record> records) {
        if (records != null) {
            this.records.addAll(records);
        }
        return this;
    }

    public Page build() {
        PageFactory factory = new PageFactory();
        Page page = factory.createPage(pageType);

        if (this.header == null) {
            buildHeader();
        }
        if (this.slotDirectory == null) {
            buildSlots();
        }

        page.setHeader(this.header);
        page.setSlotDirectory(this.slotDirectory);
        page.setRecords(new ArrayList<>(this.records));

        return page;
    }
}

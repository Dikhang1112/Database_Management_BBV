package storage_engine.page;

public class Record {

    private RID rid;
    private byte[] data;

    public RID getRid() {
        return rid;
    }

    public byte[] getData() {
        return data;
    }
}

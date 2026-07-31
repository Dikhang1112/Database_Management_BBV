package entity.storage_engine.page;

public class Record {

    private RID rid;
    private byte[] data;

    public Record() {
        this.data = new byte[0];
    }

    public Record(byte[] data) {
        this.data = data;
    }

    public Record(RID rid, byte[] data) {
        this.rid = rid;
        this.data = data;
    }

    public RID getRid() {
        return rid;
    }

    public void setRid(RID rid) {
        this.rid = rid;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}

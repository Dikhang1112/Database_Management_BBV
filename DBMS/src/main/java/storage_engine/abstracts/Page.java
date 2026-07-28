package storage_engine.abstracts;

import storage_engine.page.RID;
import storage_engine.page.Record;

public abstract class Page {

    public void read() {
    }

    public void deserialize(byte[] bytes) {
    }

    public boolean insertRecord(Record record) {
        return false;
    }

    public boolean updateRecord(RID rid, Record record) {
        return false;
    }

    public boolean deleteRecord(RID rid) {
        return false;
    }

    public byte[] serialize() {
        return new byte[0];
    }

    public void write() {
    }
}

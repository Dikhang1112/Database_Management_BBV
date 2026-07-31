package entity.storage_engine.page;

import entity.storage_engine.abstracts.Page;

public class IndexPage extends Page {

    @Override
    public void deserialize(byte[] bytes) {
        System.out.println("     2. [IndexPage Custom Deserialize] Parse raw bytes thành các Index Keys + Pointers.");
    }

    @Override
    public byte[] serialize() {
        System.out.println("     4. [IndexPage Custom Serialize] Nén Index Keys + Child Pointers thành byte array.");
        return new byte[4096];
    }
}

package storage_engine.page;

import storage_engine.abstracts.Page;

public class DataPage extends Page {

    @Override
    public void deserialize(byte[] bytes) {
        System.out.println("     2. [DataPage Custom Deserialize] Parse raw bytes thành Slotted Page Header + Slots Directory + Data Records.");
    }

    @Override
    public byte[] serialize() {
        System.out.println("     4. [DataPage Custom Serialize] Chuyển đổi toàn bộ Data Records + Slot Directory thành byte array 4096 bytes.");
        return new byte[4096];
    }
}

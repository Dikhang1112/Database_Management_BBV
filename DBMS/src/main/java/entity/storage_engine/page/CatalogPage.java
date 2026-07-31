package entity.storage_engine.page;

import entity.storage_engine.abstracts.Page;

public class CatalogPage extends Page {

    @Override
    public void deserialize(byte[] bytes) {
        System.out.println("     2. [CatalogPage Custom Deserialize] Parse raw bytes thành System Metadata Catalog entries.");
    }

    @Override
    public byte[] serialize() {
        System.out.println("     4. [CatalogPage Custom Serialize] Ghi thông tin Schema Metadata thành byte array.");
        return new byte[4096];
    }
}

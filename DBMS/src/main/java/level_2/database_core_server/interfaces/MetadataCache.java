package level_2.database_core_server.interfaces;

public interface MetadataCache {
    void put(int objectID, MetadataEntity obj);
    MetadataEntity get(int objectID);
    void invalidate(int objectID);
}

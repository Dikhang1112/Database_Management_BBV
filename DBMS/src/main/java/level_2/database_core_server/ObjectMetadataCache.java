package database_core_server;

import database_core_server.interfaces.EvictionStrategy;
import database_core_server.interfaces.MetadataCache;
import database_core_server.interfaces.MetadataEntity;
import java.util.Map;

public class ObjectMetadataCache implements MetadataCache {
    private Map<Integer, MetadataEntity> activeCacheMap;
    private Map<String, Integer> nameToIdLookupMap;
    private int currentCacheSize;
    private int maxCacheCapacity;
    private EvictionStrategy currentStrategy;

    public ObjectMetadataCache(Map<Integer, MetadataEntity> activeCacheMap, Map<String, Integer> nameToIdLookupMap, int currentCacheSize, int maxCacheCapacity, EvictionStrategy currentStrategy) {
        this.activeCacheMap = activeCacheMap;
        this.nameToIdLookupMap = nameToIdLookupMap;
        this.currentCacheSize = currentCacheSize;
        this.maxCacheCapacity = maxCacheCapacity;
        this.currentStrategy = currentStrategy;
    }

    @Override
    public void put(int objectID, MetadataEntity obj) {
    }

    @Override
    public MetadataEntity get(int objectID) {
        return null;
    }

    @Override
    public void invalidate(int objectID) {
    }

    public MetadataEntity getByObjectName(String objName) {
        return null;
    }
}

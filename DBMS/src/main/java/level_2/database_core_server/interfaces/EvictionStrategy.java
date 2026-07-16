package database_core_server.interfaces;

import java.util.Map;

public interface EvictionStrategy {
    int evict(Map<Integer, MetadataEntity> cacheMap);
}

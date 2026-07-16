package database_core_server;

import database_core_server.interfaces.EvictionStrategy;
import database_core_server.interfaces.MetadataEntity;
import java.util.Map;

public class LfuEviction implements EvictionStrategy {
    private Map<Integer, Integer> frequencyMap;

    public LfuEviction(Map<Integer, Integer> frequencyMap) {
        this.frequencyMap = frequencyMap;
    }

    @Override
    public int evict(Map<Integer, MetadataEntity> cacheMap) {
        return -1;
    }
}

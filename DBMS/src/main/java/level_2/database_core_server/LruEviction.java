package level_2.database_core_server;

import level_2.database_core_server.interfaces.EvictionStrategy;
import level_2.database_core_server.interfaces.MetadataEntity;
import java.util.List;
import java.util.Map;

public class LruEviction implements EvictionStrategy {
    private List<Integer> accessHistory;

    public LruEviction(List<Integer> accessHistory) {
        this.accessHistory = accessHistory;
    }

    @Override
    public int evict(Map<Integer, MetadataEntity> cacheMap) {
        return -1;
    }
}

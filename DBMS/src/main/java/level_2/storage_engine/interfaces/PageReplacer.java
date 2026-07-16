package storage_engine.interfaces;

import java.util.Map;
import storage_engine.DataPage;

public interface PageReplacer {
    int selectVictimPage(Map<Integer, DataPage> cacheMap);
}

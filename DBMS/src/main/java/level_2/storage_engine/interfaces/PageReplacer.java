package level_2.storage_engine.interfaces;

import java.util.Map;
import level_2.storage_engine.DataPage;

public interface PageReplacer {
    int selectVictimPage(Map<Integer, DataPage> cacheMap);
}

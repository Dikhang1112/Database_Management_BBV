package level_2.storage_engine;

import level_2.storage_engine.interfaces.PageReplacer;
import java.util.List;
import java.util.Map;

public class LruPageReplacer implements PageReplacer {
    private List<Integer> lruList;

    public LruPageReplacer(List<Integer> lruList) {
        this.lruList = lruList;
    }

    @Override
    public int selectVictimPage(Map<Integer, DataPage> cacheMap) {
        return -1;
    }
}

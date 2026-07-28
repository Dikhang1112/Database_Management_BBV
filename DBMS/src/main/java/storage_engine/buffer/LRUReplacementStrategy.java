package storage_engine.buffer;

import storage_engine.interfaces.PageReplacementStrategy;

public class LRUReplacementStrategy implements PageReplacementStrategy {

    @Override
    public int selectVictim() {
        return -1;
    }
}

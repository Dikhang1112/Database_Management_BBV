package storage_engine.buffer;

import storage_engine.interfaces.PageReplacementStrategy;

public class FIFOReplacementStrategy implements PageReplacementStrategy {

    @Override
    public int selectVictim() {
        return -1;
    }
}

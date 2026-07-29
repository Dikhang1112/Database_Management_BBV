package storage_engine.buffer;

import storage_engine.interfaces.PageReplacementStrategy;

public class ReplacementContext {

    public void setStrategy(PageReplacementStrategy strategy) {
    }

    public int evict() {
        return -1;
    }
}

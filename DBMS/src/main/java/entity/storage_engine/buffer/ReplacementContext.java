package entity.storage_engine.buffer;

import entity.storage_engine.interfaces.PageReplacementStrategy;

public class ReplacementContext {

    private PageReplacementStrategy strategy;

    public ReplacementContext() {
        this.strategy = new LRUReplacementStrategy();
    }

    public ReplacementContext(PageReplacementStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PageReplacementStrategy strategy) {
        this.strategy = strategy;
    }

    public PageReplacementStrategy getStrategy() {
        return strategy;
    }

    public int evict() {
        if (strategy != null) {
            return strategy.selectVictim();
        }
        return -1;
    }
}

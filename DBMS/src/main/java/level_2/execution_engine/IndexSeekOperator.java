package level_2.execution_engine;

import level_2.database_core_server.Row;
import level_2.execution_engine.interfaces.RowIterator;

public class IndexSeekOperator implements RowIterator {
    private int targetIndexID;
    private Object seekKey;

    public IndexSeekOperator(int targetIndexID, Object seekKey) {
        this.targetIndexID = targetIndexID;
        this.seekKey = seekKey;
    }

    @Override
    public void open() {
    }

    @Override
    public Row next() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public void close() {
    }
}

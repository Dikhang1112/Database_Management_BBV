package level_2.execution_engine;

import level_2.database_core_server.Row;
import level_2.execution_engine.interfaces.RowIterator;

public class TableScanOperator implements RowIterator {
    private int targetTableID;
    private int currentBlockAddress;

    public TableScanOperator(int targetTableID, int currentBlockAddress) {
        this.targetTableID = targetTableID;
        this.currentBlockAddress = currentBlockAddress;
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

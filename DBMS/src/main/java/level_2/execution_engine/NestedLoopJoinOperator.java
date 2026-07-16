package level_2.execution_engine;

import level_2.database_core_server.Row;
import level_2.execution_engine.interfaces.RowIterator;

public class NestedLoopJoinOperator implements RowIterator {
    private RowIterator outerStream;
    private RowIterator innerStream;
    private int outerJoinColumnID;
    private int innerJoinColumnID;

    public NestedLoopJoinOperator(RowIterator outerStream, RowIterator innerStream, int outerJoinColumnID, int innerJoinColumnID) {
        this.outerStream = outerStream;
        this.innerStream = innerStream;
        this.outerJoinColumnID = outerJoinColumnID;
        this.innerJoinColumnID = innerJoinColumnID;
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

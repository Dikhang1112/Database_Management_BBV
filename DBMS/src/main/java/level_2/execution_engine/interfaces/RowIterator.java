package level_2.execution_engine.interfaces;

import level_2.database_core_server.Row;

public interface RowIterator {
    void open();
    Row next();
    boolean hasNext();
    void close();
}

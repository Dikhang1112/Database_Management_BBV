package execution_engine.interfaces;

import database_core_server.Row;

public interface RowIterator {
    void open();
    Row next();
    boolean hasNext();
    void close();
}

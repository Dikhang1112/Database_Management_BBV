package level_2.execution_engine.interfaces;

import level_2.database_core_server.Row;
import level_2.database_core_server.interfaces.SessionContext;

public interface ResultStreamer {
    byte[] formatData(Row row);
    void streamToClient(byte[] serializedBytes, SessionContext session);
}

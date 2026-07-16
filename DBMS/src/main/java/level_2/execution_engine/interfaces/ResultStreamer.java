package execution_engine.interfaces;

import database_core_server.Row;
import database_core_server.interfaces.SessionContext;

public interface ResultStreamer {
    byte[] formatData(Row row);
    void streamToClient(byte[] serializedBytes, SessionContext session);
}

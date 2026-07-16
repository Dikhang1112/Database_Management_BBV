package level_2.execution_engine;

import level_2.database_core_server.Row;
import level_2.database_core_server.interfaces.SessionContext;
import level_2.execution_engine.interfaces.ResultStreamer;

public class TdsResultProcessor implements ResultStreamer {
    private String encodingCharset;
    private int maxFlushBufferSize;

    public TdsResultProcessor(String encodingCharset, int maxFlushBufferSize) {
        this.encodingCharset = encodingCharset;
        this.maxFlushBufferSize = maxFlushBufferSize;
    }

    @Override
    public byte[] formatData(Row row) {
        return null;
    }

    @Override
    public void streamToClient(byte[] serializedBytes, SessionContext session) {
    }
}

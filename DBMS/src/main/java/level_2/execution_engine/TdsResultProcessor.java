package execution_engine;

import database_core_server.Row;
import database_core_server.interfaces.SessionContext;
import execution_engine.interfaces.ResultStreamer;

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

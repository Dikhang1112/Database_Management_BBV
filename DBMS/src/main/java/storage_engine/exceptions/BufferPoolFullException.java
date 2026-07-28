package storage_engine.exceptions;

public class BufferPoolFullException extends StorageEngineException {

    public BufferPoolFullException(String message) {
        super(message);
    }
}

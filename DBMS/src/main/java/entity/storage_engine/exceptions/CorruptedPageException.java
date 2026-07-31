package entity.storage_engine.exceptions;

public class CorruptedPageException extends StorageEngineException {

    public CorruptedPageException(String message) {
        super(message);
    }
}

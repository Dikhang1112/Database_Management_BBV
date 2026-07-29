package storage_engine.exceptions;

public class PageNotFoundException extends StorageEngineException {

    public PageNotFoundException(String message) {
        super(message);
    }
}

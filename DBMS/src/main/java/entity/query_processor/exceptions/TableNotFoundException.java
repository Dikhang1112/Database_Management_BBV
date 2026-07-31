package entity.query_processor.exceptions;

public class TableNotFoundException extends SemanticException {
    public TableNotFoundException(String message) {
        super(message);
    }
}

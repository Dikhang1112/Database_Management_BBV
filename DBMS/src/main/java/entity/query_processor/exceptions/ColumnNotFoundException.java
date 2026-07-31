package entity.query_processor.exceptions;

public class ColumnNotFoundException extends SemanticException {
    public ColumnNotFoundException(String message) {
        super(message);
    }
}

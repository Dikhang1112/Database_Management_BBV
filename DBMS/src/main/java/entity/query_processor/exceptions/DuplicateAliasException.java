package entity.query_processor.exceptions;

public class DuplicateAliasException extends SemanticException {
    public DuplicateAliasException(String message) {
        super(message);
    }
}

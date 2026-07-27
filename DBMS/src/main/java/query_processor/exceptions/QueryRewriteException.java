package query_processor.exceptions;

public class QueryRewriteException extends QueryOptimizationException {
    public QueryRewriteException(String message) {
        super(message);
    }
}

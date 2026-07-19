package level_2.query_processor;

public class TokenStream {
    public boolean hasNext() {
        return false;
    }

    public Token consume() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Token lookAhead() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

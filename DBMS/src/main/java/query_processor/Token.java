package query_processor;

public class Token {
    private String tokenType;
    private String tokenValue;

    public Token() {
    }

    public Token(String tokenType, String tokenValue) {
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
    }

    public String getType() {
        return tokenType;
    }

    public String getValue() {
        return tokenValue;
    }
}

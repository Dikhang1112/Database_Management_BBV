package query_processor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(tokenType, token.tokenType) && Objects.equals(tokenValue, token.tokenValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenType, tokenValue);
    }

    @Override
    public String toString() {
        return "Token{" + "type='" + tokenType + '\'' + ", value='" + tokenValue + '\'' + '}';
    }
}

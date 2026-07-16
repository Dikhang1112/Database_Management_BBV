package level_2.query_processor;

public class Token {
    public enum TokenType {
        SELECT, FROM, WHERE, IDENTIFIER, NUMBER, STAR, COMMA, EQUALS, EOF
    }

    private TokenType type;

    public Token(TokenType type) {
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }
}

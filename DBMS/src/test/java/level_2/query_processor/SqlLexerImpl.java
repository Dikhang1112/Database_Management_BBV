package level_2.query_processor;

public class SqlLexerImpl extends SqlLexer {
    private String sqlText;
    private int currentPosition;

    public SqlLexerImpl(String sqlText) {
        super(sqlText);
        this.sqlText = sqlText;
        this.currentPosition = 0;
    }

    @Override
    public Token nextToken() {
        // Skip whitespace
        while (currentPosition < sqlText.length() && Character.isWhitespace(sqlText.charAt(currentPosition))) {
            currentPosition++;
        }

        if (currentPosition >= sqlText.length()) {
            return new Token(Token.TokenType.EOF);
        }

        char currentChar = sqlText.charAt(currentPosition);

        // Single character tokens
        if (currentChar == '*') {
            currentPosition++;
            return new Token(Token.TokenType.STAR);
        }
        if (currentChar == ',') {
            currentPosition++;
            return new Token(Token.TokenType.COMMA);
        }
        if (currentChar == '=') {
            currentPosition++;
            return new Token(Token.TokenType.EQUALS);
        }

        // Keywords or Identifiers
        if (Character.isLetter(currentChar) || currentChar == '_') {
            StringBuilder sb = new StringBuilder();
            while (currentPosition < sqlText.length() && 
                   (Character.isLetterOrDigit(sqlText.charAt(currentPosition)) || sqlText.charAt(currentPosition) == '_')) {
                sb.append(sqlText.charAt(currentPosition));
                currentPosition++;
            }
            String value = sb.toString();
            String upper = value.toUpperCase();
            if (upper.equals("SELECT")) {
                return new Token(Token.TokenType.SELECT);
            } else if (upper.equals("FROM")) {
                return new Token(Token.TokenType.FROM);
            } else if (upper.equals("WHERE")) {
                return new Token(Token.TokenType.WHERE);
            } else {
                return new Token(Token.TokenType.IDENTIFIER);
            }
        }

        // Numbers
        if (Character.isDigit(currentChar)) {
            StringBuilder sb = new StringBuilder();
            while (currentPosition < sqlText.length() && Character.isDigit(sqlText.charAt(currentPosition))) {
                sb.append(sqlText.charAt(currentPosition));
                currentPosition++;
            }
            return new Token(Token.TokenType.NUMBER);
        }

        // Unsupported characters (treated as single-character identifiers for robustness)
        currentPosition++;
        return new Token(Token.TokenType.IDENTIFIER);
    }
}

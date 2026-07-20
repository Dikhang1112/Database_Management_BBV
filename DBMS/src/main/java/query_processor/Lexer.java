package query_processor;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public TokenStream tokenize(String sqlText) {
        if (sqlText == null) {
            throw new IllegalArgumentException("SQL text cannot be null");
        }

        List<Token> tokens = new ArrayList<>();
        int i = 0;
        int length = sqlText.length();

        while (i < length) {
            char c = sqlText.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // Unterminated or string literal scanning
            if (c == '\'') {
                int start = i + 1;
                int end = sqlText.indexOf('\'', start);
                if (end == -1) {
                    throw new IllegalArgumentException("Unterminated string literal");
                }
                String literalValue = sqlText.substring(start, end);
                tokens.add(new Token("LITERAL", literalValue));
                i = end + 1;
                continue;
            }

            // Identifiers or Keywords
            if (Character.isLetter(c) || c == '_') {
                int start = i;
                while (i < length && (Character.isLetterOrDigit(sqlText.charAt(i)) || sqlText.charAt(i) == '_' || sqlText.charAt(i) == '.')) {
                    i++;
                }
                String value = sqlText.substring(start, i);
                String upper = value.toUpperCase();
                if (upper.equals("SELECT") || upper.equals("FROM") || upper.equals("WHERE") || upper.equals("AND") || upper.equals("OR")) {
                    tokens.add(new Token("KEYWORD", value));
                } else {
                    tokens.add(new Token("IDENTIFIER", value));
                }
                continue;
            }

            // Numbers
            if (Character.isDigit(c)) {
                int start = i;
                while (i < length && Character.isDigit(sqlText.charAt(i))) {
                    i++;
                }
                String value = sqlText.substring(start, i);
                tokens.add(new Token("LITERAL", value));
                continue;
            }

            // Multi-char & single-char operators / punctuation
            if (c == '<' || c == '>' || c == '=' || c == '!') {
                int start = i;
                i++;
                if (i < length && sqlText.charAt(i) == '=') {
                    i++;
                }
                String op = sqlText.substring(start, i);
                tokens.add(new Token("OPERATOR", op));
                continue;
            }

            if (c == ',' || c == '*' || c == '(' || c == ')') {
                tokens.add(new Token("PUNCTUATION", String.valueOf(c)));
                i++;
                continue;
            }

            // Invalid character
            throw new IllegalArgumentException("Unexpected character '" + c + "'");
        }

        tokens.add(new Token("EOF", ""));
        return new TokenStream(tokens);
    }
}

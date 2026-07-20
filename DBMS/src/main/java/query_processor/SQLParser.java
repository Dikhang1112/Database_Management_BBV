package query_processor;

import java.util.ArrayList;
import java.util.List;

public class SQLParser {

    public AST parse(TokenStream stream) {
        if (stream == null || !stream.hasNext()) {
            throw new IllegalArgumentException("Unexpected end of stream");
        }

        Token firstToken = stream.consume();
        if (firstToken == null || !"KEYWORD".equalsIgnoreCase(firstToken.getType()) || !"SELECT".equalsIgnoreCase(firstToken.getValue())) {
            throw new IllegalArgumentException("Expected 'SELECT' keyword");
        }

        // Projection fields
        List<String> fields = new ArrayList<>();
        while (stream.hasNext()) {
            Token token = stream.lookAhead(0);
            if (token != null && "KEYWORD".equalsIgnoreCase(token.getType()) && "FROM".equalsIgnoreCase(token.getValue())) {
                break;
            }
            token = stream.consume();
            if (token != null && "IDENTIFIER".equalsIgnoreCase(token.getType())) {
                fields.add(token.getValue());
            } else if (token != null && "PUNCTUATION".equals(token.getType()) && ",".equals(token.getValue())) {
                // comma separator
            } else if (token != null && "PUNCTUATION".equals(token.getType()) && "*".equals(token.getValue())) {
                fields.add("*");
            } else if (token != null && "KEYWORD".equalsIgnoreCase(token.getType()) && "WHERE".equalsIgnoreCase(token.getValue())) {
                throw new IllegalArgumentException("Unexpected token 'WHERE'");
            }
        }

        Token fromToken = stream.consume();
        if (fromToken == null || !"KEYWORD".equalsIgnoreCase(fromToken.getType()) || !"FROM".equalsIgnoreCase(fromToken.getValue())) {
            throw new IllegalArgumentException("Expected 'FROM' keyword");
        }

        Token tableToken = stream.consume();
        if (tableToken == null || !"IDENTIFIER".equalsIgnoreCase(tableToken.getType())) {
            throw new IllegalArgumentException("Expected table name identifier");
        }
        String tableName = tableToken.getValue();

        ASTNode whereNode = null;
        if (stream.hasNext()) {
            Token whereToken = stream.consume();
            if (whereToken != null && "KEYWORD".equalsIgnoreCase(whereToken.getType()) && "WHERE".equalsIgnoreCase(whereToken.getValue())) {
                Token leftToken = stream.consume();
                Token opToken = stream.consume();
                Token rightToken = stream.consume();
                if (leftToken != null && opToken != null && rightToken != null) {
                    IdentifierASTNode leftNode = new IdentifierASTNode(leftToken.getValue());
                    LiteralASTNode rightNode = new LiteralASTNode(rightToken.getValue(), "INT");
                    whereNode = new BinaryOpASTNode(opToken.getValue(), leftNode, rightNode);
                }
            }
        }

        SelectASTNode selectNode = new SelectASTNode(tableName, fields, whereNode);
        return new AST(selectNode);
    }
}

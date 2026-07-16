package level_2.query_processor;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SqlLexerTest {

    @Test
    public void testEmptyAndWhitespaceQuery() {
        SqlLexer lexer = new SqlLexerImpl("   \n \t ");
        Token token = lexer.nextToken();
        assertEquals(Token.TokenType.EOF, token.getType());
    }

    @Test
    public void testBasicSelectQuery() {
        SqlLexer lexer = new SqlLexerImpl("SELECT * FROM users");
        
        Token token = lexer.nextToken();
        assertEquals(Token.TokenType.SELECT, token.getType());

        token = lexer.nextToken();
        assertEquals(Token.TokenType.STAR, token.getType());

        token = lexer.nextToken();
        assertEquals(Token.TokenType.FROM, token.getType());

        token = lexer.nextToken();
        assertEquals(Token.TokenType.IDENTIFIER, token.getType());

        token = lexer.nextToken();
        assertEquals(Token.TokenType.EOF, token.getType());
    }

    @Test
    public void testComplexQueryWithWhereClause() {
        SqlLexer lexer = new SqlLexerImpl("SELECT name, age FROM users WHERE id = 10");

        // SELECT
        Token token = lexer.nextToken();
        assertEquals(Token.TokenType.SELECT, token.getType());

        // name
        token = lexer.nextToken();
        assertEquals(Token.TokenType.IDENTIFIER, token.getType());

        // ,
        token = lexer.nextToken();
        assertEquals(Token.TokenType.COMMA, token.getType());

        // age
        token = lexer.nextToken();
        assertEquals(Token.TokenType.IDENTIFIER, token.getType());

        // FROM
        token = lexer.nextToken();
        assertEquals(Token.TokenType.FROM, token.getType());

        // users
        token = lexer.nextToken();
        assertEquals(Token.TokenType.IDENTIFIER, token.getType());

        // WHERE
        token = lexer.nextToken();
        assertEquals(Token.TokenType.WHERE, token.getType());

        // id
        token = lexer.nextToken();
        assertEquals(Token.TokenType.IDENTIFIER, token.getType());

        // =
        token = lexer.nextToken();
        assertEquals(Token.TokenType.EQUALS, token.getType());

        // 10
        token = lexer.nextToken();
        assertEquals(Token.TokenType.NUMBER, token.getType());

        // EOF
        token = lexer.nextToken();
        assertEquals(Token.TokenType.EOF, token.getType());
    }

    @Test
    public void testCaseInsensitivityOfKeywords() {
        SqlLexer lexer = new SqlLexerImpl("select * from users");

        Token token = lexer.nextToken();
        assertEquals(Token.TokenType.SELECT, token.getType());

        token = lexer.nextToken();
        assertEquals(Token.TokenType.STAR, token.getType());

        token = lexer.nextToken();
        assertEquals(Token.TokenType.FROM, token.getType());

        token = lexer.nextToken();
        assertEquals(Token.TokenType.IDENTIFIER, token.getType());

        token = lexer.nextToken();
        assertEquals(Token.TokenType.EOF, token.getType());
    }
}

package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SQLParserTest {

    private final SQLParser parser = new SQLParser();

    // Kiểm thử phân tích cú pháp câu lệnh SELECT có điều kiện WHERE chuẩn
    @Test
    @DisplayName("TC-04. Parse Select Query")
    void parse_ShouldGenerateParseTree_WhenValidSelectQuery() {
        TokenStream stream = new TokenStream(Arrays.asList(
                new Token("KEYWORD", "SELECT"),
                new Token("IDENTIFIER", "name"),
                new Token("KEYWORD", "FROM"),
                new Token("IDENTIFIER", "users"),
                new Token("KEYWORD", "WHERE"),
                new Token("IDENTIFIER", "age"),
                new Token("OPERATOR", ">"),
                new Token("LITERAL", "18")
        ));

        AST ast = parser.parse(stream);

        assertThat(ast).isNotNull();
        assertThat(ast.getRootASTNode()).isInstanceOf(SelectASTNode.class);

        //Dung cay SelectASTNode
        SelectASTNode selectNode = (SelectASTNode) ast.getRootASTNode();
        assertThat(selectNode.getTableName()).isEqualTo("users");
        assertThat(selectNode.getProjectionFields()).contains("name");
        assertThat(selectNode.getWhereCondition()).isNotNull();
    }

    // Kiểm thử phân tích cú pháp câu lệnh SELECT khi không có mệnh đề WHERE
    @Test
    @DisplayName("TC-04A. Parse Query Without WHERE Clause")
    void parse_ShouldParseQueryWithoutWhereClause_WhenWhereIsOmitted() {
        TokenStream stream = new TokenStream(Arrays.asList(
                new Token("KEYWORD", "SELECT"),
                new Token("IDENTIFIER", "name"),
                new Token("KEYWORD", "FROM"),
                new Token("IDENTIFIER", "users")
        ));

        AST ast = parser.parse(stream);

        assertThat(ast).isNotNull();
        SelectASTNode selectNode = (SelectASTNode) ast.getRootASTNode();
        assertThat(selectNode.getWhereCondition()).isNull();
    }

    // Kiểm thử tung ngoại lệ lỗi cú pháp khi thiếu từ khóa FROM
    @Test
    @DisplayName("TC-04B. Syntax Error - Missing FROM Keyword")
    void parse_ShouldThrowException_WhenSyntaxIsInvalid() {
        TokenStream stream = new TokenStream(Arrays.asList(
                new Token("KEYWORD", "SELECT"),
                new Token("IDENTIFIER", "name"),
                new Token("IDENTIFIER", "users")
        ));

        assertThatThrownBy(() -> parser.parse(stream))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected 'FROM' keyword");
    }

    // Kiểm thử tung ngoại lệ khi gặp Token không mong muốn
    @Test
    @DisplayName("TC-04C. Syntax Error - Unexpected Token")
    void parse_ShouldThrowException_WhenUnexpectedTokenEncountered() {
        TokenStream stream = new TokenStream(Arrays.asList(
                new Token("KEYWORD", "SELECT"),
                new Token("KEYWORD", "WHERE"),
                new Token("KEYWORD", "FROM"),
                new Token("IDENTIFIER", "users")
        ));

        assertThatThrownBy(() -> parser.parse(stream))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unexpected token 'WHERE'");
    }

    // Kiểm thử tung ngoại lệ khi luồng Token bị rỗng
    @Test
    @DisplayName("TC-04D. Syntax Error - Empty Token Stream")
    void parse_ShouldThrowException_WhenStreamIsEmpty() {
        TokenStream stream = new TokenStream(Collections.emptyList());

        assertThatThrownBy(() -> parser.parse(stream))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unexpected end of stream");
    }
}

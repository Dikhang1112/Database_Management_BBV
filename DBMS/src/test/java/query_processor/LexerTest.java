package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LexerTest {

    private final Lexer lexer = new Lexer();

    // Kiểm thử tách chuỗi SQL hợp lệ thành luồng Token hợp lệ
    @Test
    @DisplayName("TC-01. Tokenize Valid SQL")
    void tokenize_ShouldReturnTokenStream_WhenValidSqlIsProvided() {
        TokenStream stream = lexer.tokenize("SELECT id FROM users");

        assertThat(stream).isNotNull();
        assertThat(stream.consume()).isEqualTo(new Token("KEYWORD", "SELECT"));
        assertThat(stream.consume()).isEqualTo(new Token("IDENTIFIER", "id"));
        assertThat(stream.consume()).isEqualTo(new Token("KEYWORD", "FROM"));
        assertThat(stream.consume()).isEqualTo(new Token("IDENTIFIER", "users"));
    }

    // Kiểm thử nhận diện từ khóa SQL không phân biệt hoa/thường
    @Test
    @DisplayName("TC-01A. Tokenize Case-Insensitive Keywords")
    void tokenize_ShouldHandleKeywordsCaseInsensitively_WhenMixedCase() {
        TokenStream stream = lexer.tokenize("sElEcT name FrOm users");

        assertThat(stream.consume().getType()).isEqualTo("KEYWORD");
        assertThat(stream.consume().getType()).isEqualTo("IDENTIFIER");
        assertThat(stream.consume().getType()).isEqualTo("KEYWORD");
    }

    // Kiểm thử nhận diện hằng số và chuỗi ký tự (Literals)
    @Test
    @DisplayName("TC-01B. Tokenize Numeric and String Literals")
    void tokenize_ShouldScanNumericAndStringLiterals_WhenPresent() {
        TokenStream stream = lexer.tokenize("SELECT * FROM users WHERE age = 25 AND name = 'Alice'");

        assertThat(stream.lookAhead(0)).isEqualTo(new Token("KEYWORD", "SELECT"));
        
        boolean hasNumericLiteral = false;
        boolean hasStringLiteral = false;
        while (stream.hasNext()) {
            Token t = stream.consume();
            if ("25".equals(t.getValue())) hasNumericLiteral = true;
            if ("Alice".equals(t.getValue())) hasStringLiteral = true;
        }

        assertThat(hasNumericLiteral).isTrue();
        assertThat(hasStringLiteral).isTrue();
    }

    // Kiểm thử nhận diện toán tử so sánh và dấu phân cách
    @Test
    @DisplayName("TC-01C. Tokenize Operators and Punctuation")
    void tokenize_ShouldScanOperatorsAndPunctuation_WhenValid() {
        TokenStream stream = lexer.tokenize("SELECT id, age FROM users WHERE age <= 30");

        boolean hasComma = false;
        boolean hasLteOperator = false;
        while (stream.hasNext()) {
            Token t = stream.consume();
            if (",".equals(t.getValue()) && "PUNCTUATION".equals(t.getType())) hasComma = true;
            if ("<=".equals(t.getValue()) && "OPERATOR".equals(t.getType())) hasLteOperator = true;
        }

        assertThat(hasComma).isTrue();
        assertThat(hasLteOperator).isTrue();
    }

    // Kiểm thử tung ngoại lệ khi gặp chuỗi ký tự chưa đóng ngoặc kép
    @Test
    @DisplayName("TC-01D. Tokenize - Unterminated String Literal")
    void tokenize_ShouldThrowException_WhenUnterminatedStringLiteral() {
        assertThatThrownBy(() -> lexer.tokenize("SELECT * FROM users WHERE name = 'Alice"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unterminated string literal");
    }

    // Kiểm thử tung ngoại lệ khi gặp ký tự không hợp lệ
    @Test
    @DisplayName("TC-01E. Tokenize - Invalid Character Error")
    void tokenize_ShouldThrowException_WhenInvalidCharacterEncountered() {
        assertThatThrownBy(() -> lexer.tokenize("SELECT @ FROM users"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unexpected character '@'");
    }
}

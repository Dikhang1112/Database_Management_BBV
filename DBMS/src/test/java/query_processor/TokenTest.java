package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenTest {

    // Kiểm thử khởi tạo đối tượng Token với kiểu và giá trị chuẩn
    @Test
    @DisplayName("TC-03. Create Token")
    void createToken_ShouldInitializeTypeAndValue_WhenValidArgsProvided() {
        Token token = new Token("KEYWORD", "SELECT");

        assertThat(token.getType()).isEqualTo("KEYWORD");
        assertThat(token.getValue()).isEqualTo("SELECT");
    }

    // Kiểm thử so sánh bằng nhau giữa hai đối tượng Token
    @Test
    @DisplayName("TC-03A. Token Equality Comparison")
    void equals_ShouldCompareTokenTypeAndValue_WhenObjectsChecked() {
        Token token1 = new Token("KEYWORD", "SELECT");
        Token token2 = new Token("KEYWORD", "SELECT");
        Token token3 = new Token("IDENTIFIER", "SELECT");

        assertThat(token1).isEqualTo(token2);
        assertThat(token1).isNotEqualTo(token3);
    }

    // Kiểm thử khởi tạo Token bằng constructor mặc định
    @Test
    @DisplayName("TC-03B. Default Constructor Initialization")
    void defaultConstructor_ShouldInitializeWithNulls() {
        Token token = new Token();

        assertThat(token.getType()).isNull();
        assertThat(token.getValue()).isNull();
    }
}

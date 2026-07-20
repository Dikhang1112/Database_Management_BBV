package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class TokenStreamTest {

    // Kiểm thử lấy lần lượt các Token và tăng con trỏ chỉ số
    @Test
    @DisplayName("TC-02. Consume Sequential Tokens")
    void consume_ShouldReturnNextTokenAndAdvance_WhenHasNext() {
        Token t1 = new Token("KEYWORD", "SELECT");
        Token t2 = new Token("IDENTIFIER", "id");
        TokenStream stream = new TokenStream(Arrays.asList(t1, t2));

        assertThat(stream.consume()).isEqualTo(t1);
        assertThat(stream.consume()).isEqualTo(t2);
    }

    // Kiểm thử đọc trước Token tại vị trí offset mà không tăng con trỏ
    @Test
    @DisplayName("TC-02A. LookAhead Offset Without Advancing")
    void lookAhead_ShouldReturnTokenAtOffset_WithoutAdvancingPointer() {
        Token t1 = new Token("KEYWORD", "SELECT");
        Token t2 = new Token("IDENTIFIER", "id");
        TokenStream stream = new TokenStream(Arrays.asList(t1, t2));

        assertThat(stream.lookAhead(1)).isEqualTo(t2);
        assertThat(stream.hasNext()).isTrue();
        assertThat(stream.consume()).isEqualTo(t1);
    }

    // Kiểm thử xử lý biên khi luồng Token rỗng
    @Test
    @DisplayName("TC-02B. Consume - Empty Stream Boundary")
    void consume_ShouldReturnNullOrThrow_WhenStreamIsEmpty() {
        TokenStream stream = new TokenStream(Collections.emptyList());

        assertThat(stream.hasNext()).isFalse();
        assertThat(stream.consume()).isNull();
    }

    // Kiểm thử đọc trước ngoài phạm vi chỉ số danh sách
    @Test
    @DisplayName("TC-02C. LookAhead Out of Bounds")
    void lookAhead_ShouldReturnNull_WhenOffsetExceedsBounds() {
        Token t1 = new Token("KEYWORD", "SELECT");
        TokenStream stream = new TokenStream(Collections.singletonList(t1));

        assertThat(stream.lookAhead(5)).isNull();
        assertThat(stream.lookAhead(-1)).isNull();
    }

    // Kiểm thử lấy Token khi đã chạm mốc kết thúc luồng (EOF)
    @Test
    @DisplayName("TC-02D. Consume Past EOF")
    void consume_ShouldReturnNull_WhenConsumingPastEOF() {
        Token t1 = new Token("EOF", "");
        TokenStream stream = new TokenStream(Collections.singletonList(t1));

        assertThat(stream.consume()).isEqualTo(t1);
        assertThat(stream.consume()).isNull();
    }
}

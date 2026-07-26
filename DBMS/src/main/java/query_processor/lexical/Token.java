package query_processor.lexical;

/**
 * Đóng gói thông tin của một từ vựng (Token) thu được từ giai đoạn phân tích Lexer.
 */
public class Token {

    private final String type;
    private final String value;

    /**
     * Khởi tạo đối tượng Token.
     *
     * @param type  Loại token.
     * @param value Giá trị chuỗi của token.
     */
    public Token(String type, String value) {
        this.type = type;
        this.value = value;
        // TODO: Future DBMS logic implementation
    }

    /**
     * Lấy loại của Token.
     *
     * @return Chuỗi loại Token.
     */
    public String getType() {
        // TODO: Future DBMS logic implementation
        return type;
    }

    /**
     * Lấy giá trị của Token.
     *
     * @return Chuỗi giá trị Token.
     */
    public String getValue() {
        // TODO: Future DBMS logic implementation
        return value;
    }
}

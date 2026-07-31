package entity.query_processor.lexical;

import entity.query_processor.interfaces.CompilerStage;

/**
 * Giai đoạn phân tích từ vựng (Lexical Analysis) biến chuỗi SQL thành TokenStream.
 * Pattern: Chain of Responsibility.
 */
public class Lexer implements CompilerStage {

    /**
     * Khởi tạo Lexer.
     */
    public Lexer() {
        // TODO: Future DBMS logic implementation
    }

    /**
     * Thực thi giai đoạn Lexer theo giao diện CompilerStage.
     *
     * @param input Chuỗi sqlText đầu vào.
     * @return TokenStream thu được.
     */
    @Override
    public Object process(Object input) {
        if (input instanceof String sqlText) {
            return process(sqlText);
        }
        // TODO: Future DBMS logic implementation
        return null;
    }

    /**
     * Chuyển đổi văn bản SQL thành TokenStream.
     *
     * @param sqlText Văn bản câu lệnh SQL.
     * @return Luồng TokenStream.
     */
    public TokenStream process(String sqlText) {
        // TODO: Future DBMS logic implementation
        return null;
    }
}

package entity.query_processor.parser;

import entity.query_processor.interfaces.CompilerStage;
import entity.query_processor.lexical.TokenStream;

/**
 * Giai đoạn phân tích cú pháp SQL (Syntax Parsing) biến TokenStream thành ParseTree.
 * Pattern: Chain of Responsibility.
 */
public class SQLParser implements CompilerStage {

    /**
     * Khởi tạo SQLParser.
     */
    public SQLParser() {
        // TODO: Future DBMS logic implementation
    }

    /**
     * Thực thi giai đoạn Parser theo giao diện CompilerStage.
     *
     * @param input TokenStream đầu vào.
     * @return ParseTree kết quả.
     */
    @Override
    public Object process(Object input) {
        if (input instanceof TokenStream stream) {
            return process(stream);
        }
        // TODO: Future DBMS logic implementation
        return null;
    }

    /**
     * Phân tích luồng TokenStream thành ParseTree.
     *
     * @param stream Luồng TokenStream.
     * @return Cây cú pháp ParseTree.
     */
    public ParseTree process(TokenStream stream) {
        // TODO: Future DBMS logic implementation
        return null;
    }
}

package query_processor.parser;

import query_processor.interfaces.CompilerStage;
import query_processor.ast.AST;

/**
 * Giai đoạn chuyển đổi từ Cây cú pháp ParseTree sang Cây Cú Pháp Trừu Tượng AST.
 * Pattern: Chain of Responsibility.
 */
public class ASTBuilder implements CompilerStage {

    /**
     * Khởi tạo ASTBuilder.
     */
    public ASTBuilder() {
        // TODO: Future DBMS logic implementation
    }

    /**
     * Thực thi giai đoạn ASTBuilder theo giao diện CompilerStage.
     *
     * @param input Cây cú pháp ParseTree.
     * @return Cây AST được xây dựng.
     */
    @Override
    public Object process(Object input) {
        if (input instanceof ParseTree tree) {
            return process(tree);
        }
        // TODO: Future DBMS logic implementation
        return null;
    }

    /**
     * Chuyển đổi ParseTree thành Cây AST.
     *
     * @param tree Cây cú pháp ParseTree.
     * @return Đối tượng AST.
     */
    public AST process(ParseTree tree) {
        // TODO: Future DBMS logic implementation
        return null;
    }
}

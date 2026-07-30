package query_processor.interfaces;

import query_processor.abstracts.ASTNode;

/**
 * Interface Visitor cho việc duyệt các nút trong cây AST.
 * Áp dụng Visitor Pattern để tách bạch thuật toán duyệt/xử lý khỏi cấu trúc nút AST.
 */
public interface ASTVisitor {
    /**
     * Thăm và xử lý một nút trong cây AST.
     *
     * @param node Nút AST cần thăm.
     */
    void visit(ASTNode node);
}

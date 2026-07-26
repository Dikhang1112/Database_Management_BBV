package query_processor.abstracts;

import query_processor.interfaces.ASTVisitor;

/**
 * Lớp trừu tượng cơ sở đại diện cho một nút (Node) trong Cây Cú Pháp Trừu Tượng (AST).
 * Áp dụng Pattern Composite kết hợp Visitor Pattern.
 */
public abstract class ASTNode {

    /**
     * Chấp nhận một Visitor để thực hiện thao tác duyệt/thẩm định nút.
     *
     * @param visitor Đối tượng Visitor thực hiện thăm nút.
     */
    public abstract void accept(ASTVisitor visitor);
}

package entity.query_processor.ast;

import entity.query_processor.abstracts.ASTNode;

/**
 * Đóng gói toàn bộ cấu trúc Cây Cú Pháp Trừu Tượng (Abstract Syntax Tree - AST).
 * Quản lý nút gốc root kiểu ASTNode.
 */
public class AST {

    private ASTNode root;

    /**
     * Khởi tạo cây AST rỗng.
     */
    public AST() {
        // TODO: Future DBMS logic implementation
    }

    /**
     * Khởi tạo cây AST với nút gốc chỉ định.
     *
     * @param root Nút gốc của cây AST.
     */
    public AST(ASTNode root) {
        this.root = root;
        // TODO: Future DBMS logic implementation
    }

    /**
     * Lấy nút gốc của cây AST.
     *
     * @return Nút gốc ASTNode.
     */
    public ASTNode getRoot() {
        // TODO: Future DBMS logic implementation
        return root;
    }

    /**
     * Thiết lập nút gốc mới cho cây AST.
     *
     * @param root Nút gốc ASTNode cần gán.
     */
    public void setRoot(ASTNode root) {
        this.root = root;
        // TODO: Future DBMS logic implementation
    }
}

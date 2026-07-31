package entity.query_processor.ast;

import entity.query_processor.abstracts.ASTNode;
import entity.query_processor.interfaces.ASTVisitor;

/**
 * Nút Composite đại diện cho câu lệnh SELECT trong cây AST.
 * Chứa các nút con như TableASTNode, WhereASTNode.
 */
public class SelectASTNode extends ASTNode {

    private final ASTNode tableNode;
    private final ASTNode whereNode;

    public SelectASTNode(ASTNode tableNode, ASTNode whereNode) {
        this.tableNode = tableNode;
        this.whereNode = whereNode;
    }

    public ASTNode getTableNode() {
        return tableNode;
    }

    public ASTNode getWhereNode() {
        return whereNode;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        if (visitor != null) {
            visitor.visit(this);
        }
        if (tableNode != null) {
            tableNode.accept(visitor);
        }
        if (whereNode != null) {
            whereNode.accept(visitor);
        }
    }
}

package entity.query_processor.ast;

import entity.query_processor.abstracts.ASTNode;
import entity.query_processor.interfaces.ASTVisitor;

/**
 * Nút lá đại diện cho định danh Bảng (Table) trong cây AST.
 */
public class TableASTNode extends ASTNode {

    private final String tableName;

    public TableASTNode(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        if (visitor != null) {
            visitor.visit(this);
        }
    }
}

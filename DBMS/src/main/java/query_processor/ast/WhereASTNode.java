package query_processor.ast;

import query_processor.abstracts.ASTNode;
import query_processor.interfaces.ASTVisitor;

/**
 * Nút lá đại diện cho Mệnh đề điều kiện (WHERE) trong cây AST.
 */
public class WhereASTNode extends ASTNode {

    private final String condition;

    public WhereASTNode(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        if (visitor != null) {
            visitor.visit(this);
        }
    }
}

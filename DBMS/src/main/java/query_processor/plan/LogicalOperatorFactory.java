package query_processor.plan;

import query_processor.abstracts.ASTNode;

/**
 * Nhà máy (Factory Method) khởi tạo các toán tử đại số logic từ nút AST.
 * Pattern: Factory Method Pattern.
 */
public class LogicalOperatorFactory {

    public LogicalOperatorFactory() {
    }

    /**
     * Khởi tạo đối tượng toán tử logic từ nút ASTNode.
     *
     * @param node Nút ASTNode đầu vào.
     * @return Nút toán tử logic (LogicalPlanNode).
     */
    public Object createOperator(ASTNode node) {
        if (node == null) {
            throw new IllegalArgumentException("ASTNode cannot be null");
        }
        return new LogicalPlanNode("LOGICAL_OPERATOR");
    }
}

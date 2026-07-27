package query_processor.plan;

import query_processor.abstracts.ASTNode;
import query_processor.ast.AST;

/**
 * Xây dựng từng bước cây Kế hoạch Thực thi Logic (LogicalPlan) từ AST.
 * Pattern: Builder Pattern.
 */
public class LogicalPlanBuilder {

    private final LogicalOperatorFactory logicalOperatorFactory;

    public LogicalPlanBuilder() {
        this(new LogicalOperatorFactory());
    }

    public LogicalPlanBuilder(LogicalOperatorFactory logicalOperatorFactory) {
        this.logicalOperatorFactory = logicalOperatorFactory != null ? logicalOperatorFactory : new LogicalOperatorFactory();
    }

    /**
     * Build đối tượng LogicalPlan từ cây AST.
     *
     * @param ast Cây AST đầu vào.
     * @return Đối tượng LogicalPlan hoàn chỉnh.
     */
    public LogicalPlan build(AST ast) {
        if (ast == null) {
            throw new IllegalArgumentException("AST cannot be null");
        }
        ASTNode root = ast.getRoot();
        if (root == null) {
            return new LogicalPlan();
        }
        Object operator = logicalOperatorFactory.createOperator(root);
        LogicalPlanNode rootNode = operator instanceof LogicalPlanNode ? (LogicalPlanNode) operator : new LogicalPlanNode("SCAN");
        return new LogicalPlan(rootNode);
    }
}

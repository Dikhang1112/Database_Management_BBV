package query_processor;

public class QueryOptimizer {
    public LogicalPlan generateLogicalPlan(AST ast) {
        if (ast == null || ast.getRootASTNode() == null) {
            throw new IllegalArgumentException("AST root node cannot be null");
        }
        return new LogicalPlan();
    }

    public PhysicalPlan optimize(LogicalPlan plan) {
        if (plan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        return new PhysicalPlan(10.5);
    }
}

package query_processor.optimizer;

import query_processor.planner.LogicalPlan;

public class JoinOptimizer {

    public LogicalPlan optimize(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        LogicalPlan reordered = optimizeJoinOrder(logicalPlan);
        return selectJoinMethod(reordered);
    }

    public LogicalPlan optimizeJoinOrder(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        return logicalPlan;
    }

    public LogicalPlan selectJoinMethod(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        return logicalPlan;
    }
}

package query_processor.optimizer;

import query_processor.plan.LogicalPlan;

public class QueryRewriter {

    public LogicalPlan rewrite(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        LogicalPlan p1 = predicatePushdown(logicalPlan);
        LogicalPlan p2 = projectionPushdown(p1);
        return constantFolding(p2);
    }

    public LogicalPlan predicatePushdown(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        return logicalPlan;
    }

    public LogicalPlan projectionPushdown(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        return logicalPlan;
    }

    public LogicalPlan constantFolding(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        return logicalPlan;
    }
}

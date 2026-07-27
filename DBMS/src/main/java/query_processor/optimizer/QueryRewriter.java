package query_processor.optimizer;

import query_processor.planner.LogicalPlan;

public abstract class QueryRewriter {

    public abstract LogicalPlan rewrite(LogicalPlan logicalPlan);

    public abstract LogicalPlan predicatePushdown(LogicalPlan logicalPlan);

    public abstract LogicalPlan projectionPushdown(LogicalPlan logicalPlan);

    public abstract LogicalPlan constantFolding(LogicalPlan logicalPlan);
}

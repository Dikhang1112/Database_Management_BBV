package query_processor.optimizer;

import query_processor.planner.LogicalPlan;

public abstract class JoinOptimizer {

    public abstract LogicalPlan optimize(LogicalPlan logicalPlan);

    public abstract LogicalPlan optimizeJoinOrder(LogicalPlan logicalPlan);

    public abstract LogicalPlan selectJoinMethod(LogicalPlan logicalPlan);
}

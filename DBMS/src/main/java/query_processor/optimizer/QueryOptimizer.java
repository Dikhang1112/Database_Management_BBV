package query_processor.optimizer;

import query_processor.planner.LogicalPlan;
import query_processor.planner.PhysicalPlan;
import query_processor.interfaces.OptimizationRule;

public abstract class QueryOptimizer {

    public abstract PhysicalPlan optimize(LogicalPlan logicalPlan);

    public abstract void setOptimizationRule(OptimizationRule rule);
}

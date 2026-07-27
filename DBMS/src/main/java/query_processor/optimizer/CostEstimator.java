package query_processor.optimizer;

import query_processor.planner.LogicalPlan;

public abstract class CostEstimator {

    public abstract double estimate(LogicalPlan logicalPlan);

    public abstract double estimateCardinality(LogicalPlan logicalPlan);

    public abstract double estimateSelectivity(LogicalPlan logicalPlan);
}

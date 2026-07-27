package query_processor.optimizer;

import query_processor.planner.LogicalPlan;
import query_processor.planner.PhysicalPlan;

public abstract class PlanEnumerator {

    public abstract PhysicalPlan enumerate(LogicalPlan logicalPlan);

    public abstract PhysicalPlan selectAccessPath(LogicalPlan logicalPlan);
}

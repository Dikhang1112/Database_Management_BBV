package entity.query_processor.optimizer;

import entity.query_processor.plan.LogicalPlan;
import entity.query_processor.plan.PhysicalPlan;

public class PlanEnumerator {

    public PhysicalPlan enumerate(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        return selectAccessPath(logicalPlan);
    }

    public PhysicalPlan selectAccessPath(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        return new PhysicalPlan();
    }
}

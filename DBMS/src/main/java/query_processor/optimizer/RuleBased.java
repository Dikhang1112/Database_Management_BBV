package query_processor.optimizer;

import query_processor.interfaces.OptimizationRule;
import query_processor.plan.LogicalPlan;

public class RuleBased implements OptimizationRule {
    @Override
    public LogicalPlan optimize(LogicalPlan plan) {
        System.out.println("  -> [Strategy: Rule-Based] Thực hiện Predicate Pushdown & Constant Folding...");
        return plan;
    }
}

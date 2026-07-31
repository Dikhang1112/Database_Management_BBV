package entity.query_processor.optimizer;

import entity.query_processor.interfaces.OptimizationRule;
import entity.query_processor.plan.LogicalPlan;

public class RuleBased implements OptimizationRule {
    @Override
    public LogicalPlan optimize(LogicalPlan plan) {
        System.out.println("  -> [Strategy: Rule-Based] Thực hiện Predicate Pushdown & Constant Folding...");
        return plan;
    }
}

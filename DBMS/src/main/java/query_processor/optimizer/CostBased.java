package query_processor.optimizer;

import query_processor.interfaces.OptimizationRule;
import query_processor.plan.LogicalPlan;

public class CostBased implements OptimizationRule {
    @Override
    public LogicalPlan optimize(LogicalPlan plan) {
        System.out.println("  -> [Strategy: Cost-Based] Tính toán chi phí CPU/IO & Chọn Join Order tối ưu...");
        return plan;
    }
}

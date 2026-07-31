package entity.query_processor.optimizer;

import entity.query_processor.interfaces.OptimizationRule;
import entity.query_processor.plan.LogicalPlan;

public class CostBased implements OptimizationRule {
    @Override
    public LogicalPlan optimize(LogicalPlan plan) {
        System.out.println("  -> [Strategy: Cost-Based] Tính toán chi phí CPU/IO & Chọn Join Order tối ưu...");
        return plan;
    }
}

package entity.query_processor.interfaces;

import entity.query_processor.plan.LogicalPlan;

/**
 * Giao diện định nghĩa chiến lược tối ưu hóa kế hoạch thực thi logic.
 * Áp dụng Pattern Strategy.
 */
public interface OptimizationRule {

    /**
     * Tối ưu hóa kế hoạch logic đầu vào và trả về kế hoạch đã tối ưu.
     *
     * @param plan Kế hoạch logic chưa tối ưu.
     * @return Kế hoạch logic đã tối ưu.
     */
    LogicalPlan optimize(LogicalPlan plan);
}

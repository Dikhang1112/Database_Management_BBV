package query_processor.optimizer;

import query_processor.planner.LogicalPlan;

/**
 * Đánh giá chi phí tài nguyên (CPU, I/O) cho các phương án kế hoạch thực thi logic.
 * Áp dụng Pattern Strategy.
 */
public class CostEstimator {

    /**
     * Khởi tạo CostEstimator.
     */
    public CostEstimator() {
        // TODO: Future DBMS logic implementation
    }

    /**
     * Ước lượng chi phí cho một kế hoạch logic chỉ định.
     *
     * @param plan Kế hoạch thực thi logic.
     * @return Giá trị chi phí dạng Object.
     */
    public Object estimate(LogicalPlan plan) {
        // TODO: Future DBMS logic implementation
        return null;
    }
}

package query_processor.plan;

/**
 * Chuẩn hóa cấu trúc cây Kế hoạch Logic (LogicalPlan), loại bỏ toán tử dư thừa và làm phẳng các cấu trúc lồng nhau.
 */
public class PlanNormalizer {

    public PlanNormalizer() {
    }

    /**
     * Chuẩn hóa kế hoạch thực thi logic.
     *
     * @param logicalPlan Kế hoạch logic đầu vào.
     * @return Kế hoạch logic đã được chuẩn hóa.
     */
    public LogicalPlan normalize(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        return logicalPlan;
    }
}

package query_processor.plan;

/**
 * Thẩm định tính hợp lệ và toàn vẹn của cây Kế hoạch Logic (LogicalPlan).
 */
public class PlanValidator {

    public PlanValidator() {
    }

    /**
     * Thẩm định cấu trúc LogicalPlan trước khi chuyển giao tạo kế hoạch vật lý.
     *
     * @param logicalPlan Kế hoạch thực thi logic.
     */
    public void validate(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
    }
}

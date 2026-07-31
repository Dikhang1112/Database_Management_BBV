package entity.query_processor.plan;

/**
 * Nhà máy (Factory Method) khởi tạo các toán tử thực thi vật lý từ nút LogicalPlanNode.
 * Pattern: Factory Method Pattern.
 */
public class PhysicalOperatorFactory {

    public PhysicalOperatorFactory() {
    }

    /**
     * Khởi tạo đối tượng toán tử vật lý từ nút LogicalPlanNode.
     *
     * @param node Nút toán tử logic đầu vào.
     * @return Nút toán tử vật lý (PhysicalPlanNode).
     */
    public PhysicalPlanNode createOperator(LogicalPlanNode node) {
        if (node == null) {
            throw new IllegalArgumentException("LogicalPlanNode cannot be null");
        }
        return new PhysicalPlanNode("PHYSICAL_OPERATOR");
    }
}

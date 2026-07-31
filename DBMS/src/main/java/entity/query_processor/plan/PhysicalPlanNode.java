package entity.query_processor.plan;

/**
 * Nút toán tử thực thi vật lý (Physical Operator Node) đại diện cho chiến lược thực thi cụ thể trên đĩa/bộ nhớ.
 */
public class PhysicalPlanNode {

    private String physicalOperatorType;

    public PhysicalPlanNode() {
    }

    public PhysicalPlanNode(String physicalOperatorType) {
        this.physicalOperatorType = physicalOperatorType;
    }

    public String getPhysicalOperatorType() {
        return physicalOperatorType;
    }

    public void setPhysicalOperatorType(String physicalOperatorType) {
        this.physicalOperatorType = physicalOperatorType;
    }
}

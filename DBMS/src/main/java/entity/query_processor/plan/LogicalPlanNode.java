package entity.query_processor.plan;

/**
 * Nút toán tử đại số logic đại diện cho một bước xử lý trong cây LogicalPlan.
 */
public class LogicalPlanNode {

    private String operatorType;

    public LogicalPlanNode() {
    }

    public LogicalPlanNode(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }
}

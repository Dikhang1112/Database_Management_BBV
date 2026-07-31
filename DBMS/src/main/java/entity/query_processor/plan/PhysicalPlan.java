package entity.query_processor.plan;

/**
 * Đóng gói cây Kế hoạch Thực thi Vật lý (Physical Plan) sẵn sàng cho ExecutionEngine.
 */
public class PhysicalPlan {

    private PhysicalPlanNode root;

    public PhysicalPlan() {
    }

    public PhysicalPlan(PhysicalPlanNode root) {
        this.root = root;
    }

    public PhysicalPlanNode getRoot() {
        return root;
    }

    public void setRoot(PhysicalPlanNode root) {
        this.root = root;
    }
}

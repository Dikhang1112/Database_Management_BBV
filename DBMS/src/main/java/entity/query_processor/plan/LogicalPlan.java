package entity.query_processor.plan;

/**
 * Đóng gói cây Kế hoạch Thực thi Logic (Logical Plan).
 */
public class LogicalPlan {

    private LogicalPlanNode root;

    public LogicalPlan() {
    }

    public LogicalPlan(LogicalPlanNode root) {
        this.root = root;
    }

    public LogicalPlanNode getRoot() {
        return root;
    }

    public void setRoot(LogicalPlanNode root) {
        this.root = root;
    }
}

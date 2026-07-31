package entity.query_processor.plan;

/**
 * Xây dựng từng bước cây Kế hoạch Thực thi Vật lý (PhysicalPlan) từ LogicalPlan.
 * Pattern: Builder Pattern.
 */
public class PhysicalPlanBuilder {

    private final PhysicalOperatorFactory physicalOperatorFactory;

    public PhysicalPlanBuilder() {
        this(new PhysicalOperatorFactory());
    }

    public PhysicalPlanBuilder(PhysicalOperatorFactory physicalOperatorFactory) {
        this.physicalOperatorFactory = physicalOperatorFactory != null ? physicalOperatorFactory : new PhysicalOperatorFactory();
    }

    /**
     * Build đối tượng PhysicalPlan từ Kế hoạch Logic.
     *
     * @param logicalPlan Kế hoạch logic LogicalPlan.
     * @return Đối tượng PhysicalPlan hoàn chỉnh.
     */
    public PhysicalPlan build(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        LogicalPlanNode root = logicalPlan.getRoot();
        if (root == null) {
            return new PhysicalPlan();
        }
        PhysicalPlanNode physicalOperator = physicalOperatorFactory.createOperator(root);
        return new PhysicalPlan(physicalOperator);
    }
}

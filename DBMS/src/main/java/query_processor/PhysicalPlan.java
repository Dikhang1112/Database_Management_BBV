package query_processor;

public class PhysicalPlan {
    private final double estimatedCost;

    public PhysicalPlan() {
        this.estimatedCost = 0.0;
    }

    public PhysicalPlan(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public PhysicalPlanNode getPhysicalRoot() {
        return new PhysicalPlanNode() {};
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }
}

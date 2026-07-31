package entity.execution_engine.abstracts;

public abstract class UnaryOperator extends ExecutionPlanNode {

    protected ExecutionPlanNode child;

    public UnaryOperator() {
    }

    public UnaryOperator(ExecutionPlanNode child) {
        this.child = child;
    }

    public ExecutionPlanNode getChild() {
        return child;
    }

    public void setChild(ExecutionPlanNode child) {
        this.child = child;
    }
}

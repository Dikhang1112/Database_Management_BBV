package entity.execution_engine.abstracts;

public abstract class BinaryOperator extends ExecutionPlanNode {

    protected ExecutionPlanNode left;
    protected ExecutionPlanNode right;

    public BinaryOperator() {
    }

    public BinaryOperator(ExecutionPlanNode left, ExecutionPlanNode right) {
        this.left = left;
        this.right = right;
    }

    public ExecutionPlanNode getLeft() {
        return left;
    }

    public void setLeft(ExecutionPlanNode left) {
        this.left = left;
    }

    public ExecutionPlanNode getRight() {
        return right;
    }

    public void setRight(ExecutionPlanNode right) {
        this.right = right;
    }
}

package execution_engine.helpers;

import execution_engine.domain.Tuple;

public class JoinContext {

    private Tuple left;
    private Tuple right;

    public Tuple getLeft() {
        return left;
    }

    public void setLeft(Tuple left) {
        this.left = left;
    }

    public Tuple getRight() {
        return right;
    }

    public void setRight(Tuple right) {
        this.right = right;
    }
}

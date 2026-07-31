package entity.execution_engine.operators;

import entity.execution_engine.abstracts.ExecutionPlanNode;
import entity.execution_engine.abstracts.UnaryOperator;
import entity.execution_engine.domain.Tuple;

public class ProjectOperator extends UnaryOperator {

    public ProjectOperator() {
    }

    public ProjectOperator(ExecutionPlanNode child) {
        super(child);
    }

    @Override
    public void open() {
        super.open();
        if (child != null) {
            child.open();
        }
    }

    @Override
    public Tuple next() {
        super.next();
        return child != null ? child.next() : null;
    }

    @Override
    public void close() {
        if (child != null) {
            child.close();
        }
        super.close();
    }
}

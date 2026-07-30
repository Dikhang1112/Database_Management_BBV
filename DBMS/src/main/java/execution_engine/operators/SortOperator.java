package execution_engine.operators;

import execution_engine.abstracts.ExecutionPlanNode;
import execution_engine.abstracts.UnaryOperator;
import execution_engine.domain.Tuple;

public class SortOperator extends UnaryOperator {

    public SortOperator() {
    }

    public SortOperator(ExecutionPlanNode child) {
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

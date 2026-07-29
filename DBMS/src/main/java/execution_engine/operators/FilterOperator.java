package execution_engine.operators;

import execution_engine.abstracts.ExecutionPlanNode;
import execution_engine.abstracts.UnaryOperator;
import execution_engine.domain.Tuple;

public class FilterOperator extends UnaryOperator {

    public FilterOperator() {
    }

    public FilterOperator(ExecutionPlanNode child) {
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

package entity.execution_engine.abstracts;

import entity.execution_engine.domain.Tuple;

public abstract class ExecutionOperatorDecorator extends ExecutionPlanNode {

    protected ExecutionPlanNode decoratedOperator;

    public ExecutionOperatorDecorator(ExecutionPlanNode decoratedOperator) {
        this.decoratedOperator = decoratedOperator;
    }

    @Override
    public void open() {
        if (decoratedOperator != null) {
            decoratedOperator.open();
        }
        super.open();
    }

    @Override
    public Tuple next() {
        super.next();
        return decoratedOperator != null ? decoratedOperator.next() : null;
    }

    @Override
    public void close() {
        if (decoratedOperator != null) {
            decoratedOperator.close();
        }
        super.close();
    }
}

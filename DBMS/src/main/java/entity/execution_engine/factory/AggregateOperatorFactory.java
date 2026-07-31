package entity.execution_engine.factory;

import entity.execution_engine.operators.AggregateOperator;

public class AggregateOperatorFactory {

    public AggregateOperator create() {
        return new AggregateOperator();
    }
}

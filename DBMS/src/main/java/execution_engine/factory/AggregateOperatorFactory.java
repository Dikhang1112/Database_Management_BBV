package execution_engine.factory;

import execution_engine.operators.AggregateOperator;

public class AggregateOperatorFactory {

    public AggregateOperator create() {
        return new AggregateOperator();
    }
}

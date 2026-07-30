package execution_engine.factory;

import execution_engine.operators.SortOperator;

public class SortOperatorFactory {

    public SortOperator create() {
        return new SortOperator();
    }
}

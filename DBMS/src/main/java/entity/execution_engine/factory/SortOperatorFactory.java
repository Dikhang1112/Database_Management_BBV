package entity.execution_engine.factory;

import entity.execution_engine.operators.SortOperator;

public class SortOperatorFactory {

    public SortOperator create() {
        return new SortOperator();
    }
}

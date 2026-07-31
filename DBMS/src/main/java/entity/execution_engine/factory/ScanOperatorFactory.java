package entity.execution_engine.factory;

import entity.execution_engine.operators.ScanOperator;

public class ScanOperatorFactory {

    public ScanOperator create() {
        return new ScanOperator();
    }
}

package execution_engine.factory;

import execution_engine.operators.ScanOperator;

public class ScanOperatorFactory {

    public ScanOperator create() {
        return new ScanOperator();
    }
}

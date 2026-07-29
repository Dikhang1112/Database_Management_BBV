package execution_engine.factory;

import execution_engine.operators.JoinOperator;

public class JoinOperatorFactory {

    public JoinOperator create() {
        return new JoinOperator();
    }
}

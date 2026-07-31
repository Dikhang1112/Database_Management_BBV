package entity.execution_engine.factory;

import entity.execution_engine.operators.JoinOperator;

public class JoinOperatorFactory {

    public JoinOperator create() {
        return new JoinOperator();
    }
}

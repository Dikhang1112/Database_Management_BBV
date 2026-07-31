package entity.execution_engine.operators;

import entity.execution_engine.abstracts.ExecutionOperatorDecorator;
import entity.execution_engine.abstracts.ExecutionPlanNode;
import entity.execution_engine.domain.Tuple;

public class LoggingOperatorDecorator extends ExecutionOperatorDecorator {

    public LoggingOperatorDecorator(ExecutionPlanNode decoratedOperator) {
        super(decoratedOperator);
    }

    @Override
    public Tuple next() {
        System.out.println("  [Decorator: Logging] Fetching next tuple from operator...");
        Tuple tuple = super.next();
        System.out.println("  [Decorator: Logging] Tuple fetched.");
        return tuple;
    }
}

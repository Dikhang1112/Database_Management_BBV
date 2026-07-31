package entity.execution_engine.strategy;

import entity.execution_engine.abstracts.ExecutionPlanNode;
import entity.execution_engine.domain.Tuple;

public interface JoinStrategy {

    Tuple execute(ExecutionPlanNode left, ExecutionPlanNode right);
}

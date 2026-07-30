package execution_engine.strategy;

import execution_engine.abstracts.ExecutionPlanNode;
import execution_engine.domain.Tuple;

public interface JoinStrategy {

    Tuple execute(ExecutionPlanNode left, ExecutionPlanNode right);
}

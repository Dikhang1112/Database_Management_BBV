package entity.execution_engine.strategy;

import entity.execution_engine.abstracts.ExecutionPlanNode;
import entity.execution_engine.domain.Tuple;

public class NestedLoopJoinStrategy implements JoinStrategy {

    @Override
    public Tuple execute(ExecutionPlanNode left, ExecutionPlanNode right) {
        System.out.println("  -> [JoinStrategy: Nested Loop] Executing Nested Loop Join.");
        return null;
    }
}

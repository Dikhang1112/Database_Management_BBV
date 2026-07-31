package entity.execution_engine.strategy;

import entity.execution_engine.abstracts.ExecutionPlanNode;
import entity.execution_engine.domain.Tuple;

public class MergeJoinStrategy implements JoinStrategy {

    @Override
    public Tuple execute(ExecutionPlanNode left, ExecutionPlanNode right) {
        System.out.println("  -> [JoinStrategy: Merge Join] Executing Sort-Merge Join.");
        return null;
    }
}

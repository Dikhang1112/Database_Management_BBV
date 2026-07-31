package entity.execution_engine.strategy;

import entity.execution_engine.abstracts.ExecutionPlanNode;
import entity.execution_engine.domain.Tuple;

public class HashJoinStrategy implements JoinStrategy {

    @Override
    public Tuple execute(ExecutionPlanNode left, ExecutionPlanNode right) {
        System.out.println("  -> [JoinStrategy: Hash Join] Executing Hash Join (Build & Probe).");
        return null;
    }
}

package execution_engine.strategy;

import execution_engine.abstracts.ExecutionPlanNode;
import execution_engine.domain.Tuple;

public class MergeJoinStrategy implements JoinStrategy {

    @Override
    public Tuple execute(ExecutionPlanNode left, ExecutionPlanNode right) {
        System.out.println("  -> [JoinStrategy: Merge Join] Executing Sort-Merge Join.");
        return null;
    }
}

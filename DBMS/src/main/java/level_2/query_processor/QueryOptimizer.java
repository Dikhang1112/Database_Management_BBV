package level_2.query_processor;

import level_2.execution_engine.ExecutionPlan;

public class QueryOptimizer {
    private int maxOptimizationTimeMs;

    public QueryOptimizer(int maxOptimizationTimeMs) {
        this.maxOptimizationTimeMs = maxOptimizationTimeMs;
    }

    public ExecutionPlan optimizePlan(AbstractSyntaxTree validatedAst) {
        return null;
    }
}

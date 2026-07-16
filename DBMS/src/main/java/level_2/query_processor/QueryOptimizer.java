package query_processor;

import execution_engine.ExecutionPlan;

public class QueryOptimizer {
    private int maxOptimizationTimeMs;

    public QueryOptimizer(int maxOptimizationTimeMs) {
        this.maxOptimizationTimeMs = maxOptimizationTimeMs;
    }

    public ExecutionPlan optimizePlan(AbstractSyntaxTree validatedAst) {
        return null;
    }
}

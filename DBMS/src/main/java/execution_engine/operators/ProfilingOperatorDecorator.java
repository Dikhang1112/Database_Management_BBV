package execution_engine.operators;

import execution_engine.abstracts.ExecutionOperatorDecorator;
import execution_engine.abstracts.ExecutionPlanNode;
import execution_engine.domain.Tuple;

public class ProfilingOperatorDecorator extends ExecutionOperatorDecorator {

    private long totalExecutionTimeNs = 0;

    public ProfilingOperatorDecorator(ExecutionPlanNode decoratedOperator) {
        super(decoratedOperator);
    }

    @Override
    public Tuple next() {
        long start = System.nanoTime();
        Tuple tuple = super.next();
        long elapsed = System.nanoTime() - start;
        totalExecutionTimeNs += elapsed;
        System.out.println("  [Decorator: Profiling] Operator step took " + elapsed + " ns (Total: " + totalExecutionTimeNs + " ns)");
        return tuple;
    }

    public long getTotalExecutionTimeNs() {
        return totalExecutionTimeNs;
    }
}

package execution_engine.observer;

public class StatisticsCollector implements ExecutionListener {

    private long tupleCount = 0;

    @Override
    public void onExecutionStarted() {
        System.out.println("  [Observer: StatisticsCollector] Execution Started.");
        this.tupleCount = 0;
    }

    @Override
    public void onTupleProcessed() {
        tupleCount++;
    }

    @Override
    public void onExecutionFinished() {
        System.out.println("  [Observer: StatisticsCollector] Execution Finished. Total tuples processed: " + tupleCount);
    }

    public long getTupleCount() {
        return tupleCount;
    }
}

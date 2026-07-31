package entity.execution_engine.observer;

public class ProgressMonitor implements ExecutionListener {

    private long processedCount = 0;

    @Override
    public void onExecutionStarted() {
        System.out.println("  [Observer: ProgressMonitor] Monitoring query execution progress...");
        processedCount = 0;
    }

    @Override
    public void onTupleProcessed() {
        processedCount++;
        if (processedCount % 100 == 0) {
            System.out.println("  [Observer: ProgressMonitor] Processed " + processedCount + " tuples.");
        }
    }

    @Override
    public void onExecutionFinished() {
        System.out.println("  [Observer: ProgressMonitor] Query execution progress completed (100%).");
    }
}

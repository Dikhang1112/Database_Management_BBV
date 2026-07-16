package high_level;

/**
 * Muscle of the DBMS. Evaluates the ExecutionPlan using Volcano iterators and constraints checks.
 */
public class ExecutionEngine {
    public ExecutionEngine() {
    }

    /**
     * Allocates system resources dynamically for execution.
     */
    public void allocate(PerformanceScalability performance) {
        // Allocate buffers / worker threads
    }

    /**
     * Fetches pages or rows from the StorageEngine.
     */
    public void fetch(StorageEngine storage) {
        // Fetch pages/rows
    }
}

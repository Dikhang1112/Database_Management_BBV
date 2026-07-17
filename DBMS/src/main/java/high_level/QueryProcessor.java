package high_level;

/**
 * Core Brain of the DBMS. Parses, compiles, and optimizes queries.
 */
public class QueryProcessor {
    public QueryProcessor() {
    }

    /**
     * Validates schema metadata against the active DatabaseCoreServer.
     */
    public void validate(DatabaseCoreServer server) {
        // Validate query against schema catalog
    }

    /**
     * Submits physical ExecutionPlan to the ExecutionEngine.
     */
    public void dispatch(ExecutionEngine executionEngine) {
        // Dispatch compiled plan
    }
}

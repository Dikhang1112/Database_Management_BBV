package level_2.query_processor;

import level_2.database_core_server.interfaces.SessionContext;
import level_2.execution_engine.interfaces.PlanExecutor;

public class QueryIngestionEngine {
    private SyntaxParser parserComponent;
    private QueryValidator validatorComponent;
    private QueryOptimizer optimizerComponent;
    private PlanExecutor executorComponent;

    public QueryIngestionEngine(SyntaxParser parserComponent, QueryValidator validatorComponent, QueryOptimizer optimizerComponent, PlanExecutor executorComponent) {
        this.parserComponent = parserComponent;
        this.validatorComponent = validatorComponent;
        this.optimizerComponent = optimizerComponent;
        this.executorComponent = executorComponent;
    }

    public void ingestQuery(String sqlText, SessionContext session) {
    }
}

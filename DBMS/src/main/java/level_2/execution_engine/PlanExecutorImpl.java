package level_2.execution_engine;

import level_2.database_core_server.interfaces.SessionContext;
import level_2.execution_engine.interfaces.PlanExecutor;
import level_2.execution_engine.interfaces.RowIterator;

public class PlanExecutorImpl implements PlanExecutor {
    public PlanExecutorImpl() {
    }

    @Override
    public RowIterator executePlan(ExecutionPlan plan, SessionContext session) {
        return null;
    }
}

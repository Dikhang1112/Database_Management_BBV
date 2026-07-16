package execution_engine;

import database_core_server.interfaces.SessionContext;
import execution_engine.interfaces.PlanExecutor;
import execution_engine.interfaces.RowIterator;

public class PlanExecutorImpl implements PlanExecutor {
    public PlanExecutorImpl() {
    }

    @Override
    public RowIterator executePlan(ExecutionPlan plan, SessionContext session) {
        return null;
    }
}

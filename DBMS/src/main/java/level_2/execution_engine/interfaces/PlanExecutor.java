package execution_engine.interfaces;

import database_core_server.interfaces.SessionContext;
import execution_engine.ExecutionPlan;

public interface PlanExecutor {
    RowIterator executePlan(ExecutionPlan plan, SessionContext session);
}

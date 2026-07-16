package level_2.execution_engine.interfaces;

import level_2.database_core_server.interfaces.SessionContext;
import level_2.execution_engine.ExecutionPlan;

public interface PlanExecutor {
    RowIterator executePlan(ExecutionPlan plan, SessionContext session);
}

package execution_engine.facade;

import execution_engine.abstracts.ExecutionPlanNode;
import execution_engine.factory.OperatorFactory;
import execution_engine.domain.ExecutionCoordinator;
import execution_engine.domain.ExecutionContext;
import execution_engine.domain.OperatorScheduler;
import execution_engine.domain.QueryResult;
import execution_engine.domain.QueryResultBuilder;
import execution_engine.domain.Tuple;
import execution_engine.observer.ExecutionListener;
import query_processor.plan.PhysicalPlan;

import java.util.ArrayList;
import java.util.List;

public class ExecutionEngine {

    private final ExecutionCoordinator executionCoordinator;
    private final OperatorScheduler operatorScheduler;
    private final ExecutionContext executionContext;
    private final QueryResultBuilder queryResultBuilder;
    private final OperatorFactory operatorFactory;
    private final List<ExecutionListener> listeners;

    public ExecutionEngine() {
        this.executionCoordinator = new ExecutionCoordinator();
        this.operatorScheduler = new OperatorScheduler();
        this.executionContext = new ExecutionContext();
        this.queryResultBuilder = new QueryResultBuilder();
        this.operatorFactory = new OperatorFactory();
        this.listeners = new ArrayList<>();
    }

    public void addExecutionListener(ExecutionListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    public QueryResult execute(PhysicalPlan physicalPlan) {
        long startTime = System.currentTimeMillis();
        notifyExecutionStarted();
        executionCoordinator.startExecution();

        ExecutionPlanNode rootOperator = operatorFactory.createOperator(physicalPlan != null ? physicalPlan.getRoot() : null);
        executionContext.setRootNode(rootOperator);
        operatorScheduler.schedule(rootOperator);

        if (rootOperator != null) {
            rootOperator.open();
            Tuple tuple;
            while ((tuple = rootOperator.next()) != null) {
                queryResultBuilder.addRow(tuple);
                notifyTupleProcessed();
            }
            rootOperator.close();
        }

        executionCoordinator.finishExecution();
        notifyExecutionFinished();

        long elapsedMs = System.currentTimeMillis() - startTime;
        queryResultBuilder.addMetadata("executionTimeMs", elapsedMs);
        queryResultBuilder.addMetadata("status", "SUCCESS");

        return queryResultBuilder.build();
    }

    private void notifyExecutionStarted() {
        for (ExecutionListener listener : listeners) {
            listener.onExecutionStarted();
        }
    }

    private void notifyTupleProcessed() {
        for (ExecutionListener listener : listeners) {
            listener.onTupleProcessed();
        }
    }

    private void notifyExecutionFinished() {
        for (ExecutionListener listener : listeners) {
            listener.onExecutionFinished();
        }
    }
}

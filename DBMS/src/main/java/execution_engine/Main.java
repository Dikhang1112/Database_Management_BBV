package execution_engine;

import execution_engine.abstracts.ExecutionPlanNode;
import execution_engine.enums.ExecutionState;
import execution_engine.facade.ExecutionEngine;
import execution_engine.factory.OperatorFactory;
import execution_engine.domain.QueryResult;
import execution_engine.domain.Tuple;
import execution_engine.operators.FilterOperator;
import execution_engine.operators.JoinOperator;
import execution_engine.operators.LoggingOperatorDecorator;
import execution_engine.operators.ProfilingOperatorDecorator;
import execution_engine.operators.ProjectOperator;
import execution_engine.operators.ScanOperator;
import execution_engine.observer.StatisticsCollector;
import execution_engine.strategy.HashJoinStrategy;
import execution_engine.strategy.IndexScanStrategy;
import execution_engine.strategy.NestedLoopJoinStrategy;
import execution_engine.strategy.SequentialScanStrategy;
import query_processor.plan.PhysicalPlan;
import query_processor.plan.PhysicalPlanNode;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

    }
}

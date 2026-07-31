package entity.execution_engine;

import entity.execution_engine.abstracts.ExecutionPlanNode;
import entity.execution_engine.enums.ExecutionState;
import entity.execution_engine.facade.ExecutionEngine;
import entity.execution_engine.factory.OperatorFactory;
import entity.execution_engine.domain.QueryResult;
import entity.execution_engine.domain.Tuple;
import entity.execution_engine.operators.FilterOperator;
import entity.execution_engine.operators.JoinOperator;
import entity.execution_engine.operators.LoggingOperatorDecorator;
import entity.execution_engine.operators.ProfilingOperatorDecorator;
import entity.execution_engine.operators.ProjectOperator;
import entity.execution_engine.operators.ScanOperator;
import entity.execution_engine.observer.StatisticsCollector;
import entity.execution_engine.strategy.HashJoinStrategy;
import entity.execution_engine.strategy.IndexScanStrategy;
import entity.execution_engine.strategy.NestedLoopJoinStrategy;
import entity.execution_engine.strategy.SequentialScanStrategy;
import entity.query_processor.plan.PhysicalPlan;
import entity.query_processor.plan.PhysicalPlanNode;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

    }
}

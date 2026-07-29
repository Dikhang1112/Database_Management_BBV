# Design Patterns in Execution Engine Module

This document details the **core and subsystem Design Patterns** applied in the `execution_engine` module, organized by GoF pattern classification: **Creational Patterns** (Create) ➔ **Structural Patterns** (Structure) ➔ **Behavioral Patterns** (Behavior).

---

## 1. Creational Patterns (Create)

### 1.1. Factory Method Pattern
* **Pattern**: Factory Method Pattern
* **Class/Interface Applied**: `OperatorFactory`
* **Method**: `createOperator(PhysicalPlanNode node)`
* **Reason for Use**: Encapsulates the instantiation logic that maps physical plan nodes (`PhysicalPlanNode`) to their corresponding physical execution operators (`ScanOperator`, `FilterOperator`, `ProjectOperator`, `JoinOperator`, `AggregateOperator`, `SortOperator`), hiding concrete operator creation from `ExecutionEngine`.

---

## 2. Structural Patterns (Structure)

### 2.1. Facade Pattern
* **Pattern**: Facade Pattern
* **Class/Interface Applied**: `ExecutionEngine`
* **Method**: `execute(PhysicalPlan physicalPlan)`
* **Reason for Use**: Provides a unified, simplified entry point for external callers (Query Processor, Main Driver) to execute physical query plans and retrieve query results (`QueryResult`), concealing the underlying complexity of operator trees, strategy selection, iterator loops, and observer notifications.

### 2.2. Composite Pattern
* **Pattern**: Composite Pattern
* **Class/Interface Applied**: `ExecutionPlanNode` (Abstract base class for `ScanOperator`, `FilterOperator`, `ProjectOperator`, `JoinOperator`, `AggregateOperator`, `SortOperator`)
* **Method**: `open()`, `next()`, `close()`
* **Reason for Use**: Models physical execution plans as a hierarchical tree of Volcano-style operators. Treats individual leaf operators (e.g., `ScanOperator`) and composite parent operators (e.g., `JoinOperator`, `FilterOperator`) uniformly through the same `open()`, `next()`, `close()` interface.

### 2.3. Decorator Pattern
* **Pattern**: Decorator Pattern
* **Class/Interface Applied**: `ExecutionOperatorDecorator` (Abstract decorator implemented by `LoggingOperatorDecorator`, `ProfilingOperatorDecorator`)
* **Method**: `open()`, `next()`, `close()`
* **Reason for Use**: Dynamically attaches additional responsibilities (such as runtime execution logging, tuple counts, and performance metrics profiling) to execution operators without modifying existing operator code.

---

## 3. Behavioral Patterns (Behavior)

### 3.1. Strategy Pattern
* **Pattern**: Strategy Pattern
* **Class/Interface Applied**: `JoinStrategy` (implemented by `NestedLoopJoinStrategy`, `HashJoinStrategy`, `MergeJoinStrategy`), `ScanStrategy` (implemented by `SequentialScanStrategy`, `IndexScanStrategy`)
* **Method**: `execute()`, `scan()`
* **Reason for Use**: Encapsulates interchangeable join algorithms and table scan mechanisms into separate strategy classes, enabling `JoinOperator` and `ScanOperator` to select the most optimal physical execution algorithm based on statistics and index availability.

### 3.2. Iterator Pattern (Volcano Model)
* **Pattern**: Iterator Pattern
* **Class/Interface Applied**: `TupleIterator`
* **Method**: `hasNext()`, `next()`
* **Reason for Use**: Implements the classic Volcano Iterator Execution Model, allowing parent operators to pull tuples demand-driven (one tuple at a time) from child operators without loading entire intermediate result sets into memory.

### 3.3. Template Method Pattern
* **Pattern**: Template Method Pattern
* **Class/Interface Applied**: `ScanOperator`
* **Method**: `open()`, `next()`, `close()`, `fetchTuple()`
* **Reason for Use**: Defines the skeleton sequence for table scanning in `ScanOperator`, while delegating the specific tuple extraction mechanics (`fetchTuple()`) to specialized scan strategies or underlying storage handlers.

### 3.4. State Pattern
* **Pattern**: State Pattern
* **Class/Interface Applied**: `ExecutionState` (Enum: `CREATED`, `OPEN`, `RUNNING`, `FINISHED`, `CLOSED`)
* **Method**: `setState()`, `getState()`
* **Reason for Use**: Manages the dynamic lifecycle states of execution operators, enforcing valid state transitions (e.g., requiring an operator to be `OPEN` before calling `next()`) and preventing resource leaks.

### 3.5. Observer Pattern
* **Pattern**: Observer Pattern
* **Class/Interface Applied**: `ExecutionListener` (Interface implemented by `StatisticsCollector`)
* **Method**: `onExecutionStarted()`, `onTupleProcessed()`, `onExecutionFinished()`
* **Reason for Use**: Decouples execution monitoring from core operator execution. Allows external collectors (like `StatisticsCollector`) to observe execution events (start, tuple processing, completion) in real time without polluting execution code.

# Sequence Diagrams for Execution Engine Module (By Pattern & By Feature)

Tài liệu thể hiện các sơ đồ trình tự (Sequence Diagrams) cho module **Execution Engine**, bao gồm 2 phần chính:
- **Phần I: Sơ Đồ Trình Tự Theo Design Pattern** (Theo thứ tự: Creational ➔ Structural ➔ Behavioral).
- **Phần II: Sơ Đồ Trình Tự Theo Feature Cốt Lõi** (Chuỗi tương tác tính năng thực tế).

---

# PHẦN I: SƠ ĐỒ TRÌNH TỰ THEO DESIGN PATTERN

## 1. Creational Patterns (Create)

### 1.1. Factory Method Pattern
* **Pattern**: Factory Method Pattern
* **Class/Interface Applied**: `OperatorFactory`
* **Method**: `createOperator(PhysicalPlanNode node)`

```mermaid
sequenceDiagram
    autonumber
    actor Engine as ExecutionEngine
    participant Factory as OperatorFactory (Factory)
    participant Op as ExecutionPlanNode

    Engine->>+Factory: createOperator(physicalNode)
    alt PhysicalNode is HashJoinNode
        Factory->>+Op: instantiate JoinOperator(HashJoinStrategy)
    else PhysicalNode is IndexScanNode
        Factory->>+Op: instantiate ScanOperator(IndexScanStrategy)
    else PhysicalNode is FilterNode
        Factory->>+Op: instantiate FilterOperator()
    end
    Op-->>-Factory: ExecutionPlanNode instance
    Factory-->>-Engine: Operator Tree Root
```

---

## 2. Structural Patterns (Structure)

### 2.1. Facade Pattern
* **Pattern**: Facade Pattern
* **Class/Interface Applied**: `ExecutionEngine`
* **Method**: `execute(PhysicalPlan physicalPlan)`

```mermaid
sequenceDiagram
    autonumber
    actor Driver as QueryProcessor / Main Driver
    participant EE as ExecutionEngine (Facade)
    participant Factory as OperatorFactory
    participant Tree as ExecutionPlanNode (Root)
    participant Result as QueryResult

    Driver->>+EE: execute(physicalPlan)
    EE->>+Factory: createOperator(physicalPlan.getRoot())
    Factory-->>-EE: rootOperator (ExecutionPlanNode)
    EE->>+Tree: open()
    Tree-->>-EE: Operator Opened
    loop Until no more tuples
        EE->>+Tree: next()
        Tree-->>-EE: Tuple Data
    end
    EE->>+Tree: close()
    Tree-->>-EE: Operator Closed
    EE-->>-Driver: QueryResult
```

---

### 2.2. Composite Pattern
* **Pattern**: Composite Pattern
* **Class/Interface Applied**: `ExecutionPlanNode` (implemented by `ScanOperator`, `FilterOperator`, `JoinOperator`, etc.)
* **Method**: `open()`, `next()`, `close()`

```mermaid
sequenceDiagram
    autonumber
    participant Parent as FilterOperator (Composite Node)
    participant Child as ScanOperator (Leaf Node)

    Parent->>+Child: open()
    Child-->>-Parent: Scan Opened

    loop Pull Tuples (Volcano Model)
        Parent->>+Child: next()
        Child-->>-Parent: Tuple
        Parent->>Parent: evaluatePredicate(Tuple)
    end

    Parent->>+Child: close()
    Child-->>-Parent: Scan Closed
```

---

### 2.3. Decorator Pattern
* **Pattern**: Decorator Pattern
* **Class/Interface Applied**: `ExecutionOperatorDecorator` (implemented by `LoggingOperatorDecorator`, `ProfilingOperatorDecorator`)
* **Method**: `open()`, `next()`, `close()`

```mermaid
sequenceDiagram
    autonumber
    actor EE as ExecutionEngine
    participant Decorator as ProfilingOperatorDecorator (Decorator)
    participant Operator as ExecutionPlanNode (Wrapped Operator)

    EE->>+Decorator: next()
    Decorator->>Decorator: startTimer()
    Decorator->>+Operator: next()
    Operator-->>-Decorator: Tuple Data
    Decorator->>Decorator: stopTimer() & recordMetrics()
    Decorator-->>-EE: Tuple Data
```

---

## 3. Behavioral Patterns (Behavior)

### 3.1. Strategy Pattern
* **Pattern**: Strategy Pattern
* **Class/Interface Applied**: `JoinStrategy` (`NestedLoopJoinStrategy`, `HashJoinStrategy`, `MergeJoinStrategy`), `ScanStrategy` (`SequentialScanStrategy`, `IndexScanStrategy`)
* **Method**: `execute()`, `scan()`

```mermaid
sequenceDiagram
    autonumber
    participant JoinOp as JoinOperator (Context)
    participant Strategy as JoinStrategy (HashJoinStrategy)
    participant Outer as Outer Child Operator
    participant Inner as Inner Child Operator

    JoinOp->>+Strategy: execute(outerOp, innerOp)
    Strategy->>+Inner: buildHashTable()
    Inner-->>-Strategy: Hash Table Built
    Strategy->>+Outer: probeHashTable()
    Outer-->>-Strategy: Joined Tuples
    Strategy-->>-JoinOp: Joined Tuples Result
```

---

### 3.2. Iterator Pattern (Volcano Model)
* **Pattern**: Iterator Pattern
* **Class/Interface Applied**: `TupleIterator`
* **Method**: `hasNext()`, `next()`

```mermaid
sequenceDiagram
    autonumber
    actor Caller as ExecutionPlanNode / Engine
    participant Iter as TupleIterator (Iterator)
    participant Storage as StorageEngine / Buffer

    Caller->>+Iter: hasNext()
    Iter->>+Storage: checkTupleAvailability()
    Storage-->>-Iter: true
    Iter-->>-Caller: true

    Caller->>+Iter: next()
    Iter->>+Storage: fetchNextTuple()
    Storage-->>-Iter: Tuple Data
    Iter-->>-Caller: Tuple Data
```

---

### 3.3. Template Method Pattern
* **Pattern**: Template Method Pattern
* **Class/Interface Applied**: `ScanOperator`
* **Method**: `open()`, `next()`, `close()`, `fetchTuple()`

```mermaid
sequenceDiagram
    autonumber
    participant Engine as ExecutionEngine
    participant Template as ScanOperator (Template Method)
    participant Strategy as ScanStrategy (IndexScanStrategy)

    Engine->>+Template: next()
    Template->>Template: checkState(RUNNING)
    Template->>+Strategy: fetchTuple()
    Strategy-->>-Template: Tuple Data
    Template->>Template: updateMetrics()
    Template-->>-Engine: Tuple Data
```

---

### 3.4. State Pattern
* **Pattern**: State Pattern
* **Class/Interface Applied**: `ExecutionState` (Enum: `CREATED`, `OPEN`, `RUNNING`, `FINISHED`, `CLOSED`)
* **Method**: `setState()`, `getState()`

```mermaid
sequenceDiagram
    autonumber
    participant EE as ExecutionEngine
    participant Op as ExecutionPlanNode (State Context)

    EE->>+Op: open()
    Op->>Op: setState(OPEN)
    Op-->>-EE: State = OPEN

    EE->>+Op: next()
    Op->>Op: setState(RUNNING)
    Op-->>-EE: Tuple Data

    EE->>+Op: close()
    Op->>Op: setState(CLOSED)
    Op-->>-EE: State = CLOSED
```

---

### 3.5. Observer Pattern
* **Pattern**: Observer Pattern
* **Class/Interface Applied**: `ExecutionListener` (implemented by `StatisticsCollector`)
* **Method**: `onExecutionStarted()`, `onTupleProcessed()`, `onExecutionFinished()`

```mermaid
sequenceDiagram
    autonumber
    participant EE as ExecutionEngine
    participant Listener as ExecutionListener (StatisticsCollector)

    EE->>+Listener: onExecutionStarted()
    Listener-->>-EE: Ack

    loop Tuple Processing
        EE->>+Listener: onTupleProcessed(tupleCount)
        Listener-->>-EE: Ack
    end

    EE->>+Listener: onExecutionFinished()
    Listener-->>-EE: Metrics Summarized
```

---

# PHẦN II: SƠ ĐỒ TRÌNH TỰ THEO FEATURE CỐT LÕI

## 1. Full Physical Query Plan Execution Workflow
* **Feature**: Complete Physical Plan Execution
* **Actor**: External Caller / QueryProcessor
* **Design Patterns**: Facade, Factory Method, Composite, State, Observer

```mermaid
sequenceDiagram
    autonumber
    actor QP as QueryProcessor
    participant EE as ExecutionEngine (Facade)
    participant Factory as OperatorFactory (Factory Method)
    participant Root as FilterOperator (Composite Root)
    participant Scan as ScanOperator (Composite Leaf)
    participant Listener as StatisticsCollector (Observer)

    QP->>+EE: execute(physicalPlan)
    EE->>+Factory: createOperator(physicalPlan.getRoot())
    Factory-->>-EE: FilterOperator Root
    EE->>+Listener: onExecutionStarted()
    Listener-->>-EE: Ack

    EE->>+Root: open()
    Root->>Root: setState(OPEN)
    Root->>+Scan: open()
    Scan->>Scan: setState(OPEN)
    Scan-->>-Root: Scan Opened
    Root-->>-EE: Tree Opened

    loop Pull Tuples (Volcano Iterator)
        EE->>+Root: next()
        Root->>+Scan: next()
        Scan-->>-Root: Tuple
        Root-->>-EE: Filtered Tuple
        EE->>+Listener: onTupleProcessed(1)
        Listener-->>-EE: Ack
    end

    EE->>+Root: close()
    Root->>+Scan: close()
    Scan->>Scan: setState(CLOSED)
    Scan-->>-Root: Scan Closed
    Root->>Root: setState(CLOSED)
    Root-->>-EE: Tree Closed

    EE->>+Listener: onExecutionFinished()
    Listener-->>-EE: Summary Ready
    EE-->>-QP: QueryResult
```

---

## 2. Dynamic Join Strategy Execution Workflow
* **Feature**: Join Execution (Hash Join / Nested Loop Join)
* **Actor**: ExecutionEngine
* **Design Patterns**: Strategy, Composite, Iterator

```mermaid
sequenceDiagram
    autonumber
    actor EE as ExecutionEngine
    participant JoinOp as JoinOperator
    participant Strategy as HashJoinStrategy (Strategy)
    participant BuildChild as ScanOperator (Outer Table)
    participant ProbeChild as ScanOperator (Inner Table)

    EE->>+JoinOp: open()
    JoinOp->>+Strategy: execute(BuildChild, ProbeChild)
    Strategy->>+BuildChild: open()
    BuildChild-->>-Strategy: OK
    loop Build Hash Table
        Strategy->>+BuildChild: next()
        BuildChild-->>-Strategy: Outer Tuple
        Strategy->>Strategy: insertIntoHashTable(Tuple)
    end
    Strategy->>+ProbeChild: open()
    ProbeChild-->>-Strategy: OK
    Strategy-->>-JoinOp: Join Prepared

    loop Probe Stage
        EE->>+JoinOp: next()
        JoinOp->>+Strategy: probeNext()
        Strategy->>+ProbeChild: next()
        ProbeChild-->>-Strategy: Inner Tuple
        Strategy->>Strategy: lookupHashTable(Inner Tuple)
        Strategy-->>-JoinOp: Joined Tuple
        JoinOp-->>-EE: Joined Tuple
    end
```

---

## 3. Operator Execution Performance Profiling (Decorator)
* **Feature**: Real-Time Performance Profiling & Logging
* **Actor**: ExecutionEngine
* **Design Patterns**: Decorator, Composite

```mermaid
sequenceDiagram
    autonumber
    actor EE as ExecutionEngine
    participant Profile as ProfilingOperatorDecorator (Decorator)
    participant Log as LoggingOperatorDecorator (Decorator)
    participant Op as JoinOperator (Target Operator)

    EE->>+Profile: next()
    Profile->>Profile: startNanoseconds = System.nanoTime()
    Profile->>+Log: next()
    Log->>Log: log("Fetching next tuple from JoinOperator")
    Log->>+Op: next()
    Op-->>-Log: Joined Tuple
    Log-->>-Profile: Joined Tuple
    Profile->>Profile: elapsed = System.nanoTime() - startNanoseconds
    Profile-->>-EE: Joined Tuple with Profiling Metrics
```

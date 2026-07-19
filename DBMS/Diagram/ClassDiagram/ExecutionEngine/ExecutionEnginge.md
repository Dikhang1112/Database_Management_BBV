```mermaid
    classDiagram
        direction LR

        %% ==========================================
        %% 1. PLAN ORCHESTRATION
        %% ==========================================
        class PlanExecutor {
            +executePlan(ExecutionPlan plan, SessionContext session) RowIterator
        }
        note for PlanExecutor "Coordinates query pipelines and drives the root operator node"

        class RowIterator {
            <<interface>>
            +open() void
            +next() Row
            +hasNext() boolean
            +close() void
        }
        note for RowIterator "Volcano-style streaming engine interface enabling scalable pipelined iteration"

        class ExecutionPlan {
            -int planID [PK]
            -String optimizedTreeJson
            +getRootOperatorID() int
        }
        note for ExecutionPlan "Immutable data model holding parsed physical operation steps"

        %% ==========================================
        %% 2. CORE OPERATORS
        %% ==========================================
        class TableScanOperator {
            -int targetTableID [FK]
            -int currentBlockAddress
        }
        note for TableScanOperator "Performs full sequential physical disk block read"

        class IndexSeekOperator {
            -int targetIndexID [FK]
            -Object seekKey
        }
        note for IndexSeekOperator "Executes fast point-lookup using B+Tree indexes"

        class NestedLoopJoinOperator {
            -RowIterator outerStream
            -RowIterator innerStream
            -int outerJoinColumnID [FK]
            -int innerJoinColumnID [FK]
        }
        note for NestedLoopJoinOperator "Evaluates matching criteria between two data pipelines"

        %% ==========================================
        %% 3. RESULT CHANNELS
        %% ==========================================
        class ResultStreamer {
            <<interface>>
            +formatData(Row row) byte[]
            +streamToClient(byte[] serializedBytes, SessionContext session) void
        }
        note for ResultStreamer "Serialization interface transforming memory tuples to wires"

        class TdsResultProcessor {
            -String encodingCharset
            -int maxFlushBufferSize
        }
        note for TdsResultProcessor "Packs structured rows into binary TDS chunks"

        %% ==========================================
        %% 4. CONTEXT DATA MODELS
        %% ==========================================
        class SessionContext {
            <<interface>>
            +getSessionID() int
            +getCurrentUser() String
            +updateState(String newState) void
        }
        note for SessionContext "Abstract handler giving secure access to client's physical socket descriptor"

        class Row {
            -long rowID
            -List~Field~ fields
            +getFields() List~Field~
        }
        note for Row "Volatile runtime memory tuple moving across streaming pipes"

        %% ==========================================
        %% CORE REALIZATIONS & COMPOSITIONS
        %% ==========================================
        RowIterator <|.. TableScanOperator
        RowIterator <|.. IndexSeekOperator
        RowIterator <|.. NestedLoopJoinOperator
        ResultStreamer <|.. TdsResultProcessor

        NestedLoopJoinOperator "1" *-- "2" RowIterator
```

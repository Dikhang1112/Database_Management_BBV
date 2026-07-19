```mermaid
sequenceDiagram
    autonumber
    
    %% --- FIXED ARCHITECTURAL OBJECTS (TOP ROW ONLY) ---
    actor User as User
    participant EX as PlanExecutor
    participant CTX as SessionContext
    participant JOIN as NestedLoopJoinOperator
    participant SCAN as TableScanOperator
    participant SEEK as IndexSeekOperator

    %% --- WORKFLOW: PIPELINE INITIALIZATION ---
    Note over User, SEEK: QUERY PIPELINE INITIALIZATION PHASE (TOP-DOWN)
    
    User->>EX: Send compiled ExecutionPlan directives
    activate EX
    
    EX->>CTX: updateState("RUNNING_QUERY")
    activate CTX
    Note over CTX: Locks the session thread 
    CTX-->>EX: State acknowledged
    deactivate CTX

    EX->>JOIN: open()
    activate JOIN
    Note over JOIN: Root operator allocates local match buffer
    
    %% Recursive initialization down the operator tree (Left Child: Outer Table)
    JOIN->>SCAN: open()
    activate SCAN
    Note over SCAN: Connects to Storage Engine and opens physical storage file descriptors
    SCAN-->>JOIN: File descriptors ready
    deactivate SCAN

    %% Recursive initialization down the operator tree (Right Child: Inner Table)
    JOIN->>SEEK: open()
    activate SEEK
    Note over SEEK: B-Tree Index initialization:<br/>Locates targetIndexID and binds look-up search pointer
    SEEK-->>JOIN: Index seek pointers ready
    deactivate SEEK

    JOIN-->>EX: Operator tree successfully opened
    deactivate JOIN

    Note over EX:The system is now locked and fully armed to trigger the dynamic Volcano streaming flow (Phase 2).
    
    EX-->>User: Acknowledge initialization success (Ready for data streaming)
    deactivate EX
```

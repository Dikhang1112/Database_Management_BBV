```mermaid
sequenceDiagram
    autonumber
    
    %% --- FIXED ARCHITECTURAL OBJECTS (TOP ROW ONLY) ---
    participant RP as TdsResultProcessor
    participant JOIN as NestedLoopJoinOperator
    participant SCAN as TableScanOperator
    participant CTX as SessionContext
    actor User as User

    %% --- WORKFLOW: VOLCANO ITERATOR STREAMING LOOP ---
    Note over RP, User: QUERY DATA STREAMING PHASE (BOTTOM-UP PIPELINE)
    
    %% The driving loop initiated by the Result Channel
    loop While JOIN.hasNext() is True
        RP->>JOIN: next()
        activate JOIN
        Note over JOIN: Join evaluation:<br/>Requests raw tuple from left/outer driving stream
        
        JOIN->>SCAN: next()
        activate SCAN
        Note over SCAN: Storage Engine interaction:<br/>Reads binary page block from disk and unpacks into a Row instance
        SCAN-->>JOIN: Return dynamic Transient Row
        deactivate SCAN
        
        Note over JOIN: Performs nested matching logical criteria.<br/>If rows match foreign key, compiles a new combined Row.
        JOIN-->>RP: Return matched Transient Row
        deactivate JOIN

        RP->>RP: formatData(Row)
        Note over RP: Converts volatile memory row<br/>into strict TDS network binary frame
        
        RP->>CTX: streamToClient(serializedBytes, session)
        activate CTX
        CTX-->>User: Flush data chunk instantly to physical socket descriptor
        deactivate CTX
        
        Note over RP: Transient Row memory reference is immediately released<br/>for garbage collection, keeping RAM flat constant
    end

    %% --- PIPELINE TEARDOWN LAYER ---
    Note over RP, CTX: PIPELINE TERMINATION (CLEANUP & UNLOCK)
    RP->>JOIN: close()
    activate JOIN
    JOIN->>SCAN: close()
    JOIN-->>RP: Memory and file handles released
    deactivate JOIN
    
    RP->>CTX: updateState("IDLE")
    activate CTX
    Note over CTX: Unlocks user session,<br/>ready for next incoming SQL command
    CTX-->>User: Final EOF/RowCount transmission complete
    deactivate CTX
```

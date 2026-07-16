sequenceDiagram
    autonumber
    
    %% --- FIXED ARCHITECTURAL OBJECTS (TOP ROW ONLY) ---
    actor User as User
    participant QP as QueryProcessor
    participant MC as ObjectMetadataCache
    participant EE as ExecutionEngine
    participant SE as StorageEngine

    %% ========================================================
    %% PHASE 1: COMPILATION & OPTIMIZATION (QUERY PROCESSOR)
    %% ========================================================
    Note over User, SE: PHASE 1: QUERY COMPILE-TIME 
    User->>QP: Send raw SQL Text string (e.g., "INSERT INTO users...")
    activate QP
    
    QP->>MC: validateAgainstCatalog()
    activate MC
    Note over MC: Module 2 (Metadata)
    MC-->>QP: Return schema validation status
    deactivate MC

    QP->>QP: Cost-Based Optimization (CBO)
    Note over QP: Computes I/O costs and creates ExecutionPlan
    
    QP-->>EE: Dispatch ExecutionPlan
    deactivate QP
    activate EE

    %% ========================================================
    %% PHASE 2: RUNTIME VALIDATION (DATABASE OBJECTS)
    %% ========================================================
    Note over EE, MC: PHASE 2: SCHEMA CONFORMITY VALIDATION 
    Note over EE: Module 3 (Execution Engine)
    
    EE->>EE: Table.validateRow(transientRow)
    Note over EE: Module 1 (Database Object)

    %% ========================================================
    %% PHASE 3: PHYSICAL STORAGE I/O (STORAGE ENGINE)
    %% ========================================================
    Note over EE, SE: PHASE 3: TRANSACTION & PHYSICAL DISK WRITE 
    EE->>SE: writeRowToDisk(validatedRow)
    activate SE
    
    Note over SE: Module 4 (Storage Engine)
    
    SE-->>EE: Write success
    deactivate SE

    EE-->>User: Return ResultSet bytes / RowCount success packet
    deactivate EE


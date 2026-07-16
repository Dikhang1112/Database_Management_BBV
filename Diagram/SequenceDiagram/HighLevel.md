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
    
    QP->>MC: validateAgainstCatalog() [Tra cứu siêu dữ liệu]
    activate MC
    Note over MC: Module 2 (Metadata Layer):<br/>Verifies if table/column exists on RAM<br/>and translates String name to int ObjectID
    MC-->>QP: Return system schema validation status
    deactivate MC

    QP->>QP: Cost-Based Optimization (CBO)
    Note over QP: Computes potential disk I/O costs<br/>and freezes an optimal physical ExecutionPlan
    
    QP-->>EE: Dispatch immutable ExecutionPlan block
    deactivate QP
    activate EE

    %% ========================================================
    %% PHASE 2: RUNTIME VALIDATION (DATABASE OBJECTS)
    %% ========================================================
    Note over EE, MC: PHASE 2: SCHEMA CONFORMITY VALIDATION 
    Note over EE: Module 3 (Execution Engine):<br/>Opens Volcano RowIterator pipes<br/>and maps dynamic variables into dynamic Row/Field
    
    EE->>EE: Table.validateRow(transientRow)
    Note over EE: Module 1 (Database Object):<br/>Loops columns metadata and activates<br/>TableConstraint rules using Strategy Pattern

    %% ========================================================
    %% PHASE 3: PHYSICAL STORAGE I/O (STORAGE ENGINE)
    %% ========================================================
    Note over EE, SE: PHASE 3: TRANSACTION & PHYSICAL DISK WRITE 
    EE->>SE: writeRowToDisk(validatedRow)
    activate SE
    
    Note over SE: Module 4 (Storage Engine):<br/>1. LockManager requests Exclusive Lock (X)<br/>2. BufferPoolManager fetches target DataPage<br/>3. DiskFileManager flushes binary block to hardware
    
    SE-->>EE: Write data in disk success
    deactivate SE

    EE-->>User: Return ResultSet bytes / RowCount success packet
    deactivate EE

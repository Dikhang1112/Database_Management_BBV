# 📚 Database Management System (BBV Assignment)

This project implements a full-fledged **Relational Database Management System (DBMS)** in **Java**, following a modular architecture with distinct layers for storage, catalog, transaction management, query processing, and execution.

---

# 🏗️ Architecture

![DBMS Architecture](./Diagram/images/Mindmap.jpg)

---

# 📐 High-Level Class Diagram

```mermaid
classDiagram
    %% Core System Engine Context
    class DatabaseCoreServerSystem

    %% Control Plane Modules
    class DatabaseCoreServer {
        <<Core Module>>
    }
    class SecurityPermission

    %% Data Plane Processing Axis
    class QueryProcessor {
        <<Core Module>>
    }
    class ExecutionEngine {
        <<Core Module>>
    }

    %% Physical Data Plane
    class StorageEngine {
        <<Core Module>>
    }
    class DurabilityData {
        <<Core Module>>
    }

    %% Peripheral Services
    class PerformanceScalability
    class Monitoring
    class Automatic

    DatabaseCoreServerSystem "1" *-- "1" DatabaseCoreServer
    DatabaseCoreServerSystem "1" *-- "1" SecurityPermission
    DatabaseCoreServerSystem "1" *-- "1" QueryProcessor
    DatabaseCoreServerSystem "1" *-- "1" ExecutionEngine
    DatabaseCoreServerSystem "1" *-- "1" StorageEngine
    DatabaseCoreServerSystem "1" *-- "1" DurabilityData
    DatabaseCoreServerSystem "1" *-- "1" PerformanceScalability
    DatabaseCoreServerSystem "1" *-- "1" Monitoring
    DatabaseCoreServerSystem "1" *-- "1" Automatic

    SecurityPermission ..> DatabaseCoreServer : Authorize
    QueryProcessor ..> DatabaseCoreServer : Validate
    QueryProcessor ..> ExecutionEngine
    ExecutionEngine ..> PerformanceScalability : Allocate
    ExecutionEngine ..> StorageEngine : Fetch
    StorageEngine ..> DatabaseCoreServer : Persist
    StorageEngine --> DurabilityData : Enforce
    DurabilityData --> StorageEngine : Write
    Automatic ..> DurabilityData : Backup
    Monitoring ..> ExecutionEngine : Track
    Monitoring ..> StorageEngine : Audit
    Monitoring ..> PerformanceScalability : Track
```

---

# 🔄 High-Level Sequence Diagram

```mermaid
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
```

---

# 📂 Project Structure

```text
src/
 ├── catalog/
 ├── execution/
 ├── query/
 ├── storage/
 ├── transaction/
 ├── security/
 └── monitoring/
```
```mermaid
classDiagram
    %% Core System Engine Context
    class DatabaseCoreServerSystem
    
    %% Control Plane Modules
    class DatabaseCoreServer {
        <<Core Module>>
    }
    class SecurityPermission 

    %% Data Plane Processing Axis (The Brain & Muscle)
    class QueryProcessor {
        <<Core Module>>
    }
    class ExecutionEngine {
        <<Core Module>>
    }

    %% Physical Data Plane Subsystems
    class StorageEngine {
        <<Core Module>>
    }
    class DurabilityData {
        <<Core Module>>
    }

    %% Peripheral Optimization & Monitoring Services
    class PerformanceScalability
    class Monitoring
    class Automatic
    class SecurityPermission 


    %% ========================================================
    %% STRUCTURAL COMPOSITION WITH MULTIPLICITY
    %% ========================================================
    DatabaseCoreServerSystem "1" *-- "1" DatabaseCoreServer
    DatabaseCoreServerSystem "1" *-- "1" SecurityPermission
    DatabaseCoreServerSystem "1" *-- "1" QueryProcessor
    DatabaseCoreServerSystem "1" *-- "1" ExecutionEngine
    DatabaseCoreServerSystem "1" *-- "1" StorageEngine
    DatabaseCoreServerSystem "1" *-- "1" DurabilityData
    DatabaseCoreServerSystem "1" *-- "1" PerformanceScalability
    DatabaseCoreServerSystem "1" *-- "1" Monitoring
    DatabaseCoreServerSystem "1" *-- "1" Automatic

    %% ========================================================
    %% ARCHITECTURAL DEPENDENCIES WITH ACTIONS & MULTIPLICITY
    %% ========================================================
    
    %% Gateway Plane Relations
    SecurityPermission "1" ..> "1" DatabaseCoreServer : Authorize
    QueryProcessor "1" ..> "1" SecurityPermission : Verify
    QueryProcessor "1" ..> "1" DatabaseCoreServer : Validate

    %% Core Execution Plane Relations
    QueryProcessor "1" ..> "1" ExecutionEngine
    ExecutionEngine "1" ..> "1" PerformanceScalability : Allocate
    ExecutionEngine "1" ..> "1" StorageEngine : Fetch

    %% Persistence Plane Relations
    StorageEngine "1" ..> "1" DatabaseCoreServer : Persist
    StorageEngine "1" --> "1" DurabilityData : Enforce
    DurabilityData "1" --> "1" StorageEngine : Write

    %% Automation & Telemetry Plane Relations
    Automatic "1" ..> "1" DurabilityData : Backup
    Monitoring "1" ..> "1" ExecutionEngine : Track
    Monitoring "1" ..> "1" StorageEngine : Audit
    Monitoring "1" ..> "1" PerformanceScalability : Track
```

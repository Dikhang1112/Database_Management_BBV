classDiagram
    %% Core System Engine Context
    class DatabaseManagementSystem
    
    %% Control Plane Modules
    class DatabaseManagement {
        <<Core Module>>
    }
    class SecurityPermission 

    %% Data Plane Processing Axis (The Brain & Muscle)
    class QueryProcessing {
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
    DatabaseManagementSystem "1" *-- "1" DatabaseManagement
    DatabaseManagementSystem "1" *-- "1" SecurityPermission
    DatabaseManagementSystem "1" *-- "1" QueryProcessing
    DatabaseManagementSystem "1" *-- "1" ExecutionEngine
    DatabaseManagementSystem "1" *-- "1" StorageEngine
    DatabaseManagementSystem "1" *-- "1" DurabilityData
    DatabaseManagementSystem "1" *-- "1" PerformanceScalability
    DatabaseManagementSystem "1" *-- "1" Monitoring
    DatabaseManagementSystem "1" *-- "1" Automatic

    %% ========================================================
    %% ARCHITECTURAL DEPENDENCIES WITH ACTIONS & MULTIPLICITY
    %% ========================================================
    
    %% Gateway Plane Relations
    SecurityPermission "1" ..> "1" DatabaseManagement : Authorize
    QueryProcessing "1" ..> "1" SecurityPermiss ion : Verify
    QueryProcessing "1" ..> "1" DatabaseManagement : Validate

    %% Core Execution Plane Relations
    QueryProcessing "1" ..> "1" ExecutionEngine
    ExecutionEngine "1" ..> "1" PerformanceScalability : Allocate
    ExecutionEngine "1" ..> "1" StorageEngine : Fetch

    %% Persistence Plane Relations
    StorageEngine "1" ..> "1" DatabaseManagement : Persist
    StorageEngine "1" --> "1" DurabilityData : Enforce
    DurabilityData "1" --> "1" StorageEngine : Write

    %% Automation & Telemetry Plane Relations
    Automatic "1" ..> "1" DurabilityData : Backup
    Monitoring "1" ..> "1" ExecutionEngine : Track
    Monitoring "1" ..> "1" StorageEngine : Audit
    Monitoring "1" ..> "1" PerformanceScalability : Track

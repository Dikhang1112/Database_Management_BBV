classDiagram
    class DatabaseManagementSystem
    class SecurityPermission
    class QueryProcessing
    class DatabaseManagerment
    class ExecutionEngine
    class PerformanceScalability
    class StorageEngine
    class DurabilityData
    class Automatic
    class Monitoring

    %% High-Level Architectural Dependencies
    DatabaseManagementSystem --* SecurityPermission
    DatabaseManagementSystem --* QueryProcessing

    DatabaseManagementSystem --> SecurityPermission : "1. Authenticate entry gateway"
    SecurityPermission ..> QueryProcessing : "2. Forward if valid"
    QueryProcessing --> DatabaseManagerment : "3. Verify Schema and Constraints"
    QueryProcessing ..> ExecutionEngine : "4. Handover ExecutionPlan"
    ExecutionEngine --> PerformanceScalability : "5. Request Memory and Threads"
    ExecutionEngine --> StorageEngine : "6. Request raw data access"
    StorageEngine --> DatabaseManagerment : "7. Persist System Catalog physically"
    StorageEngine --> DurabilityData : "8. Enforce Write-Ahead Logging (WAL)"
    DurabilityData --> StorageEngine : "9. Read/Write log (.ldf) & data (.mdf) files"
    Automatic --> DurabilityData : "Schedule automated backup jobs"
    Monitoring ..> ExecutionEngine : "Monitor Active Sessions and Processes"
    Monitoring ..> StorageEngine : "Monitor I/O pressure and Deadlocks"
    Monitoring ..> PerformanceScalability : "Monitor RAM pressure and Idle CPU"
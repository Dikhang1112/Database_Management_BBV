# DBMS High-Level System Architecture

This document provides a high-level architectural view of the Relational Database Management System (DBMS) structured and formatted into **Mermaid Flowcharts** and **Mermaid Class Diagrams** based on system modules defined in [HighLevel.md](file:///d:/BBV/Database_Management_BBV/DBMS/Diagram/ClassDiagram/HighLevel.md), [Metadata.md](file:///d:/BBV/Database_Management_BBV/DBMS/Diagram/ClassDiagram/Metadata/Metadata.md), and [QueryProcessor.md](file:///d:/BBV/Database_Management_BBV/DBMS/Diagram/ClassDiagram/QueryProcessor/QueryProcessor.md).

---

## 1. Master System Architecture

### Master System Architecture Flowchart

```mermaid
flowchart TD
    subgraph CoreServer["Database Core Server"]
        DatabaseServer["DatabaseServer"]
        SessionManager["SessionManager"]
        ConnectionManager["ConnectionManager"]
        ConfigurationManager["ConfigurationManager"]
    end

    subgraph Security["Security & Permissions"]
        AuthenticationManager["AuthenticationManager"]
        AuthorizationManager["AuthorizationManager"]
        AuditManager["AuditManager"]
    end

    subgraph QueryProc["Query Processor"]
        Lexer["Lexer"]
        SQLParser["SQLParser"]
        ASTBuilder["ASTBuilder"]
        QueryOptimizer["QueryOptimizer"]
        StatisticsManager["StatisticsManager"]
    end

    subgraph MetadataMod["Metadata Catalog"]
        CatalogManager["CatalogManager"]
        DatabaseObj["Database"]
        SchemaObj["Schema"]
        TableObj["Table"]
        ColumnObj["Column"]
        IndexObj["Index"]
        ViewObj["View"]
        DataTypeObj["DataType"]
    end

    subgraph ExecEngine["Execution Engine"]
        ExecutionPlanner["ExecutionPlanner"]
        ExecutionContext["ExecutionContext"]
        QueryExecutor["QueryExecutor"]
        subgraph Pipeline["Operator Pipeline"]
            ScanOp["ScanOperator"]
            FilterOp["FilterOperator"]
            JoinOp["JoinOperator"]
            AggOp["AggregateOperator"]
            SortOp["SortOperator"]
        end
        ResultSet["ResultSet"]
    end

    subgraph StorageEng["Storage Engine"]
        FileManager["FileManager"]
        PageManager["PageManager"]
        subgraph BufferPoolSys["Buffer Pool System"]
            BufferPoolMgr["BufferPoolManager"]
            BufferPool["BufferPool"]
            BufferFrame["BufferFrame"]
            DataPage["DataPage"]
        end
        PageReplacer["PageReplacer / ClockPageReplacer"]
        LockManager["LockManager (LockTable)"]
        TransactionManager["TransactionManager"]
    end

    subgraph Durability["Durability & Recovery"]
        WALManager["WALManager"]
        CheckpointManager["CheckpointManager"]
        RecoveryManager["RecoveryManager"]
    end

    subgraph Perf["Performance & Scalability"]
        CacheManager["CacheManager"]
        MemoryManager["MemoryManager"]
    end

    subgraph MonAuto["Monitoring & Automation"]
        MetricsCollector["MetricsCollector"]
        AutoVacuum["AutoVacuum"]
    end

    %% Data & Control Flows
    ConnectionManager --> DatabaseServer
    DatabaseServer --> SessionManager
    SessionManager --> AuthenticationManager
    AuthenticationManager --> AuthorizationManager
    AuthorizationManager -.-> CatalogManager
    SessionManager --> Lexer
    Lexer --> SQLParser --> ASTBuilder --> QueryOptimizer
    QueryOptimizer <--> StatisticsManager
    QueryOptimizer -.-> CatalogManager
    QueryOptimizer --> ExecutionPlanner
    ExecutionPlanner --> QueryExecutor
    QueryExecutor --> ExecutionContext
    QueryExecutor --> Pipeline
    Pipeline --> BufferPoolMgr
    QueryExecutor --> TransactionManager
    QueryExecutor --> LockManager
    TransactionManager --> WALManager
    CheckpointManager --> BufferPoolMgr
    RecoveryManager --> FileManager
    BufferPoolMgr --> BufferPool --> BufferFrame --> DataPage
    BufferPoolMgr --> PageReplacer
    BufferPoolMgr --> PageManager --> FileManager
    Pipeline --> ResultSet
```

### Master System Architecture Class Diagram

```mermaid
classDiagram
    direction TD

%% =========================================================
%% SYSTEM MODULE BOUNDARIES
%% =========================================================
    class DatabaseCoreServer {
        <<Module>>
    }

    class QueryProcessor {
        <<Module>>
    }

    class ExecutionEngine {
        <<Module>>
    }

    class StorageEngine {
        <<Module>>
    }

    class Metadata {
        <<Module>>
    }

    class DurabilityData {
        <<Module>>
    }

    class SecurityPermission {
        <<Module>>
    }

    class PerformanceScalability {
        <<Module>>
    }

    class Monitoring {
        <<Module>>
    }

    class Automation {
        <<Module>>
    }

%% =========================================================
%% METADATA MODULE INNER COMPONENTS
%% =========================================================
    class CatalogManager
    class Database
    class Schema
    class Table
    class Column
    class Index
    class View
    class DataType{
        <<Enumeration>>
    }

%% =========================================================
%% DATABASE CORE SERVER INNER COMPONENTS
%% =========================================================
    class DatabaseServer
    class SessionManager
    class ConnectionManager
    class ConfigurationManager

%% =========================================================
%% QUERY PROCESSOR INNER COMPONENTS
%% =========================================================
    class Lexer
    class SQLParser
    class ASTBuilder
    class QueryOptimizer
    class StatisticsManager

%% =========================================================
%% EXECUTION ENGINE INNER COMPONENTS
%% =========================================================
    class ExecutionPlanner
    class ExecutionContext
    class QueryExecutor
    class OperatorPipeline
    class ResultSet

    class ScanOperator
    class FilterOperator
    class JoinOperator
    class AggregateOperator
    class SortOperator

%% =========================================================
%% STORAGE ENGINE INNER COMPONENTS
%% =========================================================
    class FileManager
    class PageManager
    class BufferPoolManager
    class BufferPool
    class BufferFrame

    class PageReplacer{
        <<Interface>>
    }

    class ClockPageReplacer

    class LockManager
    class LockTable
    class TransactionManager
    class DataPage

%% =========================================================
%% DURABILITY INNER COMPONENTS
%% =========================================================
    class WALManager
    class CheckpointManager
    class RecoveryManager

%% =========================================================
%% SECURITY INNER COMPONENTS
%% =========================================================
    class AuthenticationManager
    class AuthorizationManager
    class AuditManager

%% =========================================================
%% PERFORMANCE INNER COMPONENTS
%% =========================================================
    class CacheManager
    class MemoryManager

%% =========================================================
%% MONITORING INNER COMPONENTS
%% =========================================================
    class MetricsCollector

%% =========================================================
%% AUTOMATION INNER COMPONENTS
%% =========================================================
    class AutoVacuum

%% =========================================================
%% MODULE COMPOSITION
%% =========================================================

%% Metadata Module Composition
    Metadata *-- CatalogManager
    Metadata *-- Database
    Metadata *-- Schema
    Metadata *-- Table
    Metadata *-- Column
    Metadata *-- Index
    Metadata *-- View
    Metadata *-- DataType

    CatalogManager *-- Database
    Database *-- Schema
    Schema *-- Table
    Schema *-- View
    Table *-- Column
    Table *-- Index
    Column --> DataType

%% Database Core Server Composition
    DatabaseCoreServer *-- DatabaseServer
    DatabaseCoreServer *-- SessionManager
    DatabaseCoreServer *-- ConnectionManager
    DatabaseCoreServer *-- ConfigurationManager

%% Query Processor Composition
    QueryProcessor *-- Lexer
    QueryProcessor *-- SQLParser
    QueryProcessor *-- ASTBuilder
    QueryProcessor *-- QueryOptimizer
    QueryProcessor *-- StatisticsManager

%% Execution Engine Composition
    ExecutionEngine *-- ExecutionPlanner
    ExecutionEngine *-- ExecutionContext
    ExecutionEngine *-- QueryExecutor
    ExecutionEngine *-- OperatorPipeline
    ExecutionEngine *-- ResultSet

    OperatorPipeline *-- ScanOperator
    OperatorPipeline *-- FilterOperator
    OperatorPipeline *-- JoinOperator
    OperatorPipeline *-- AggregateOperator
    OperatorPipeline *-- SortOperator

%% Storage Engine Composition
    StorageEngine *-- FileManager
    StorageEngine *-- PageManager
    StorageEngine *-- BufferPoolManager
    StorageEngine *-- LockManager
    StorageEngine *-- TransactionManager

    BufferPoolManager --> BufferPool
    BufferPool *-- BufferFrame
    BufferFrame *-- DataPage

    PageReplacer <|.. ClockPageReplacer
    BufferPoolManager --> PageReplacer

    LockManager --> LockTable

%% Infrastructure Modules Compositions
    DurabilityData *-- WALManager
    DurabilityData *-- CheckpointManager
    DurabilityData *-- RecoveryManager

    SecurityPermission *-- AuthenticationManager
    SecurityPermission *-- AuthorizationManager
    SecurityPermission *-- AuditManager

    PerformanceScalability *-- CacheManager
    PerformanceScalability *-- MemoryManager

    Monitoring *-- MetricsCollector

    Automation *-- AutoVacuum

%% =========================================================
%% CROSS MODULE DEPENDENCIES
%% =========================================================

    DatabaseServer --> SessionManager
    SessionManager --> QueryProcessor

%% Query Processor interacts with Metadata Module instead of loose inner objects
    QueryProcessor ..> Metadata : "Validates Schemas & Tables via Catalog"
    QueryOptimizer --> StatisticsManager

    QueryProcessor --> ExecutionEngine

    QueryExecutor --> OperatorPipeline
    OperatorPipeline --> BufferPoolManager

    QueryExecutor --> TransactionManager
    QueryExecutor --> LockManager

    TransactionManager --> WALManager

    CheckpointManager --> BufferPoolManager

    RecoveryManager --> FileManager

%% Security & Permissions link to Metadata
    AuthorizationManager ..> Metadata : "Verifies Permissions on Tables"
```

---

## 2. Subsystem & Module Breakdown

Detailed breakdown including Mermaid flowchart and Mermaid class diagram for each module defined in the architecture.

### 2.1. Database Core Server Module (`DatabaseCoreServer`)
Manages client connections, user session contexts, server lifecycle, and global system configurations.

#### Core Server Flowchart
```mermaid
flowchart LR
    subgraph DatabaseCoreServer["Database Core Server"]
        ConnectionManager["ConnectionManager\n(Client Connections & Networking)"]
        DatabaseServer["DatabaseServer\n(Main Entrypoint & Server Lifecycle)"]
        SessionManager["SessionManager\n(User Context & Session Scope)"]
        ConfigurationManager["ConfigurationManager\n(Global System Configs)"]

        ConnectionManager --> DatabaseServer
        DatabaseServer --> SessionManager
        DatabaseServer -.-> ConfigurationManager
    end
```

---

### 2.2. Query Processor Module (`QueryProcessor`)
Handles tokenization (`Lexer`), parsing (`SQLParser`), AST generation (`ASTBuilder`), cost estimations (`StatisticsManager`), and logical/physical query optimization (`QueryOptimizer`).

#### Query Processor Flowchart
```mermaid
flowchart LR
    subgraph QueryProcessor["Query Processor"]
        SQL["SQL Query Text"] --> Lexer["Lexer\n(Tokenization)"]
        Lexer --> SQLParser["SQLParser\n(Syntax Parsing)"]
        SQLParser --> ASTBuilder["ASTBuilder\n(Abstract Syntax Tree)"]
        ASTBuilder --> QueryOptimizer["QueryOptimizer\n(Cost & Rule Optimization)"]
        StatisticsManager["StatisticsManager\n(Table Statistics)"] <--> QueryOptimizer
        QueryOptimizer --> PhysicalPlan["Optimized Execution Plan"]
    end
```

#### Query Processor Class Diagram
```mermaid
classDiagram
    direction TD

    %% =====================================================
    %% QUERY PROCESSOR MODULE (PURE DML COMPILER)
    %% =====================================================
    class QueryProcessor{
        <<Module>>
        +executeIngestion(String sqlText) PhysicalPlan
    }

    %% =====================================================
    %% LEXICAL ANALYSIS DIVISION
    %% =====================================================
    class Lexer{
        +tokenize(String sqlText) TokenStream
        -scanIdentifier() Token
        -scanNumber() Token
    }

    class TokenStream{
        +hasNext() boolean
        +consume() Token
        +lookAhead() Token
    }

    class Token{
        +getType() TokenType
        +getValue() String
    }

    %% =====================================================
    %% SYNTAX ANALYSIS DIVISION
    %% =====================================================
    class SQLParser{
        +parse(TokenStream stream) ParseTree
        -parseSelectClause() ParseTreeNode
        -parseWhereClause() ParseTreeNode
    }

    class ParseTree{
        +getRootNode() ParseTreeNode
        +toDebugString() String
    }

    %% =====================================================
    %% AST CONSTRUCTION DIVISION
    %% =====================================================
    class ASTBuilder{
        +buildAST(ParseTree parseTree) AST
        -mapToLogicalNode(ParseTreeNode node) ASTNode
    }

    class AST{
        +getRootASTNode() ASTNode
        +traverseTree() void
    }

    %% =====================================================
    %% SEMANTIC ANALYSIS DIVISION
    %% =====================================================
    class SemanticAnalyzer{
        +validate(AST ast) boolean
    }

    class NameResolver{
        +resolveIdentifiers(ASTNode node) void
    }

    class TypeChecker{
        +checkTypeConformity(ASTNode node) void
    }

    %% =====================================================
    %% QUERY OPTIMIZER DIVISION (CBO ENGINE)
    %% =====================================================
    class QueryOptimizer{
        +generateLogicalPlan(AST ast) LogicalPlan
        +optimize(LogicalPlan plan) PhysicalPlan
    }

    class LogicalPlan{
        +applyRule(String ruleName) void
        +getLogicalRoot() LogicalPlanNode
    }

    class PhysicalPlan{
        +getPhysicalRoot() PhysicalPlanNode
        +getEstimatedCost() double
    }

    class CostEstimator{
        +calculateIoCost(LogicalPlanNode node) double
        +calculateCpuCost(LogicalPlanNode node) double
    }

    class PlanGenerator{
        +enumeratePhysicalPlans(LogicalPlan lPlan) List<PhysicalPlan>
        +selectBestPlan(List<PhysicalPlan> plans) PhysicalPlan
    }

    class StatisticsManager{
        +estimateCardinality(String tableName) long
        +estimateSelectivity(String column, String value) double
    }

    %% =====================================================
    %% EXTERNAL CONTEXT BOUNDARIES (SHARED SYSTEM DOMAINS)
    %% =====================================================
    class MetadataModule{
        <<External Module>>
        +fetchTableSchema(String table) TableSchema
        +checkTableExists(String table) boolean
        +checkColumnExists(String table, String col) boolean
    }

    class ExecutionEngine{
        <<External Module>>
        +executePhysicalPlan(PhysicalPlan plan) ResultSet
    }

    %% =====================================================
    %% INTERNAL STRUCTURAL COMPOSITIONS
    %% =====================================================
    QueryProcessor *-- Lexer : Owns Tokenizer Engine
    QueryProcessor *-- SQLParser : Owns Grammar Validator
    QueryProcessor *-- ASTBuilder : Owns Structural Tree Generator
    QueryProcessor *-- SemanticAnalyzer : Owns Logical Domain Validator
    QueryProcessor *-- QueryOptimizer : Owns Plan Transformer
    QueryProcessor *-- StatisticsManager : Owns Data Density Estimator

    %% =====================================================
    %% INTERNAL PIPELINE DATAFLOWS
    %% =====================================================
    Lexer --> TokenStream : Produces Sequential Stream
    TokenStream *-- Token : Encapsulates Value Tokens

    SQLParser --> TokenStream : Consumes Stream Tokens
    SQLParser --> ParseTree : Generates Concrete Syntax Tree

    ASTBuilder --> ParseTree : Consumes Abstract Trees
    ASTBuilder --> AST : Transforms to Abstract Syntax Tree

    SemanticAnalyzer --> AST : Inspects Node Nodes
    SemanticAnalyzer *-- NameResolver : Delegates Structural Binding
    SemanticAnalyzer *-- TypeChecker : Delegates Data Type Verification

    QueryOptimizer --> LogicalPlan : Caches Structural Operations
    QueryOptimizer *-- CostEstimator : Delegates CPU/IO Resource Computation
    QueryOptimizer *-- PlanGenerator : Delegates Physical Execution Path Evaluation

    PlanGenerator --> PhysicalPlan : Emits Best Cost Plan Matrix
    CostEstimator --> StatisticsManager : Fetches System Structural Cardinality Matrix

    %% =====================================================
    %% EXTERNAL CROSS-PLANE DEPENDENCIES
    %% =====================================================
    NameResolver ..> MetadataModule : Syncs Logical Identifiers with Physical Table Catalog
    TypeChecker ..> MetadataModule : Syncs Data Field Properties with Physical Field Catalog
    QueryOptimizer ..> MetadataModule : Inspects B-Plus Tree Index Configuration Metrics
    PhysicalPlan --> ExecutionEngine : Dispatches Compiled Execution Vectors to Database Kernel CPU
```

---

### 2.3. Execution Engine Module (`ExecutionEngine`)
Receives physical execution plans (`ExecutionPlanner`), manages execution context (`ExecutionContext`), executes operator pipelines (`QueryExecutor`, `OperatorPipeline`), and formats output streams (`ResultSet`).

#### Execution Engine Flowchart
```mermaid
flowchart TD
    subgraph ExecutionEngine["Execution Engine"]
        ExecutionPlanner["ExecutionPlanner"] --> QueryExecutor["QueryExecutor"]
        QueryExecutor --> ExecutionContext["ExecutionContext"]
        
        subgraph Pipeline["Operator Pipeline"]
            direction LR
            ScanOperator["ScanOperator"] --> FilterOperator["FilterOperator"]
            FilterOperator --> JoinOperator["JoinOperator"]
            JoinOperator --> AggregateOperator["AggregateOperator"]
            AggregateOperator --> SortOperator["SortOperator"]
        end

        QueryExecutor --> Pipeline
        Pipeline --> ResultSet["ResultSet\n(Output Stream)"]
    end
```

---

### 2.4. Storage Engine Module (`StorageEngine`)
Manages physical data files (`FileManager`), page frames (`PageManager`, `DataPage`), buffer replacement strategies (`BufferPoolManager`, `BufferPool`, `BufferFrame`, `PageReplacer`, `ClockPageReplacer`), concurrency locks (`LockManager`, `LockTable`), and ACID transactions (`TransactionManager`).

#### Storage Engine Flowchart
```mermaid
flowchart TD
    subgraph StorageEngine["Storage Engine"]
        TransactionManager["TransactionManager\n(ACID Control)"]
        LockManager["LockManager\n(Concurrency Control)"] --> LockTable["LockTable"]

        subgraph BufferSystem["Buffer Management System"]
            BufferPoolManager["BufferPoolManager"] --> BufferPool["BufferPool"]
            BufferPool --> BufferFrame["BufferFrame"]
            BufferFrame --> DataPage["DataPage"]
            BufferPoolManager --> ClockPageReplacer["ClockPageReplacer\n(PageReplacer Impl)"]
        end

        PageManager["PageManager\n(Page Allocation)"]
        FileManager["FileManager\n(Disk I/O Operations)"]

        BufferPoolManager --> PageManager
        PageManager --> FileManager
    end
```

---

### 2.5. Metadata Module (`Metadata`)
Encapsulates database objects, schemas, tables, columns, indexes, views, and data types (`CatalogManager`, `Database`, `Schema`, `Table`, `Column`, `Index`, `View`, `DataType`).

#### Metadata Flowchart
```mermaid
flowchart TD
    subgraph Metadata["Metadata Catalog"]
        CatalogManager["CatalogManager"] --> Database["Database"]
        Database --> Schema["Schema"]
        Schema --> Table["Table"]
        Schema --> View["View"]
        Table --> Column["Column"]
        Table --> Index["Index"]
        Column --> DataType["DataType\n(Enumeration)"]
    end
```

#### Metadata Class Diagram
```mermaid
classDiagram
    direction TD

    %% =====================================================
    %% METADATA MODULE
    %% =====================================================

    class MetadataModule{
        <<Module>>
    }

    %% =====================================================
    %% CATALOG
    %% =====================================================

    class CatalogManager{
        createDatabase(String databaseName) Database
        dropDatabase(String databaseName)
        getDatabase(String databaseName) Database
        containsDatabase(String databaseName) boolean
        listDatabases() List~Database~
    }

    class Database{
        createSchema(String schemaName) Schema
        dropSchema(String schemaName)
        getSchema(String schemaName) Schema
        listSchemas() List~Schema~
    }

    class Schema{
        createTable(String tableName) Table
        dropTable(String tableName)
        getTable(String tableName) Table

        createView(String viewName,String sql) View
        createSequence(String sequenceName) Sequence
        createTrigger(String triggerName) Trigger
        createProcedure(String procedureName) StoredProcedure
        createFunction(String functionName) Function

        listTables() List~Table~
        listViews() List~View~
    }

    %% =====================================================
    %% TABLE
    %% =====================================================

    class Table{
        addColumn(Column column)
        removeColumn(String columnName)

        addConstraint(Constraint constraint)
        removeConstraint(String constraintName)

        addIndex(Index index)
        removeIndex(String indexName)

        getColumn(String columnName) Column
        getIndex(String indexName) Index

        listColumns() List~Column~
        listIndexes() List~Index~
        listConstraints() List~Constraint~
    }

    class Column{
        rename(String columnName)
        changeDataType(DataType dataType)
        setNullable(boolean nullable)
        setDefaultValue(String value)
    }

    class DataType{
        <<Enumeration>>
    }

    %% =====================================================
    %% INDEX
    %% =====================================================

    class Index{
        rebuild()
        enable()
        disable()
    }

    %% =====================================================
    %% VIEW
    %% =====================================================

    class View{
        compile()
        refresh()
        updateDefinition(String sql)
    }

    %% =====================================================
    %% CONSTRAINT
    %% =====================================================

    class Constraint{
        validate()
        enable()
        disable()
    }

    class PrimaryKeyConstraint{
    }

    class ForeignKeyConstraint{
        validateReference()
    }

    class UniqueConstraint{
    }

    class CheckConstraint{
        evaluate()
    }

    %% =====================================================
    %% SEQUENCE
    %% =====================================================

    class Sequence{
        nextValue() long
        currentValue() long
        reset(long value)
    }

    %% =====================================================
    %% TRIGGER
    %% =====================================================

    class Trigger{
        enable()
        disable()
        compile()
    }

    %% =====================================================
    %% STORED PROCEDURE
    %% =====================================================

    class StoredProcedure{
        compile()
        execute()
    }

    %% =====================================================
    %% FUNCTION
    %% =====================================================

    class Function{
        compile()
        execute()
    }

    %% =====================================================
    %% MODULE COMPOSITION
    %% =====================================================

    MetadataModule *-- CatalogManager
    MetadataModule *-- Database
    MetadataModule *-- Schema
    MetadataModule *-- Table
    MetadataModule *-- Column
    MetadataModule *-- Index
    MetadataModule *-- View
    MetadataModule *-- Constraint
    MetadataModule *-- Sequence
    MetadataModule *-- Trigger
    MetadataModule *-- StoredProcedure
    MetadataModule *-- Function

    %% =====================================================
    %% CATALOG TREE
    %% =====================================================

    CatalogManager "1" *-- "*" Database

    Database "1" *-- "*" Schema

    Schema "1" *-- "*" Table
    Schema "1" *-- "*" View
    Schema "1" *-- "*" Sequence
    Schema "1" *-- "*" Trigger
    Schema "1" *-- "*" StoredProcedure
    Schema "1" *-- "*" Function

    Table "1" *-- "*" Column
    Table "1" *-- "*" Index
    Table "1" *-- "*" Constraint

    Column --> DataType

    %% =====================================================
    %% INHERITANCE
    %% =====================================================

    Constraint <|-- PrimaryKeyConstraint
    Constraint <|-- ForeignKeyConstraint
    Constraint <|-- UniqueConstraint
    Constraint <|-- CheckConstraint
```

---

### 2.6. Durability & Recovery Data Module (`DurabilityData`)
Ensures write-ahead logging (`WALManager`), dirty page synchronization (`CheckpointManager`), and system recovery post-crash (`RecoveryManager`).

#### Durability & Recovery Flowchart
```mermaid
flowchart LR
    subgraph DurabilityData["Durability & Recovery"]
        WALManager["WALManager\n(Write-Ahead Logging)"]
        CheckpointManager["CheckpointManager\n(Dirty Page Flushing)"]
        RecoveryManager["RecoveryManager\n(Crash Recovery Procedure)"]

        WALManager -.-> CheckpointManager
        CheckpointManager -.-> RecoveryManager
    end
```

---

### 2.7. Security & Permissions Module (`SecurityPermission`)
Handles user authentication (`AuthenticationManager`), object permission verification (`AuthorizationManager`), and action logging (`AuditManager`).

#### Security & Permissions Flowchart
```mermaid
flowchart LR
    subgraph SecurityPermission["Security & Permissions"]
        AuthenticationManager["AuthenticationManager\n(Identity Verification)"]
        AuthorizationManager["AuthorizationManager\n(Access Rights Verification)"]
        AuditManager["AuditManager\n(Security Logging)"]

        AuthenticationManager --> AuthorizationManager
        AuthorizationManager --> AuditManager
    end
```

---

### 2.8. Performance & Scalability Module (`PerformanceScalability`)
Provides memory allocations and result caching layers (`CacheManager`, `MemoryManager`).

#### Performance & Scalability Flowchart
```mermaid
flowchart LR
    subgraph PerformanceScalability["Performance & Scalability"]
        CacheManager["CacheManager\n(Query Result & Metadata Cache)"]
        MemoryManager["MemoryManager\n(Memory Pools & Allocations)"]

        MemoryManager --> CacheManager
    end
```

---

### 2.9. Monitoring Module (`Monitoring`)
Collects internal performance metrics and diagnostic indicators (`MetricsCollector`).

#### Monitoring Flowchart
```mermaid
flowchart LR
    subgraph Monitoring["Monitoring"]
        MetricsCollector["MetricsCollector\n(System Metrics & Telemetry)"]
    end
```

---

### 2.10. Automation Module (`Automation`)
Manages automated maintenance tasks, background cleanup, and dead tuple compaction (`AutoVacuum`).

#### Automation Flowchart
```mermaid
flowchart LR
    subgraph Automation["Automation"]
        AutoVacuum["AutoVacuum\n(Background Cleanup & Compaction)"]
    end
```

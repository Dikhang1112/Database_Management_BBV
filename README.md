# 📚 Database Management System (BBV Assignment)

This project implements a full-fledged **Relational Database Management System (DBMS)** in **Java**, following a modular architecture with distinct layers for storage, catalog, transaction management, query processing, and execution.

---

#  Class diagram overview

``` mermaid
classDiagram
    direction TD
    %% =========================================================
    %% SYSTEM MODULE BOUNDARIES
    %% =========================================================
    class DatabaseCoreServer { <<Module>> }
    class QueryProcessor { <<Module>> }
    class ExecutionEngine { <<Module>> }
    class StorageEngine { <<Module>> }
    class DurabilityData { <<Module>> }
    class SecurityPermission { <<Module>> }
    class PerformanceScalability { <<Module>> }
    class Monitoring { <<Module>> }
    class Automation { <<Module>> }

    %% =========================================================
    %% 1. SYSTEM CATALOG & METADATA (SHARED LOGICAL DOMAIN)
    %% =========================================================
    class CatalogManager {
        -Map<String,Database> databaseRegistry
        +getDatabase(String name) Database
    }
    class Database {
        -String dbName
        -Map<String,Schema> schemas
    }
    class Schema {
        -String schemaName
        -Map<String,Table> tables
        -Map<String,View> views
    }
    class Table {
        -int tableID
        -String tableName
        -List<Column> columns
        -List<Index> indexes
    }
    class Column {
        -int columnID
        -String columnName
        -DataType dataType
        -boolean isPrimaryKey
    }
    class Index {
        -int indexID
        -String indexName
        -String indexType
    }
    class View {
        -String viewName
        -String definitionSql
    }
    class DataType {
        <<enumeration>>
        INT
        VARCHAR
        DATETIME
    }

    %% =========================================================
    %% 2. DATABASE CORE SERVER
    %% =========================================================
    class DatabaseServer
    class SessionManager
    class ConnectionManager
    class ConfigurationManager

    %% =========================================================
    %% 3. QUERY PROCESSOR (DML COMPILER)
    %% =========================================================
    class SQLParser
    class Lexer
    class ASTBuilder
    class QueryOptimizer
    class StatisticsManager

    %% =========================================================
    %% 4. EXECUTION ENGINE (RELATIONAL OPERATORS)
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
    %% 5. STORAGE ENGINE (PHYSICAL PERSISTENCE)
    %% =========================================================
    class FileManager
    class PageManager
    class BufferPoolManager
    class BufferPool
    class BufferFrame
    class PageReplacer { <<Interface>> }
    class ClockPageReplacer
    class LockManager
    class LockTable
    class TransactionManager
    class DataPage

    %% =========================================================
    %% 6. AUXILIARY SUBSYSTEMS (DURABILITY, SECURITY, ETC.)
    %% =========================================================
    class WALManager
    class CheckpointManager
    class RecoveryManager
    class AuthenticationManager
    class AuthorizationManager
    class AuditManager
    class CacheManager
    class MemoryManager
    class MetricsCollector
    class AutoVacuum

    %% =========================================================
    %% COMPOSITION & INHERITANCE (MODULE INNER-STRUCTURE)
    %% =========================================================
    
    %% Shared Metadata Tree
    CatalogManager "1" *-- "*" Database
    Database "1" *-- "*" Schema
    Schema "1" *-- "*" Table
    Schema "1" *-- "*" View
    Table "1" *-- "*" Column
    Table "1" *-- "*" Index
    Column --> DataType

    %% Module 1: Core Server
    DatabaseCoreServer *-- DatabaseServer
    DatabaseCoreServer *-- SessionManager
    DatabaseCoreServer *-- ConnectionManager
    DatabaseCoreServer *-- ConfigurationManager

    %% Module 2: Query Processor
    QueryProcessor *-- SQLParser
    QueryProcessor *-- Lexer
    QueryProcessor *-- ASTBuilder
    QueryProcessor *-- QueryOptimizer
    QueryProcessor *-- StatisticsManager

    %% Module 3: Execution Engine
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

    %% Module 4: Storage Engine
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

    %% Supporting Modules Composition
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
    %% CROSS-MODULE INTER-DEPENDENCIES (THE SYSTEM FLOW)
    %% =========================================================
    DatabaseServer --> SessionManager : Validates
    SessionManager --> QueryProcessor : Passes SQL
    
    %% Nút thắt ranh giới: Query Processor mượn Catalog để Validate logic
    QueryProcessor ..> CatalogManager : Reads Metadata Schema
    QueryOptimizer --> StatisticsManager : Requests Data Density Cost
    
    %% Luồng kích hoạt thực thi
    QueryProcessor --> ExecutionEngine : Dispatches Physical Plan
    QueryExecutor --> OperatorPipeline : Drives Volcano Pull
    
    %% Tầng thực thi chọc xuống lưu trữ vật lý
    OperatorPipeline --> BufferPoolManager : Fetches Data Pages
    QueryExecutor --> TransactionManager : Begins/Commits Transaction
    QueryExecutor --> LockManager : Requests Logical Locks
    
    %% Đồng bộ Transaction Log bền vững
    TransactionManager --> WALManager : Forces Append-Only Log Flush
    CheckpointManager --> BufferPoolManager : Flushes Dirty RAM Pages
    RecoveryManager --> FileManager : Replays WAL Logs on Crash
    
    %% Phân quyền mức bảng
    AuthorizationManager ..> Table : Verifies Access Rights

```
---

# DBMS Class Diagrams

This document contains the comprehensive class diagrams for the Database Management System (DBMS), including the overall System Architecture Overview, the Metadata Subsystem, the Query Processor Module, and their detailed sub-component diagrams.

---

## 1. System Architecture Overview Class Diagram

The following Mermaid class diagram illustrates the high-level system module boundaries, inner components, composition relationships, and cross-module interaction flows across the entire DBMS.

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
    class CatalogManager {
        <<Singleton / Composite Node>>
    }
    class Database {
        <<Composite Node / State Context>>
    }
    class DatabaseStatus {
        <<Enumeration>>
    }
    class Schema {
        <<Composite Node / Factory Method>>
    }
    class Table {
        <<Composite Node / Prototype / Memento / Subject>>
    }
    class Column {
        <<Composite Leaf / Prototype>>
    }
    class DataType {
        <<Enumeration>>
    }
    class Index {
        <<Strategy Context>>
    }
    class Constraint {
        <<Abstract / Template Method>>
    }
    class PrimaryKeyConstraint
    class ForeignKeyConstraint
    class UniqueConstraint
    class CheckConstraint

    class MetadataElement {
        <<Interface - Composite>>
    }
    class DDLCommand {
        <<Interface - Command>>
    }
    class TableMemento {
        <<Memento>>
    }
    class MetadataChangeListener {
        <<Interface - Observer>>
    }
    class IndexRebuildStrategy {
        <<Interface - Strategy>>
    }
    class ConstraintFactory {
        <<Factory Method>>
    }
    class ColumnBuilder {
        <<Builder>>
    }

%% =========================================================
%% QUERY PROCESSOR INNER COMPONENTS
%% =========================================================
    class Lexer {
        <<Chain of Responsibility>>
    }
    class SQLParser {
        <<Chain of Responsibility>>
    }
    class ASTBuilder {
        <<Chain of Responsibility>>
    }
    class SemanticAnalyzer {
        <<Visitor>>
    }
    class QueryRewriter {
        <<Visitor>>
    }
    class QueryOptimizer {
        <<Strategy Context>>
    }
    class PlanGenerator {
        <<Factory Method>>
    }

%% =========================================================
%% COMPOSITION & AGGREGATION RELATIONSHIPS
%% =========================================================
    CatalogManager "1" *-- "*" Database : Owns Databases
    Database "1" *-- "*" Schema : Owns Schemas
    Schema "1" *-- "*" Table : Owns Tables
    Table "1" *-- "*" Column : Owns Columns
    Table "1" *-- "*" Index : Owns Indexes
    Table "1" *-- "*" Constraint : Owns Constraints

    Database --> DatabaseStatus : Tracks State
    Column --> DataType : Defines Type

    Constraint <|-- PrimaryKeyConstraint
    Constraint <|-- ForeignKeyConstraint
    Constraint <|-- UniqueConstraint
    Constraint <|-- CheckConstraint

    MetadataElement <|.. CatalogManager
    MetadataElement <|.. Database
    MetadataElement <|.. Schema
    MetadataElement <|.. Table
    MetadataElement <|.. Column

    Index --> IndexRebuildStrategy : Delegates Rebuild
    Table ..> TableMemento : Creates/Restores
    Schema ..> ConstraintFactory : Uses
    ColumnBuilder ..> Column : Builds

    QueryProcessor *-- Lexer : Owns Lexer
    QueryProcessor *-- SQLParser : Owns Parser
    QueryProcessor *-- ASTBuilder : Owns ASTBuilder
    QueryProcessor *-- SemanticAnalyzer : Owns SemanticAnalyzer
    QueryProcessor *-- QueryRewriter : Owns QueryRewriter
    QueryProcessor *-- QueryOptimizer : Owns QueryOptimizer
    QueryProcessor *-- PlanGenerator : Owns PlanGenerator

%% =========================================================
%% CROSS-MODULE INTERACTION FLOWS
%% =========================================================
    DatabaseCoreServer --> SecurityPermission : Authenticates & Authorizes
    DatabaseCoreServer --> QueryProcessor : Passes SQL Query Text
    QueryProcessor --> Metadata : Reads Schema & Validates Symbols
    QueryProcessor --> ExecutionEngine : Dispatches Physical Plan
    ExecutionEngine --> StorageEngine : Executes Records IO
    ExecutionEngine --> DurabilityData : Logs WAL Transactions
    DatabaseCoreServer --> Monitoring : Emits Metrics & Logs
    PerformanceScalability --> StorageEngine : Optimizes Caching & Indexing
    Automation --> DatabaseCoreServer : Triggers Maintenance Tasks
```

---

## 2. Metadata Subsystem Class Diagram

The Metadata Subsystem Class Diagram illustrates the structural hierarchy, domain entities, management components, design patterns, and DDL command representations for managing database metadata.

```mermaid
classDiagram
    direction TD

    %% =====================================================
    %% METADATA MODULE & FACADE
    %% =====================================================

    class Metadata{
        <<Module / Facade>>
        +getInstance() Metadata
        +getTable(String dbName, String schemaName, String tableName) Table
        +executeDDL(DDLCommand command)
    }

    %% =====================================================
    %% CORE CATALOG TREE & DOMAIN ENTITIES
    %% =====================================================

    class CatalogManager{
        <<Singleton / Composite Node>>
        +getInstance() CatalogManager$
        +createDatabase(String databaseName) Database
        +dropDatabase(String databaseName)
        +getDatabase(String databaseName) Database
        +containsDatabase(String databaseName) boolean
        +listDatabases() List~Database~
        +getElementName() String
    }

    class Database{
        <<Composite Node / State Context>>
        +createSchema(String schemaName) Schema
        +dropSchema(String schemaName)
        +getSchema(String schemaName) Schema
        +setStatus(DatabaseStatus status)
        +getElementName() String
    }

    class Schema{
        <<Composite Node / Factory Method>>
        +createTable(String tableName) Table
        +dropTable(String tableName)
        +getTable(String tableName) Table
        +getElementName() String
    }

    class Table{
        <<Composite Node / Prototype / Memento / Subject>>
        +addColumn(Column column)
        +removeColumn(String columnName)
        +addConstraint(Constraint constraint)
        +addIndex(Index index)
        +createMemento() TableMemento
        +restore(TableMemento memento)
        +clone() Table
        +registerListener(MetadataChangeListener listener)
        +notifyListeners(String eventType, String targetName)
        +getElementName() String
    }

    class Column{
        <<Composite Leaf / Prototype>>
        +changeDataType(DataType dataType)
        +clone() Column
        +getElementName() String
    }

    class DataType{
        <<Enumeration>>
        INT
        VARCHAR
        BIGINT
        BOOLEAN
        TIMESTAMP
    }

    class Index{
        <<Strategy Context>>
        +setRebuildStrategy(IndexRebuildStrategy strategy)
        +rebuild()
        +enable()
        +disable()
    }

    class Constraint{
        <<Abstract / Template Method>>
        +validate() boolean
        #preValidate() boolean
        #doValidate() boolean*
        #postValidate(boolean result)
    }

    class PrimaryKeyConstraint{
        #doValidate() boolean
    }

    class ForeignKeyConstraint{
        +validateReference() boolean
        #doValidate() boolean
    }

    class UniqueConstraint{
        #doValidate() boolean
    }

    class CheckConstraint{
        #doValidate() boolean
    }

    class DatabaseStatus{
        <<Enumeration>>
        ONLINE
        OFFLINE
        READ_ONLY
    }

    %% =====================================================
    %% DESIGN PATTERN INFRASTRUCTURE & INTERFACES
    %% =====================================================

    class MetadataElement{
        <<Interface - Composite>>
        +getElementName() String*
    }

    class MetadataChangeListener{
        <<Interface - Observer>>
        +onMetadataChanged(String eventType, String targetName)*
    }

    class DDLCommand{
        <<Interface - Command>>
        +execute()*
        +undo()*
    }

    class CreateTableCommand{
        <<Command Implementation>>
        +execute()
        +undo()
    }

    class IndexRebuildStrategy{
        <<Interface - Strategy>>
        +rebuildIndex(Index index)*
    }

    class ConstraintFactory{
        <<Factory Method>>
        +createConstraint(String type, String name, Object... args)$ Constraint
    }

    class ColumnBuilder{
        <<Builder>>
        +setType(DataType dataType) ColumnBuilder
        +setNullable(boolean nullable) ColumnBuilder
        +setDefaultValue(String defaultValue) ColumnBuilder
        +build() Column
    }

    class TableMemento{
        <<Memento>>
        +getTableName() String
        +getColumnsSnapshot() List~Column~
    }

    class ConstraintValidationChain{
        <<Chain of Responsibility>>
        +addConstraint(Constraint constraint)
        +validateAll() boolean
    }

    %% =====================================================
    %% RELATIONSHIPS & PATTERNS MAP
    %% =====================================================

    MetadataElement <|.. CatalogManager
    MetadataElement <|.. Database
    MetadataElement <|.. Schema
    MetadataElement <|.. Table
    MetadataElement <|.. Column

    DDLCommand <|.. CreateTableCommand

    CatalogManager "1" *-- "*" Database
    Database "1" *-- "*" Schema
    Schema "1" *-- "*" Table

    Table "1" *-- "*" Column
    Table "1" *-- "*" Index
    Table "1" *-- "*" Constraint
    Column --> DataType

    Constraint <|-- PrimaryKeyConstraint
    Constraint <|-- ForeignKeyConstraint
    Constraint <|-- UniqueConstraint
    Constraint <|-- CheckConstraint

    Index --> IndexRebuildStrategy : Strategy
    Table ..> TableMemento : Memento
    Schema ..> ConstraintFactory : Factory Method
    ColumnBuilder ..> Column : Builder
```

### 2.1 Metadata Subsystem Details

#### 2.1.1 Catalog & Database Management

```mermaid
classDiagram
    direction TD

%% =====================================================
%% METADATA MODULE (FACADE)
%% =====================================================

    class MetadataModule{
        <<Module / Facade>>
        -CatalogManager catalogManager
        +getInstance() MetadataModule$
        +getCatalogManager() CatalogManager
        +executeDDL(DDLCommand command)
        +getDatabase(String databaseName) Database
    }

%% =====================================================
%% CATALOG MANAGER (AGGREGATE ROOT)
%% =====================================================

    class CatalogManager{
        <<Singleton / Composite Root>>
        -UUID catalogId
        -Map~String,Database~ databases
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +getInstance() CatalogManager$
        +createDatabase(String databaseName) Database
        +dropDatabase(String databaseName)
        +getDatabase(String databaseName) Database
        +containsDatabase(String databaseName) boolean
        +listDatabases() List~Database~
        +clear()
    }

%% =====================================================
%% DATABASE (AGGREGATE REFERENCE)
%% =====================================================

    class Database{
        <<Aggregate Root>>
        -UUID databaseId
        -String databaseName
        -DatabaseStatus status
        -LocalDateTime createdAt
        +setStatus(DatabaseStatus status)
        +getDatabaseName() String
        +getStatus() DatabaseStatus
    }

%% =====================================================
%% COMPOSITE
%% =====================================================

    class MetadataElement{
        <<Interface>>
        +getElementName() String*
    }

%% =====================================================
%% COMMAND PATTERN
%% =====================================================

    class DDLCommand{
        <<Interface>>
        +execute()*
        +undo()*
    }

    class CreateDatabaseCommand{
        -String databaseName
        +execute()
        +undo()
    }

    class DropDatabaseCommand{
        -String databaseName
        +execute()
        +undo()
    }

%% =====================================================
%% ENUM
%% =====================================================

    class DatabaseStatus{
        <<Enumeration>>
        ONLINE
        OFFLINE
        READ_ONLY
    }

%% =====================================================
%% RELATIONSHIPS
%% =====================================================

    MetadataModule *-- CatalogManager
    CatalogManager "1" *-- "*" Database
    MetadataElement <|.. CatalogManager
    MetadataElement <|.. Database
    Database --> DatabaseStatus
    DDLCommand <|.. CreateDatabaseCommand
    DDLCommand <|.. DropDatabaseCommand
    MetadataModule ..> DDLCommand
    CreateDatabaseCommand ..> CatalogManager
    DropDatabaseCommand ..> CatalogManager
```

#### 2.1.2 Schema & Table Hierarchy

```mermaid
classDiagram
    direction TD

%% =====================================================
%% DATABASE
%% =====================================================

    class Database{
        -UUID databaseId
        -String databaseName
        -DatabaseStatus status
        -Map~String,Schema~ schemas
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +createSchema(String schemaName) Schema
        +dropSchema(String schemaName)
        +getSchema(String schemaName) Schema
        +containsSchema(String schemaName) boolean
        +listSchemas() List~Schema~
        +setStatus(DatabaseStatus status)
    }

%% =====================================================
%% SCHEMA
%% =====================================================

    class Schema{
        -UUID schemaId
        -String schemaName
        -Map~String,Table~ tables
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +createTable(String tableName) Table
        +dropTable(String tableName)
        +getTable(String tableName) Table
        +containsTable(String tableName) boolean
        +listTables() List~Table~
    }

%% =====================================================
%% DATABASE OBJECTS
%% =====================================================

    class Table

%% =====================================================
%% COMMAND PATTERN
%% =====================================================

    class DDLCommand{
        <<Interface>>
        +execute()*
        +undo()*
    }

    class CreateSchemaCommand{
        -String schemaName
        +execute()
        +undo()
    }

    class DropSchemaCommand{
        -String schemaName
        +execute()
        +undo()
    }

%% =====================================================
%% ENUM
%% =====================================================

    class DatabaseStatus{
        <<Enumeration>>
        ONLINE
        OFFLINE
        READ_ONLY
    }

%% =====================================================
%% RELATIONSHIPS
%% =====================================================

    Database *-- Schema
    Schema *-- Table
    Database --> DatabaseStatus
    DDLCommand <|.. CreateSchemaCommand
    DDLCommand <|.. DropSchemaCommand
    CreateSchemaCommand ..> Database
    DropSchemaCommand ..> Database
```

#### 2.1.3 Table Structure, Columns, Constraints & Indexes

```mermaid
classDiagram
    direction TD

%% =====================================================
%% TABLE
%% =====================================================

    class Table{
        -UUID tableId
        -String tableName
        -List~Column~ columns
        -List~Constraint~ constraints
        -List~Index~ indexes
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +createColumn(Column column)
        +dropColumn(String columnName)
        +createConstraint(Constraint constraint)
        +dropConstraint(String constraintName)
        +createIndex(Index index)
        +dropIndex(String indexName)
        +createMemento() TableMemento
        +restore(TableMemento memento)
        +clone() Table
        +registerListener(MetadataChangeListener listener)
        +removeListener(MetadataChangeListener listener)
        +notifyListeners(String eventType,String targetName)
    }

%% =====================================================
%% COLUMN
%% =====================================================

    class Column{
        -UUID columnId
        -String columnName
        -DataType dataType
        -boolean nullable
        -String defaultValue
        +changeDataType(DataType dataType)
        +clone() Column
    }

%% =====================================================
%% DATA TYPE
%% =====================================================

    class DataType{
        <<Enumeration>>
        INT
        BIGINT
        VARCHAR
        BOOLEAN
        TIMESTAMP
    }

%% =====================================================
%% INDEX
%% =====================================================

    class Index{
        -UUID indexId
        -String indexName
        -List~Column~ columns
        -boolean enabled
        +setRebuildStrategy(IndexRebuildStrategy strategy)
        +rebuild()
        +enable()
        +disable()
    }

%% =====================================================
%% CONSTRAINT
%% =====================================================

    class Constraint{
        <<Abstract>>
        -UUID constraintId
        -String constraintName
        +validate() boolean
        #preValidate() boolean
        #doValidate() boolean
        #postValidate(boolean result)
    }

    class PrimaryKeyConstraint{
        +doValidate() boolean
    }

    class ForeignKeyConstraint{
        -String referencedTable
        +validateReference() boolean
        +doValidate() boolean
    }

    class UniqueConstraint{
        +doValidate() boolean
    }

    class CheckConstraint{
        -String expression
        +evaluate() boolean
        +doValidate() boolean
    }

%% =====================================================
%% CONSTRAINT FACTORY
%% =====================================================

    class ConstraintFactory{
        <<Factory Method>>
        +createConstraint(String type, String name, Object... args) Constraint$
    }

%% =====================================================
%% CONSTRAINT VALIDATION CHAIN
%% =====================================================

    class ConstraintValidationChain{
        <<Chain of Responsibility>>
        -List~Constraint~ constraints
        +addConstraint(Constraint constraint)
        +removeConstraint(Constraint constraint)
        +validateAll() boolean
    }

%% =====================================================
%% COLUMN BUILDER
%% =====================================================

    class ColumnBuilder{
        <<Builder>>
        -String columnName
        -DataType dataType
        -boolean nullable
        -String defaultValue
        +setName(String name) ColumnBuilder
        +setType(DataType dataType) ColumnBuilder
        +setNullable(boolean nullable) ColumnBuilder
        +setDefaultValue(String value) ColumnBuilder
        +build() Column
    }

%% =====================================================
%% MEMENTO
%% =====================================================

    class TableMemento{
        <<Memento>>
        -String tableName
        -List~Column~ columns
        +getTableName() String
        +getColumnsSnapshot() List~Column~
    }

%% =====================================================
%% COMMAND PATTERN
%% =====================================================

    class DDLCommand{
        <<Interface>>
        +execute()*
        +undo()*
    }

    class CreateTableCommand{
        -String tableName
        +execute()
        +undo()
    }

    class DropTableCommand{
        -String tableName
        +execute()
        +undo()
    }

    class CreateColumnCommand{
        -Column column
        +execute()
        +undo()
    }

    class DropColumnCommand{
        -String columnName
        +execute()
        +undo()
    }

%% =====================================================
%% RELATIONSHIPS
%% =====================================================

    Table *-- Column
    Table *-- Constraint
    Table *-- Index
    Column --> DataType
    Constraint <|-- PrimaryKeyConstraint
    Constraint <|-- ForeignKeyConstraint
    Constraint <|-- UniqueConstraint
    Constraint <|-- CheckConstraint
    ConstraintFactory ..> Constraint
    ConstraintValidationChain --> Constraint
    ColumnBuilder ..> Column
    Table ..> TableMemento
    DDLCommand <|.. CreateTableCommand
    DDLCommand <|.. DropTableCommand
    DDLCommand <|.. CreateColumnCommand
    DDLCommand <|.. DropColumnCommand
    CreateTableCommand ..> Table
    DropTableCommand ..> Table
    CreateColumnCommand ..> Table
    DropColumnCommand ..> Table
    Index ..> IndexRebuildStrategy
```

#### 2.1.4 Behavioral Patterns & Infrastructure

```mermaid
classDiagram
    direction TD

%% =====================================================
%% COMPOSITE
%% =====================================================

    class MetadataElement{
        <<Interface>>
        +getElementName() String*
    }

%% =====================================================
%% OBSERVER
%% =====================================================

    class MetadataChangeListener{
        <<Interface>>
        +onMetadataChanged(String eventType,String targetName)*
    }

%% =====================================================
%% COMMAND
%% =====================================================

    class DDLCommand{
        <<Interface>>
        +execute()*
        +undo()*
    }

    class CreateTableCommand{
        -String tableName
        +execute()
        +undo()
    }

%% =====================================================
%% STRATEGY
%% =====================================================

    class IndexRebuildStrategy{
        <<Interface>>
        +rebuildIndex(Index index)*
    }

%% =====================================================
%% CHAIN OF RESPONSIBILITY
%% =====================================================

    class ConstraintValidationChain{
        -List~Constraint~ constraints
        +addConstraint(Constraint constraint)
        +validateAll() boolean
    }

%% =====================================================
%% REFERENCES
%% =====================================================

    class Schema
    class Table
    class Index
    class Constraint

%% =====================================================
%% RELATIONSHIPS
%% =====================================================

    MetadataElement <|.. CatalogManager
    MetadataElement <|.. Database
    MetadataElement <|.. Schema
    MetadataElement <|.. Table
    MetadataElement <|.. Column

    DDLCommand <|.. CreateTableCommand
    Index --> IndexRebuildStrategy
    ConstraintValidationChain --> Constraint
    CreateTableCommand ..> Schema
```

---

## 3. Query Processor Module Class Diagram

The Query Processor Module Class Diagram represents the complete SQL compilation pipeline, AST construction, semantic analysis, query optimization, and physical plan generation.

```mermaid
classDiagram
    direction TD

%% =====================================================
%% QUERY PROCESSOR MODULE
%% =====================================================

class QueryProcessor{
    <<Facade>>
    +compile(String sqlText) PhysicalPlan
}

%% =====================================================
%% SQL COMPILATION PIPELINE
%% =====================================================

class CompilerStage{
    <<Interface>>
    +process(Object input) Object*
}

class Lexer{
    <<Chain of Responsibility>>
    +process(String sqlText) TokenStream
}

class SQLParser{
    <<Chain of Responsibility>>
    +process(TokenStream stream) ParseTree
}

class ASTBuilder{
    <<Chain of Responsibility>>
    +process(ParseTree tree) AST
}

class SemanticAnalyzer{
    <<Visitor>>
    +process(AST ast) AST
    +visit(ASTNode node)
}

class QueryRewriter{
    <<Visitor>>
    +process(AST ast) AST
    +visit(ASTNode node)
}

class QueryOptimizer{
    <<Strategy Context>>
    +process(AST ast) PhysicalPlan
    +setOptimizationRule(OptimizationRule rule)
}

class PlanGenerator{
    +createPhysicalPlan(LogicalPlan plan) PhysicalPlan
}

%% =====================================================
%% LEXICAL STRUCTURES
%% =====================================================

class TokenStream
class Token
class ParseTree

%% =====================================================
%% ABSTRACT SYNTAX TREE
%% =====================================================

class AST{
    +getRoot() ASTNode
}

class ASTNode{
    <<Composite>>
    +accept(ASTVisitor visitor)
}

class ASTVisitor{
    <<Interface>>
    +visit(ASTNode node)*
}

%% =====================================================
%% EXPRESSION TREE
%% =====================================================

class ExpressionNode{
    +evaluate()
}

class PredicateNode{
    +evaluate()
}

%% =====================================================
%% PLAN BUILDERS
%% =====================================================

class LogicalPlanBuilder{
    <<Builder>>
    +build(AST ast) LogicalPlan
}

class PhysicalPlanBuilder{
    <<Builder>>
    +build(LogicalPlan plan) PhysicalPlan
}

%% =====================================================
%% OPTIMIZATION
%% =====================================================

class OptimizationRule{
    <<Interface / Strategy>>
    +optimize(LogicalPlan plan) LogicalPlan*
}

class CostEstimator{
    <<Strategy>>
    +estimate(LogicalPlan plan)
}

%% =====================================================
%% PLANS
%% =====================================================

class LogicalPlan
class PhysicalPlan

%% =====================================================
%% EXTERNAL MODULE
%% =====================================================

class MetadataModule{
    <<External Module>>
}

class ExecutionEngine{
    <<External Module>>
}

%% =====================================================
%% RELATIONSHIPS
%% =====================================================

CompilerStage <|.. Lexer
CompilerStage <|.. SQLParser
CompilerStage <|.. ASTBuilder
CompilerStage <|.. SemanticAnalyzer
CompilerStage <|.. QueryRewriter
CompilerStage <|.. QueryOptimizer

ASTVisitor <|.. SemanticAnalyzer
ASTVisitor <|.. QueryRewriter

QueryProcessor *-- Lexer
QueryProcessor *-- SQLParser
QueryProcessor *-- ASTBuilder
QueryProcessor *-- SemanticAnalyzer
QueryProcessor *-- QueryRewriter
QueryProcessor *-- QueryOptimizer
QueryProcessor *-- PlanGenerator

Lexer --> TokenStream
TokenStream *-- Token

SQLParser --> ParseTree

ASTBuilder --> AST

AST *-- ASTNode

ASTNode <|-- ExpressionNode
ASTNode <|-- PredicateNode

QueryOptimizer --> OptimizationRule
QueryOptimizer --> CostEstimator

LogicalPlanBuilder --> LogicalPlan
PhysicalPlanBuilder --> PhysicalPlan

PlanGenerator --> PhysicalPlan

SemanticAnalyzer ..> MetadataModule
QueryRewriter ..> MetadataModule
QueryOptimizer ..> MetadataModule

PlanGenerator --> ExecutionEngine
```

### 3.1 Query Processor Subsystem Details

#### 3.1.1 Semantic Analysis & Name Resolution

```mermaid
classDiagram
    direction TD

%% =====================================================
%% SEMANTIC ANALYSIS
%% =====================================================

class SemanticAnalyzer{
    <<Visitor>>
    +analyze(AST ast)
    +visit(ASTNode node)
}

class ASTVisitor{
    <<Interface>>
    +visit(ASTNode node)*
}

%% =====================================================
%% NAME RESOLUTION
%% =====================================================

class NameResolver{
    +resolve(AST ast)
}

class TableResolver{
    +resolveTable(ASTNode node)
}

class ColumnResolver{
    +resolveColumn(ASTNode node)
}

class AliasResolver{
    +resolveAlias(ASTNode node)
}

%% =====================================================
%% TYPE CHECKING
%% =====================================================

class TypeChecker{
    +validate(AST ast)
}

class ExpressionTypeChecker{
    +checkExpression(ASTNode node)
}

class FunctionTypeChecker{
    +checkFunction(ASTNode node)
}

%% =====================================================
%% SEMANTIC VALIDATION
%% =====================================================

class AggregateValidator{
    +validate(AST ast)
}

class GroupByValidator{
    +validate(AST ast)
}

class OrderByValidator{
    +validate(AST ast)
}

%% =====================================================
%% EXTERNAL MODULE
%% =====================================================

class MetadataModule{
    <<External Module>>
}

%% =====================================================
%% SHARED OBJECTS
%% =====================================================

class AST
class ASTNode

%% =====================================================
%% RELATIONSHIPS
%% =====================================================

ASTVisitor <|.. SemanticAnalyzer

SemanticAnalyzer *-- NameResolver
SemanticAnalyzer *-- TypeChecker

SemanticAnalyzer *-- AggregateValidator
SemanticAnalyzer *-- GroupByValidator
SemanticAnalyzer *-- OrderByValidator

NameResolver *-- TableResolver
NameResolver *-- ColumnResolver
NameResolver *-- AliasResolver

TypeChecker *-- ExpressionTypeChecker
TypeChecker *-- FunctionTypeChecker

SemanticAnalyzer --> AST
AST *-- ASTNode

TableResolver ..> MetadataModule
ColumnResolver ..> MetadataModule
AliasResolver ..> MetadataModule

ExpressionTypeChecker ..> MetadataModule
FunctionTypeChecker ..> MetadataModule
```

#### 3.1.2 Query Optimization Engine

```mermaid
classDiagram
    direction TD

%% =====================================================
%% QUERY OPTIMIZER
%% =====================================================

class QueryOptimizer{
    <<Strategy Context>>
    +optimize(LogicalPlan logicalPlan) PhysicalPlan
    +setOptimizationRule(OptimizationRule rule)
}

class OptimizationRule{
    <<Interface>>
    +optimize(LogicalPlan logicalPlan)*
}

%% =====================================================
%% QUERY REWRITE
%% =====================================================

class QueryRewriter{
    +rewrite(LogicalPlan logicalPlan)
}

class PredicatePushdownOptimizer{
    +optimize(LogicalPlan logicalPlan)
}

class ProjectionPushdownOptimizer{
    +optimize(LogicalPlan logicalPlan)
}

class ConstantFoldingOptimizer{
    +optimize(LogicalPlan logicalPlan)
}

%% =====================================================
%% JOIN OPTIMIZATION
%% =====================================================

class JoinOptimizer{
    +optimize(LogicalPlan logicalPlan)
}

class JoinOrderOptimizer{
    +optimize(LogicalPlan logicalPlan)
}

class JoinMethodSelector{
    +selectJoinMethod(LogicalPlan logicalPlan)
}

%% =====================================================
%% COST OPTIMIZATION
%% =====================================================

class CostEstimator{
    +estimate(LogicalPlan logicalPlan)
}

class CardinalityEstimator{
    +estimate(LogicalPlan logicalPlan)
}

class StatisticsManager{
    +estimateCardinality()
    +estimateSelectivity()
}

%% =====================================================
%% PLAN SEARCH
%% =====================================================

class PlanEnumerator{
    +enumerate(LogicalPlan logicalPlan)
}

class AccessPathSelector{
    +select(LogicalPlan logicalPlan)
}

%% =====================================================
%% SHARED OBJECTS
%% =====================================================

class LogicalPlan
class PhysicalPlan

%% =====================================================
%% RELATIONSHIPS
%% =====================================================

QueryOptimizer --> QueryRewriter
QueryOptimizer --> JoinOptimizer
QueryOptimizer --> CostEstimator
QueryOptimizer --> PlanEnumerator

OptimizationRule <|.. QueryRewriter
OptimizationRule <|.. JoinOptimizer
OptimizationRule <|.. CostEstimator

QueryRewriter *-- PredicatePushdownOptimizer
QueryRewriter *-- ProjectionPushdownOptimizer
QueryRewriter *-- ConstantFoldingOptimizer

JoinOptimizer *-- JoinOrderOptimizer
JoinOptimizer *-- JoinMethodSelector

CostEstimator --> CardinalityEstimator
CostEstimator --> StatisticsManager

PlanEnumerator --> AccessPathSelector
PlanEnumerator --> PhysicalPlan
```

#### 3.1.3 Plan Generation & Building

```mermaid
classDiagram
    direction TD

%% =====================================================
%% PLAN GENERATION
%% =====================================================

class PlanGenerator{
    +createLogicalPlan(AST ast) LogicalPlan
    +createPhysicalPlan(LogicalPlan logicalPlan) PhysicalPlan
}

%% =====================================================
%% LOGICAL PLAN
%% =====================================================

class LogicalPlanBuilder{
    <<Builder>>
    +build(AST ast)
}

class LogicalOperatorFactory{
    <<Factory Method>>
    +createOperator(ASTNode node)
}

class LogicalPlan

%% =====================================================
%% PHYSICAL PLAN
%% =====================================================

class PhysicalPlanBuilder{
    <<Builder>>
    +build(LogicalPlan logicalPlan)
}

class PhysicalOperatorFactory{
    <<Factory Method>>
    +createOperator(LogicalPlanNode node)
}

class PhysicalPlan

%% =====================================================
%% PLAN VALIDATION
%% =====================================================

class PlanValidator{
    +validate(LogicalPlan logicalPlan)
}

class PlanNormalizer{
    +normalize(LogicalPlan logicalPlan)
}

%% =====================================================
%% SHARED OBJECTS
%% =====================================================

class AST
class ASTNode
class LogicalPlanNode

%% =====================================================
%% RELATIONSHIPS
%% =====================================================

PlanGenerator --> LogicalPlanBuilder
PlanGenerator --> PhysicalPlanBuilder

LogicalPlanBuilder --> LogicalOperatorFactory
LogicalPlanBuilder --> LogicalPlan

PhysicalPlanBuilder --> PhysicalOperatorFactory
PhysicalPlanBuilder --> PhysicalPlan

LogicalPlanBuilder --> AST
PlanValidator --> LogicalPlan
PlanNormalizer --> LogicalPlan
```

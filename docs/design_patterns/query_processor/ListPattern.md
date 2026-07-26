# Design Patterns in Query Processor Module (Core & Subsystems)

This document details the **core and subsystem Design Patterns** in the `query_processor` module, organized by compilation and optimization pipeline stages: **Pipeline & Facade Layer** ➔ **Lexical & Syntax Parsing** ➔ **AST & Syntax Tree** ➔ **Semantic Analysis & Query Rewrite** ➔ **Query Optimization (CBO)** ➔ **Plan Building & Generation**.

---

## 1. Facade & Pipeline Layer

### 1.1. Facade Pattern
* **Pattern**: Facade Pattern
* **Class/Interface Applied**: QueryProcessor
* **Method**: `compile(sqlText)`
* **Reason for Use**: Provides a unified, high-level API entry point for external modules (Database Core Server, Execution Engine) to process SQL queries, encapsulating the complex compilation pipeline from Lexer, SQLParser, ASTBuilder, SemanticAnalyzer, QueryRewriter, QueryOptimizer to PlanGenerator.

### 1.2. Chain of Responsibility Pattern
* **Pattern**: Chain of Responsibility Pattern
* **Class/Interface Applied**: CompilerStage (implemented by Lexer, SQLParser, ASTBuilder, SemanticAnalyzer, QueryRewriter, QueryOptimizer)
* **Method**: `process(input)`
* **Reason for Use**: Chains independent compiler compilation stages sequentially, where each stage processes its dedicated task (Tokenizing ➔ Parsing ➔ AST Construction ➔ Semantic Check ➔ Query Rewrite ➔ Optimization) and forwards the transformed output to the next stage in the pipeline.

---

## 2. AST & Syntax Tree Level

### 2.1. Composite Pattern
* **Pattern**: Composite Pattern
* **Class/Interface Applied**: AST, ASTNode
* **Method**: `accept(visitor)`
* **Reason for Use**: Constructs a hierarchical tree representation for Abstract Syntax Trees (AST), allowing uniform tree traversal and manipulation across both composite tree nodes and individual statement/expression nodes.

---

## 3. Semantic Analysis & Query Rewrite Level

### 3.1. Visitor Pattern
* **Pattern**: Visitor Pattern
* **Class/Interface Applied**: ASTVisitor (implemented by SemanticAnalyzer, QueryRewriter)
* **Method**: `visit(node)`, `analyze(ast)`, `rewrite(ast)`
* **Reason for Use**: Decouples AST traversal and semantic inspection/transformation logic from `ASTNode` classes, enabling multiple compiler passes (Semantic Analysis, Query Rewrite) to operate over the AST without modifying node structures.

---

## 4. Query Optimization Level (CBO & Optimization Rules)

### 4.1. Strategy Pattern
* **Pattern**: Strategy Pattern
* **Class/Interface Applied**: QueryOptimizer (Strategy Context), OptimizationRule (Strategy Interface), CostEstimator, PredicatePushdownOptimizer, ProjectionPushdownOptimizer, ConstantFoldingOptimizer
* **Method**: `optimize(plan)`, `setOptimizationRule(rule)`, `estimate(plan)`
* **Reason for Use**: Encapsulates Cost-Based Optimization (CBO) algorithms, estimation strategies, and query rewrite rules into interchangeable Strategy objects that can be dynamically selected and executed by `QueryOptimizer`.

---

## 5. Execution Plan Building & Generation Level

### 5.1. Builder Pattern
* **Pattern**: Builder Pattern
* **Class/Interface Applied**: LogicalPlanBuilder, PhysicalPlanBuilder
* **Method**: `build(ast)`, `build(logicalPlan)`
* **Reason for Use**: Constructs complex `LogicalPlan` and `PhysicalPlan` execution trees step-by-step from AST nodes and optimizer decisions.

### 5.2. Factory Method Pattern
* **Pattern**: Factory Method Pattern
* **Class/Interface Applied**: LogicalOperatorFactory, PhysicalOperatorFactory
* **Method**: `createOperator(node)`
* **Reason for Use**: Encapsulates operator instantiation logic for logical operators (`LogicalScan`, `LogicalFilter`, `LogicalJoin`, `LogicalAggregate`, `LogicalSort`) and physical execution operators (`PhysicalSeqScan`, `PhysicalHashJoin`, `PhysicalNestedLoop`).

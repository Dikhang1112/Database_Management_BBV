# Sequence Diagrams for Core Features in Query Processor Module

This document contains sequence diagrams for the **core features** of the Query Processor module. Each feature demonstrates one or more design patterns working together rather than illustrating an isolated pattern.

---

# 1. Compile SQL Statement

### Feature
Compile SQL Statement

### Actor
Application / DatabaseServer

### Classes Involved
- QueryProcessor
- Lexer
- SQLParser
- ASTBuilder
- SemanticAnalyzer
- QueryRewriter
- QueryOptimizer
- PlanGenerator

### Design Patterns
- Facade
- Builder
- Interpreter
- Visitor
- Strategy
- Factory Method

```mermaid
sequenceDiagram
    autonumber

    actor Client

    participant QP as QueryProcessor
    participant Lexer
    participant Parser as SQLParser
    participant ASTB as ASTBuilder
    participant SA as SemanticAnalyzer
    participant QR as QueryRewriter
    participant Optimizer as QueryOptimizer
    participant PG as PlanGenerator

    Client->>QP: compile(sql)

    QP->>Lexer: tokenize(sql)
    Lexer-->>QP: TokenStream

    QP->>Parser: parse(TokenStream)
    Parser-->>QP: ParseTree

    QP->>ASTB: build(ParseTree)
    ASTB-->>QP: AST

    QP->>SA: analyze(AST)
    SA-->>QP: Validated AST

    QP->>QR: rewrite(AST)
    QR-->>QP: Optimized AST

    QP->>Optimizer: optimize(AST)
    Optimizer-->>QP: LogicalPlan

    QP->>PG: build(LogicalPlan)
    PG-->>QP: PhysicalPlan

    QP-->>Client: PhysicalPlan
```

---

# 2. Resolve Table Reference

### Feature

Resolve Table Reference

### Actor

QueryProcessor

### Classes

- SemanticAnalyzer
- TableResolver
- MetadataModule
- CatalogManager
- Database
- Schema
- Table

### Design Patterns

- Visitor
- Facade
- Composite

```mermaid
sequenceDiagram

    autonumber

    participant SA as SemanticAnalyzer
    participant Resolver as TableResolver
    participant Meta as MetadataModule
    participant Catalog as CatalogManager
    participant DB as Database
    participant Schema
    participant Table

    SA->>Resolver: resolve(tableName)

    Resolver->>Meta: getTable()

    Meta->>Catalog: getDatabase()

    Catalog->>DB: getSchema()

    DB->>Schema: getTable()

    Schema-->>Resolver: Table

    Resolver-->>SA: Success
```

---

# 3. Rewrite Query

### Feature

Rewrite Query

### Design Patterns

- Visitor

```mermaid
sequenceDiagram

    autonumber

    participant SA as SemanticAnalyzer
    participant QR as QueryRewriter
    participant AST

    SA->>QR: rewrite(AST)

    QR->>AST: visit()

    AST-->>QR: ASTNode

    QR->>QR: Predicate Pushdown

    QR->>QR: Constant Folding

    QR-->>SA: Rewritten AST
```

---

# 4. Optimize Query

### Feature

Optimize Query

### Design Patterns

- Strategy

- Composite

```mermaid
sequenceDiagram

    autonumber

    participant QP
    participant Optimizer
    participant Rule
    participant Cost
    participant Statistics

    QP->>Optimizer: optimize()

    Optimizer->>Rule: optimize(plan)

    Rule-->>Optimizer: Optimized Plan

    Optimizer->>Cost: estimate(plan)

    Cost->>Statistics: estimateCardinality()

    Statistics-->>Cost: Cardinality

    Cost-->>Optimizer: Cost
```

---

# 5. Build Logical Plan

### Feature

Build Logical Plan

### Design Patterns

- Builder

```mermaid
sequenceDiagram

    autonumber

    participant Optimizer
    participant Builder as LogicalPlanBuilder
    participant LogicalPlan

    Optimizer->>Builder: build(AST)

    Builder->>Builder: addScan()

    Builder->>Builder: addFilter()

    Builder->>Builder: addJoin()

    Builder->>Builder: addProject()

    Builder-->>Optimizer: LogicalPlan
```

---

# 6. Build Physical Plan

### Feature

Build Physical Plan

### Design Patterns

- Builder

- Factory Method

```mermaid
sequenceDiagram

    autonumber

    participant Optimizer
    participant Builder as PhysicalPlanBuilder
    participant Factory as PhysicalOperatorFactory
    participant PhysicalPlan

    Optimizer->>Builder: build(LogicalPlan)

    Builder->>Factory: createOperator()

    Factory-->>Builder: PhysicalOperator

    Builder->>Builder: assembleOperatorTree()

    Builder-->>Optimizer: PhysicalPlan
```
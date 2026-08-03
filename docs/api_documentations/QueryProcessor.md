# Query Processor Subsystem - REST API Documentation & Design Pattern Mapping

This document provides a comprehensive specification of the core REST APIs for the **Query Processor Subsystem** of the DBMS engine. All endpoints are mapped to underlying **Design Patterns** (Facade, Chain of Responsibility, Composite, Visitor, Strategy, Builder, Factory Method) and follow the standardized `ApiResponse` wrapper.

---

## 1. Standard Response Format

All REST endpoints return a unified JSON response envelope:

```json
{
  "status": 200,
  "message": "Human readable response description",
  "data": { ... },
  "timestamp": "03-08-2026 15:00:00"
}
```

* **`status`**: HTTP Status Code (`200 OK`, `201 Created`, `204 No Content`, `400 Bad Request`, `404 Not Found`, `405 Method Not Allowed`, `500 Internal Error`).
* **`message`**: Result message or exception detail.
* **`data`**: Payload data object (returns `null` or 204 No Content on errors or execution failures).
* **`timestamp`**: Datetime string formatted as `dd-MM-yyyy HH:mm:ss`.

---

## 2. REST API List Grouped by Subsystem Domains & Design Patterns

### 2.1. Compilation Pipeline (Facade & Chain of Responsibility Pattern)

| # | Method | REST API URL | Parameters | Status Code | Design Pattern / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **1** | `POST` | `/api/v1/query-processor/compile` | **Body:** `{ "sqlText": "String" }` | `200 OK`, `400`, `500` | **Facade Pattern:** Execute full SQL compilation pipeline into PhysicalPlan |
| **2** | `POST` | `/api/v1/query-processor/pipeline/process-stage` | **Body:** `{ "stage": "String", "input": Object }` | `200 OK`, `400` | **Chain of Responsibility Pattern:** Execute individual CompilerStage step |
| **3** | `GET` | `/api/v1/query-processor/pipeline/stages` | *None* | `200 OK` | **Chain of Responsibility Pattern:** List registered stages in compilation pipeline |

---

### 2.2. Lexical Analysis Subsystem

| # | Method | REST API URL | Parameters | Status Code | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **4** | `POST` | `/api/v1/query-processor/lexical/tokenize` | **Body:** `{ "sqlText": "String" }` | `200 OK`, `400` | Tokenize raw SQL string into `TokenStream` via `Lexer` |
| **5** | `POST` | `/api/v1/query-processor/lexical/validate` | **Body:** `{ "tokenStream": TokenStream }` | `200 OK`, `400` | Validate lexical correctness of tokens in `TokenStream` |

---

### 2.3. Syntactic Analysis & AST (Composite & Visitor Pattern)

| # | Method | REST API URL | Parameters | Status Code | Design Pattern / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **6** | `POST` | `/api/v1/query-processor/parser/parse` | **Body:** TokenStream | `200 OK`, `400` | `SQLParser`: Transform `TokenStream` into `ParseTree` |
| **7** | `POST` | `/api/v1/query-processor/parser/ast/build` | **Body:** ParseTree | `201 Created`, `400` | `ASTBuilder`: Construct root `AST` node hierarchy from `ParseTree` |
| **8** | `POST` | `/api/v1/query-processor/ast/nodes` | **Body:** ASTNodeDTO | `201 Created`, `400` | **Composite Pattern:** Instantiates Select, Table, or Where AST nodes |
| **9** | `POST` | `/api/v1/query-processor/ast/accept-visitor` | **Body:** `{ "ast": AST, "visitorType": "String" }` | `200 OK`, `400` | **Visitor Pattern:** Traverse AST hierarchy via `ASTVisitor` implementation |

---

### 2.4. Semantic Analysis Subsystem (Visitor Pattern)

| # | Method | REST API URL | Parameters | Status Code | Design Pattern / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **10** | `POST` | `/api/v1/query-processor/semantic/analyze` | **Body:** AST | `200 OK`, `400` | **Visitor Pattern:** Execute full 4-step semantic analysis via `SemanticAnalyzer` |
| **11** | `POST` | `/api/v1/query-processor/semantic/resolve-names` | **Body:** AST | `200 OK`, `400`, `404` | `NameResolver`: Resolve table, column, and alias identifiers via `MetadataModule` |
| **12** | `POST` | `/api/v1/query-processor/semantic/validate-types` | **Body:** AST | `200 OK`, `400` | `TypeChecker`: Validate expression data types against metadata catalog |
| **13** | `POST` | `/api/v1/query-processor/semantic/validate-group-by` | **Body:** AST | `200 OK`, `400` | `GroupByValidator`: Validate GROUP BY aggregation rules and column dependencies |
| **14** | `POST` | `/api/v1/query-processor/semantic/validate-order-by` | **Body:** AST | `200 OK`, `400` | `OrderByValidator`: Validate ORDER BY sorting keys and expressions |

---

### 2.5. Query Optimization Subsystem (Strategy Pattern)

| # | Method | REST API URL | Parameters | Status Code | Design Pattern / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **15** | `POST` | `/api/v1/query-processor/optimizer/optimize` | **Body:** LogicalPlan | `200 OK`, `400` | `QueryOptimizer`: Coordinate query optimization pipeline |
| **16** | `POST` | `/api/v1/query-processor/optimizer/rewrite` | **Body:** LogicalPlan | `200 OK`, `400` | `QueryRewriter`: Apply logical rewriting rules (predicate pushdown, projection pruning) |
| **17** | `PUT` | `/api/v1/query-processor/optimizer/rules/strategy` | **Body:** `{ "strategy": "RULE_BASED \| COST_BASED" }` | `200 OK`, `400` | **Strategy Pattern:** Switch optimization strategy (`RuleBased` or `CostBased`) |
| **18** | `POST` | `/api/v1/query-processor/optimizer/cost/estimate` | **Body:** LogicalPlan | `200 OK`, `400` | `CostEstimator` & `StatisticsManager`: Compute execution cost estimates |
| **19** | `POST` | `/api/v1/query-processor/optimizer/join/optimize` | **Body:** LogicalPlan | `200 OK`, `400` | `JoinOptimizer`: Reorder multi-table join operators |

---

### 2.6. Plan Generation Subsystem (Builder & Factory Method Pattern)

| # | Method | REST API URL | Parameters | Status Code | Design Pattern / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **20** | `POST` | `/api/v1/query-processor/plan/logical/create` | **Body:** AST | `201 Created`, `400` | `PlanGenerator`: Generate `LogicalPlan` from validated AST |
| **21** | `POST` | `/api/v1/query-processor/plan/physical/create` | **Body:** LogicalPlan | `201 Created`, `400` | `PlanGenerator`: Generate `PhysicalPlan` via validate -> normalize -> build pipeline |
| **22** | `POST` | `/api/v1/query-processor/plan/logical/build` | **Body:** AST | `201 Created`, `400` | **Builder Pattern:** `LogicalPlanBuilder.build(ast)` |
| **23** | `POST` | `/api/v1/query-processor/plan/physical/build` | **Body:** LogicalPlan | `201 Created`, `400` | **Builder Pattern:** `PhysicalPlanBuilder.build(logicalPlan)` |
| **24** | `POST` | `/api/v1/query-processor/plan/operators/logical` | **Body:** OperatorDTO | `201 Created`, `400` | **Factory Method Pattern:** `LogicalOperatorFactory.createLogicalOperator()` |
| **25** | `POST` | `/api/v1/query-processor/plan/operators/physical` | **Body:** OperatorDTO | `201 Created`, `400` | **Factory Method Pattern:** `PhysicalOperatorFactory.createPhysicalOperator()` |
| **26** | `POST` | `/api/v1/query-processor/plan/logical/normalize` | **Body:** LogicalPlan | `200 OK`, `400` | `PlanNormalizer`: Standardize logical plan tree structure |
| **27** | `POST` | `/api/v1/query-processor/plan/logical/validate` | **Body:** LogicalPlan | `200 OK`, `400` | `PlanValidator`: Verify structural validity of LogicalPlan |

---

### 2.7. Execution Engine Integration

| # | Method | REST API URL | Parameters | Status Code | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **28** | `POST` | `/api/v1/query-processor/execution/execute` | **Body:** PhysicalPlan | `200 OK`, `400`, `500` | `ExecutionEngine`: Dispatch PhysicalPlan to execution engine & return result set |

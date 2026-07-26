# Query Processor Subsystem - Semantic Analysis Unit Test Scenarios Mindmap

This mindmap represents the structural taxonomy of the Query Processor - Semantic Analysis unit test scenarios, organized from top-level orchestrator (`SemanticAnalyzer`) down to component-level resolvers, type checkers, validators, and AST visitor nodes (`SemanticAnalyzer` ➔ `NameResolver` ➔ `TypeChecker` ➔ `AggregateValidator` ➔ `GroupByValidator` ➔ `OrderByValidator` ➔ `ASTVisitor & ASTNode`).

```mermaid
flowchart LR
    Root(("Query Processor Semantic Analysis Unit Tests"))

    %% =====================================================
    %% Categories (Semantic Analysis Subsystem Hierarchy)
    %% =====================================================

    Cat1(["1. SemanticAnalyzerTest"])
    Cat2(["2. NameResolverTest"])
    Cat3(["3. TypeCheckerTest"])
    Cat4(["4. AggregateValidatorTest"])
    Cat5(["5. GroupByValidatorTest"])
    Cat6(["6. OrderByValidatorTest"])
    Cat7(["7. ASTVisitorAndNodeTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4
    Root --> Cat5
    Root --> Cat6
    Root --> Cat7

    %% =====================================================
    %% 1. SemanticAnalyzerTest (Main Orchestrator & Visitor)
    %% =====================================================

    Cat1 --> TC01("TC-01 Analyze Full AST")
    TC01 --> TC01A("TC-01A Analyze Null AST")
    TC01 --> TC01B("TC-01B Visit AST Node")
    TC01 --> TC01C("TC-01C Visit Null AST Node")
    TC01 --> TC01D("TC-01D Semantic Analysis Error Propagation")

    %% =====================================================
    %% 2. NameResolverTest (Table, Column & Alias Resolution)
    %% =====================================================

    Cat2 --> TC02("TC-02 Resolve Table Identifier")
    TC02 --> TC02A("TC-02A Resolve Table - Table Not Found")
    TC02 --> TC02B("TC-02B Resolve Column Identifier")
    TC02 --> TC02C("TC-02C Resolve Column - Column Not Found")
    TC02 --> TC02D("TC-02D Resolve Table Alias")
    TC02 --> TC02E("TC-02E Resolve Duplicate Alias Collision")
    TC02 --> TC02F("TC-02F Resolve Full AST Identifiers")

    %% =====================================================
    %% 3. TypeCheckerTest (Expression & Function Type Checking)
    %% =====================================================

    Cat3 --> TC03("TC-03 Validate AST Types")
    TC03 --> TC03A("TC-03A Compatible Operands Expression")
    TC03 --> TC03B("TC-03B Incompatible Type Mismatch")
    TC03 --> TC03C("TC-03C Check Function Types")
    TC03 --> TC03D("TC-03D Function Not Found")
    TC03 --> TC03E("TC-03E Parameter Type Mismatch")

    %% =====================================================
    %% 4. AggregateValidatorTest (Aggregate Function Validation)
    %% =====================================================

    Cat4 --> TC04("TC-04 Validate Aggregate Functions")
    TC04 --> TC04A("TC-04A Nested Aggregates Disallowed")
    TC04 --> TC04B("TC-04B Aggregate in WHERE Clause Disallowed")

    %% =====================================================
    %% 5. GroupByValidatorTest (GROUP BY Clause Validation)
    %% =====================================================

    Cat5 --> TC05("TC-05 Validate GROUP BY Clause")
    TC05 --> TC05A("TC-05A Missing Non-Aggregated Column")
    TC05 --> TC05B("TC-05B Empty GROUP BY with Non-Aggregated Column")

    %% =====================================================
    %% 6. OrderByValidatorTest (ORDER BY Clause Validation)
    %% =====================================================

    Cat6 --> TC06("TC-06 Validate ORDER BY Clause")
    TC06 --> TC06A("TC-06A Ambiguous Sort Key")
    TC06 --> TC06B("TC-06B DISTINCT Query Sort Key Missing from Projection")

    %% =====================================================
    %% 7. ASTVisitorAndNodeTest (Visitor Double Dispatch & AST)
    %% =====================================================

    Cat7 --> TC07("TC-07 AST Node Accept Visitor")
    TC07 --> TC07A("TC-07A AST Node Accept Null Visitor")
    TC07 --> TC07B("TC-07B AST Root Traversal")
```

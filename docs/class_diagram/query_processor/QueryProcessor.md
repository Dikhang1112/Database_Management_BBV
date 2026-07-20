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
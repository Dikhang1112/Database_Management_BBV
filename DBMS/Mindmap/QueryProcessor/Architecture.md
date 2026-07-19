flowchart TD

%% =====================================================
%% LEXICAL ANALYSIS
%% =====================================================

Lexer["Lexer"]
TokenStream["Token Stream"]

%% =====================================================
%% SYNTAX ANALYSIS
%% =====================================================

SQLParser["SQL Parser"]
ParseTree["Parse Tree"]

%% =====================================================
%% AST CONSTRUCTION
%% =====================================================

ASTBuilder["AST Builder"]
AST["Abstract Syntax Tree"]

%% =====================================================
%% SEMANTIC ANALYSIS
%% =====================================================

SemanticAnalyzer["Semantic Analyzer"]
NameResolver["Name Resolver"]
TypeChecker["Type Checker"]

%% =====================================================
%% QUERY OPTIMIZER
%% =====================================================

QueryOptimizer["Query Optimizer"]
LogicalPlan["Logical Plan"]
CostEstimator["Cost Estimator"]
StatisticsManager["Statistics Manager"]
PlanGenerator["Physical Plan Generator"]
PhysicalPlan["Physical Plan"]

%% =====================================================
%% EXTERNAL MODULES
%% =====================================================

MetadataModule["Metadata Module"]
ExecutionEngine["Execution Engine"]
ResultSet["Result Set"]

%% =====================================================
%% MAIN PIPELINE
%% =====================================================

Lexer --> TokenStream

TokenStream --> SQLParser

SQLParser --> ParseTree

ParseTree --> ASTBuilder

ASTBuilder --> AST

AST --> SemanticAnalyzer

SemanticAnalyzer --> QueryOptimizer

QueryOptimizer --> LogicalPlan

LogicalPlan --> CostEstimator

CostEstimator --> StatisticsManager

CostEstimator --> PlanGenerator

PlanGenerator --> PhysicalPlan

PhysicalPlan --> ExecutionEngine

ExecutionEngine --> ResultSet

%% =====================================================
%% INTERNAL RESPONSIBILITIES
%% =====================================================

SemanticAnalyzer -. delegates .-> NameResolver
SemanticAnalyzer -. delegates .-> TypeChecker

%% =====================================================
%% METADATA LOOKUPS
%% =====================================================

NameResolver -. lookup .-> MetadataModule
TypeChecker -. validate .-> MetadataModule
QueryOptimizer -. statistics .-> MetadataModule
StatisticsManager -. metadata .-> MetadataModule
```
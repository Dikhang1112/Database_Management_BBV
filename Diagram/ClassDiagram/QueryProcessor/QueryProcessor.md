classDiagram
    direction RL

    %% ==========================================
    %% 1. SQL PARSER LAYER
    %% ==========================================
    class SqlLexer {
        -String sqlText
        -int currentPosition
        +nextToken() Token
    }

    class SyntaxParser {
        -SqlLexer lexerReference
        +parseTree() AbstractSyntaxTree
    }

    %% ==========================================
    %% 2. QUERY INGESTION LAYER
    %% ==========================================
    class QueryValidator {
        -MetadataCache metadataCacheReference
        -boolean isSemantic
        +validateAst(AbstractSyntaxTree ast, SessionContext session) boolean
    }

    class QueryOptimizer {
        -int maxOptimizationTimeMs
        +optimizePlan(AbstractSyntaxTree validatedAst) ExecutionPlan
    }

    class QueryIngestionEngine {
        -SyntaxParser parserComponent
        -QueryValidator validatorComponent
        -QueryOptimizer optimizerComponent
        -PlanExecutor executorComponent
        +ingestQuery(String sqlText, SessionContext session) void
    }

    %% ==========================================
    %% 3. EXPLICITLY RE-DECLARED EXTERNAL CLASSES
    %% ==========================================
    class SessionContext {
        <<interface>>
        +getSessionID() int
        +getCurrentUser() String
        +updateState(String newState) void
    }

    class PlanExecutor {
        <<interface>>
        +executePlan(ExecutionPlan plan, SessionContext session) RowIterator
    }

    class ExecutionPlan {
        -int planID [PK]
        -String optimizedTreeJson
        +getRootOperatorID() int
    }

    %% --- INTERNAL ABSTRACT TYPING MOCKS ---
    class Token { }
    class AbstractSyntaxTree { }

    %% ==========================================
    %% CORE STRUCTURE UML RELATIONSHIPS ONLY
    %% ==========================================
    QueryIngestionEngine "1" *-- "1" SyntaxParser : orchestrates syntax parsing
    QueryIngestionEngine "1" *-- "1" QueryValidator : orchestrates semantic checking
    QueryIngestionEngine "1" *-- "1" QueryOptimizer : orchestrates plan optimization
    
    SyntaxParser "1" --> "1" SqlLexer : consumes tokens from

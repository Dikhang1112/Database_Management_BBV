
graph LR
    QP["QueryProcessor Unit Tests"] --> Lexer["SqlLexerTest"]
    QP --> Parser["SyntaxParserTest"]
    QP --> Validator["QueryValidatorTest"]
    QP --> Optimizer["QueryOptimizerTest"]
    QP --> Ingestion["QueryIngestionEngineTest"]

    %% ==========================================
    %% SQL LEXER
    %% ==========================================
    subgraph SqlLexer Test Cases
        Lexer --> L1["Method: testCaseInsensitivityOfKeywords()<br/>Input: 'select * from users'<br/>Output: Tokens[SELECT, STAR, FROM, IDENTIFIER(users), EOF]"]
        Lexer --> L2["Method: testEmptyAndWhitespaceQuery()<br/>Input: '   \n \t '<br/>Output: Token[EOF]"]
        Lexer --> L3["Method: testBasicSelectQuery()<br/>Input: 'SELECT * FROM users'<br/>Output: Tokens[SELECT, STAR, FROM, IDENTIFIER(users), EOF]"]
        Lexer --> L4["Method: testComplexQueryWithWhereClause()<br/>Input: 'SELECT name, age FROM users WHERE id = 10'<br/>Output: Tokens[SELECT, IDENTIFIER(name), COMMA, ..., EQUALS, NUMBER, EOF]"]
        Lexer --> L5["Method: testUnsupportedCharacters()<br/>Input: 'SELECT @ FROM #'<br/>Output: Tokens[SELECT, IDENTIFIER(@), FROM, IDENTIFIER(#), EOF]"]
    end

    %% ==========================================
    %% SYNTAX PARSER
    %% ==========================================
    subgraph SyntaxParser Test Cases
        Parser --> P1["Method: testParseValidSelectQuery()<br/>Input: 'SELECT * FROM users'<br/>Output: AbstractSyntaxTree: SelectNode(Source: users, Projection: ALL)"]
        Parser --> P2["Method: testParseWhereClause()<br/>Input: 'SELECT name FROM users WHERE age = 20'<br/>Output: AST: SelectNode with WhereNode(BinaryOp(EQUALS, age, 20))"]
        Parser --> P3["Method: testParseSyntaxErrorMissingFrom()<br/>Input: 'SELECT * users'<br/>Output: Throws: ParseException('Missing FROM keyword')"]
        Parser --> P4["Method: testParseEmptyQuery()<br/>Input: ''<br/>Output: Throws: EmptyQueryException"]
    end

    %% ==========================================
    %% QUERY VALIDATOR
    %% ==========================================
    subgraph QueryValidator Test Cases
        Validator --> V1["Method: testValidateExistingTableAndColumns()<br/>Input: AST('SELECT age FROM users') + MetadataCache with schema 'users'<br/>Output: boolean: true"]
        Validator --> V2["Method: testValidateMissingTable()<br/>Input: AST('SELECT * FROM non_existing') + MetadataCache is empty<br/>Output: Throws: TableNotFoundException('non_existing')"]
        Validator --> V3["Method: testValidateTypeMismatch()<br/>Input: AST('WHERE age = &quot;twenty&quot;') where age is INT<br/>Output: Throws: TypeMismatchException('Cannot compare INT with STRING')"]
        Validator --> V4["Method: testValidateUserPermissions()<br/>Input: AST('SELECT * FROM salary') + SessionContext('guest')<br/>Output: Throws: UnauthorizedAccessException"]
    end

    %% ==========================================
    %% QUERY OPTIMIZER
    %% ==========================================
    subgraph QueryOptimizer Test Cases
        Optimizer --> O1["Method: testOptimizeProjectionPushdown()<br/>Input: AST with nested filters<br/>Output: ExecutionPlan with Projection operation pushed below Scan"]
        Optimizer --> O2["Method: testOptimizeJoinOrder()<br/>Input: Join AST of tables A, B, C (A: large, B: small, C: medium)<br/>Output: ExecutionPlan: Join order set to (B JOIN C) JOIN A"]
        Optimizer --> O3["Method: testOptimizationTimeout()<br/>Input: Complex AST + maxOptimizationTimeMs: 1<br/>Output: ExecutionPlan: Default fallback non-optimized plan"]
    end

    %% ==========================================
    %% QUERY INGESTION ENGINE
    %% ==========================================
    subgraph QueryIngestionEngine Test Cases
        Ingestion --> I1["Method: testIngestSuccessfulQuery()<br/>Input: 'SELECT * FROM users' + Valid SessionContext<br/>Output: Successful execution (delegates to PlanExecutor)"]
        Ingestion --> I2["Method: testIngestParserErrorInterruptsFlow()<br/>Input: 'SELECT *' (invalid query syntax)<br/>Output: Throws: ParseException (Validator & Executor NOT invoked)"]
        Ingestion --> I3["Method: testIngestValidationErrorInterruptsFlow()<br/>Input: 'SELECT non_existing FROM users'<br/>Output: Throws: ValidationException (Optimizer & Executor NOT invoked)"]
        Ingestion --> I4["Method: testIngestCorrectPlanDelegation()<br/>Input: 'SELECT * FROM users'<br/>Output: Verify: executorComponent.executePlan(expectedPlan, session) was invoked"]
    end
```

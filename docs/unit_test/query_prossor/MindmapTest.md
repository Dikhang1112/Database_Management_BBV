# Query Processor Unit Test Scenarios Mindmap

This mindmap represents the structural taxonomy of the Query Processor unit test scenarios, covering `Lexer`, `TokenStream`, `Token`, `SQLParser`, `AST`, `ASTNode` (`SelectASTNode`, `BinaryOpASTNode`, `IdentifierASTNode`, `LiteralASTNode`), `QueryOptimizer`, and `StatisticsManager` classes.

```mermaid
flowchart LR
    Root(("Query Processor Unit Tests"))

    %% Categories
    Cat1(["1. LexerTest"])
    Cat2(["2. TokenStreamTest"])
    Cat3(["3. TokenTest"])
    Cat4(["4. SQLParserTest"])
    Cat5(["5. ASTTest"])
    Cat6(["6. SelectASTNodeTest"])
    Cat7(["7. BinaryOpASTNodeTest"])
    Cat8(["8. IdentifierASTNodeTest"])
    Cat9(["9. LiteralASTNodeTest"])
    Cat10(["10. QueryOptimizerTest"])
    Cat11(["11. StatisticsManagerTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4
    Root --> Cat5
    Root --> Cat6
    Root --> Cat7
    Root --> Cat8
    Root --> Cat9
    Root --> Cat10
    Root --> Cat11

    %% LexerTest
    Cat1 --> TC01("TC-01 Tokenize Valid SQL")
    TC01 --> TC01A("Case-insensitive keywords")
    TC01 --> TC01B("Numeric and string literals")
    TC01 --> TC01C("Operators and punctuation")
    TC01 --> TC01D("Unterminated string literal")
    TC01 --> TC01E("Invalid character error")

    %% TokenStreamTest
    Cat2 --> TC02("TC-02 Consume Tokens")
    TC02 --> TC02A("LookAhead offset without advancing")
    TC02 --> TC02B("Empty stream boundary")
    TC02 --> TC02C("LookAhead out of bounds")
    TC02 --> TC02D("Consume past EOF")

    %% TokenTest
    Cat3 --> TC03("TC-03 Create Token")
    TC03 --> TC03A("Token equality comparison")
    TC03 --> TC03B("Default constructor initialization")

    %% SQLParserTest
    Cat4 --> TC04("TC-04 Parse Select Query")
    TC04 --> TC04A("Parse query without WHERE clause")
    TC04 --> TC04B("Syntax error missing FROM keyword")
    TC04 --> TC04C("Unexpected token error")
    TC04 --> TC04D("Empty token stream error")

    %% ASTTest
    Cat5 --> TC05("TC-05 Create AST")
    TC05 --> TC05A("Empty AST initialization")
    TC05 --> TC05B("Get root AST node")

    %% SelectASTNodeTest
    Cat6 --> TC06("TC-06 Build Select AST Node")
    TC06 --> TC06A("Null WHERE condition allowed")
    TC06 --> TC06B("Multiple projection fields")

    %% BinaryOpASTNodeTest
    Cat7 --> TC07("TC-07 Build Binary Op Node")
    TC07 --> TC07A("Nested binary operators evaluation")

    %% IdentifierASTNodeTest
    Cat8 --> TC08("TC-08 Build Identifier Node")
    TC08 --> TC08A("Qualified column identifier (table.col)")

    %% LiteralASTNodeTest
    Cat9 --> TC09("TC-09 Build Literal Node")
    TC09 --> TC09A("Integer literal node")
    TC09 --> TC09B("String literal node")
    TC09 --> TC09C("Boolean literal node")
    TC09 --> TC09D("NULL literal node")

    %% QueryOptimizerTest
    Cat10 --> TC10("TC-10 Generate Logical Plan")
    TC10 --> TC10A("Optimize logical plan to physical plan")
    TC10 --> TC10B("Estimate execution plan cost")
    TC10 --> TC10C("Select IndexScan when index exists")

    %% StatisticsManagerTest
    Cat11 --> TC11("TC-11 Estimate Table Cardinality")
    TC11 --> TC11A("Estimate predicate selectivity")
    TC11 --> TC11B("Table not found exception")
```

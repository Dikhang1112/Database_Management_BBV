# Query Processor Subsystem Unit Test Scenarios

This document defines all unit test scenarios for the `Query Processor` subsystem (`Lexer`, `TokenStream`, `Token`, `SQLParser`, `AST`, `SelectASTNode`, `BinaryOpASTNode`, `IdentifierASTNode`, `LiteralASTNode`, `QueryOptimizer`, and `StatisticsManager` classes), covering both positive (happy path) and negative (edge cases and exceptions) scenarios as mapped out in [MindmapTest.md](file:///d:/BBV/Database_Management_BBV/docs/unit_test/query_prossor/MindmapTest.md) and [SequeceTestCase.md](file:///d:/BBV/Database_Management_BBV/docs/unit_test/query_prossor/SequeceTestCase.md).

Each test scenario follows this standard format:
- **Test method:** JUnit method name.
- **Sequence diagram:** Corresponding sequence diagram ID in `SequeceTestCase.md`.
- **Input:** Input objects or parameters.
- **Steps:** Actions performed in the test.
- **Expected output:** Assertions or expected exceptions.

---

## 1. LexerTest

### TC-01. Tokenize Valid SQL (Happy Path)
- **Test method:** `tokenize_ShouldReturnTokenStream_WhenValidSqlIsProvided`
- **Sequence diagram:** `TC-01`
- **Input:** SQL string: `"SELECT id FROM users"`
- **Steps:**
  - Call `lexer.tokenize("SELECT id FROM users")`.
- **Expected output:**
  - Returns a `TokenStream` containing tokens: `KEYWORD("SELECT")`, `IDENTIFIER("id")`, `KEYWORD("FROM")`, `IDENTIFIER("users")`.

### TC-01A. Tokenize Case-Insensitive Keywords
- **Test method:** `tokenize_ShouldHandleKeywordsCaseInsensitively_WhenMixedCase`
- **Sequence diagram:** `TC-01A`
- **Input:** SQL string: `"sElEcT name FrOm users"`
- **Steps:**
  - Call `lexer.tokenize("sElEcT name FrOm users")`.
- **Expected output:**
  - Identifies `"sElEcT"` and `"FrOm"` as `KEYWORD` tokens.

### TC-01B. Tokenize Numeric and String Literals
- **Test method:** `tokenize_ShouldScanNumericAndStringLiterals_WhenPresent`
- **Sequence diagram:** `TC-01B`
- **Input:** SQL string: `"SELECT * FROM users WHERE age = 25 AND name = 'Alice'"`
- **Steps:**
  - Call `lexer.tokenize(...)`.
- **Expected output:**
  - `25` identified as numeric `LITERAL`.
  - `'Alice'` identified as string `LITERAL`.

### TC-01C. Tokenize Operators and Punctuation
- **Test method:** `tokenize_ShouldScanOperatorsAndPunctuation_WhenValid`
- **Sequence diagram:** `TC-01C`
- **Input:** SQL string: `"SELECT id, age FROM users WHERE age <= 30"`
- **Steps:**
  - Call `lexer.tokenize(...)`.
- **Expected output:**
  - `,` identified as punctuation, `<=` identified as `OPERATOR`.

### TC-01D. Tokenize - Unterminated String Literal
- **Test method:** `tokenize_ShouldThrowException_WhenUnterminatedStringLiteral`
- **Sequence diagram:** `TC-01D`
- **Input:** Invalid SQL string: `"SELECT * FROM users WHERE name = 'Alice"`
- **Steps:**
  - Call `lexer.tokenize("... 'Alice")`.
- **Expected output:**
  - Throws `LexerException` ("Unterminated string literal").

### TC-01E. Tokenize - Invalid Character Error
- **Test method:** `tokenize_ShouldThrowException_WhenInvalidCharacterEncountered`
- **Sequence diagram:** `TC-01E`
- **Input:** Invalid SQL string: `"SELECT @ FROM users"`
- **Steps:**
  - Call `lexer.tokenize("SELECT @ FROM users")`.
- **Expected output:**
  - Throws `LexerException` ("Unexpected character '@'").

---

## 2. TokenStreamTest

### TC-02. Consume Sequential Tokens (Happy Path)
- **Test method:** `consume_ShouldReturnNextTokenAndAdvance_WhenHasNext`
- **Sequence diagram:** `TC-02`
- **Input:** Token stream with `["SELECT", "id"]`
- **Steps:**
  - Call `stream.consume()`.
  - Call `stream.consume()`.
- **Expected output:**
  - First `consume()` returns `Token("KEYWORD", "SELECT")`.
  - Second `consume()` returns `Token("IDENTIFIER", "id")`.

### TC-02A. LookAhead Offset Without Advancing
- **Test method:** `lookAhead_ShouldReturnTokenAtOffset_WithoutAdvancingPointer`
- **Sequence diagram:** `TC-02A`
- **Input:** Token stream with `["SELECT", "id", "FROM"]`
- **Steps:**
  - Call `stream.lookAhead(1)`.
  - Call `stream.hasNext()`.
- **Expected output:**
  - `lookAhead(1)` returns `Token("IDENTIFIER", "id")`.
  - Pointer remains at index 0.

### TC-02B. Consume - Empty Stream Boundary
- **Test method:** `consume_ShouldReturnNullOrThrow_WhenStreamIsEmpty`
- **Sequence diagram:** `TC-02B`
- **Input:** Empty `TokenStream`
- **Steps:**
  - Call `stream.hasNext()`.
  - Call `stream.consume()`.
- **Expected output:**
  - `hasNext()` returns `false`.
  - `consume()` returns `null`.

### TC-02C. LookAhead Out of Bounds
- **Test method:** `lookAhead_ShouldReturnNull_WhenOffsetExceedsBounds`
- **Sequence diagram:** `TC-02C`
- **Input:** Token stream of length 2
- **Steps:**
  - Call `stream.lookAhead(5)`.
- **Expected output:**
  - `lookAhead(5)` returns `null`.

### TC-02D. Consume Past EOF
- **Test method:** `consume_ShouldReturnNull_WhenConsumingPastEOF`
- **Sequence diagram:** `TC-02D`
- **Input:** Token stream of size 1
- **Steps:**
  - Call `stream.consume()` twice.
- **Expected output:**
  - Second `consume()` returns `null`.

---

## 3. TokenTest

### TC-03. Create Token (Happy Path)
- **Test method:** `createToken_ShouldInitializeTypeAndValue_WhenValidArgsProvided`
- **Sequence diagram:** `TC-03`
- **Input:** `type = "KEYWORD"`, `value = "SELECT"`
- **Steps:**
  - `Token token = new Token("KEYWORD", "SELECT")`
- **Expected output:**
  - `token.getType()` returns `"KEYWORD"`.
  - `token.getValue()` returns `"SELECT"`.

### TC-03A. Token Equality Comparison
- **Test method:** `equals_ShouldCompareTokenTypeAndValue_WhenObjectsChecked`
- **Sequence diagram:** `TC-03A`
- **Input:** `t1 = Token("KEYWORD", "SELECT")`, `t2 = Token("KEYWORD", "SELECT")`
- **Steps:**
  - Compare `t1.equals(t2)`.
- **Expected output:**
  - `equals()` returns `true`.

### TC-03B. Default Constructor Initialization
- **Test method:** `defaultConstructor_ShouldInitializeWithNulls`
- **Sequence diagram:** `TC-03B`
- **Input:** None
- **Steps:**
  - `Token token = new Token()`
- **Expected output:**
  - `getType()` and `getValue()` return `null`.

---

## 4. SQLParserTest

### TC-04. Parse Select Query (Happy Path)
- **Test method:** `parse_ShouldGenerateParseTree_WhenValidSelectQuery`
- **Sequence diagram:** `TC-04`
- **Input:** TokenStream for `"SELECT name FROM users WHERE age > 18"`
- **Steps:**
  - Call `parser.parse(stream)`.
- **Expected output:**
  - Returns a non-null `ParseTree` / `AST` representing the query.

### TC-04A. Parse Query Without WHERE Clause
- **Test method:** `parse_ShouldParseQueryWithoutWhereClause_WhenWhereIsOmitted`
- **Sequence diagram:** `TC-04A`
- **Input:** TokenStream for `"SELECT name FROM users"`
- **Steps:**
  - Call `parser.parse(stream)`.
- **Expected output:**
  - Returns valid `AST` with null `whereCondition`.

### TC-04B. Syntax Error - Missing FROM Keyword
- **Test method:** `parse_ShouldThrowException_WhenSyntaxIsInvalid`
- **Sequence diagram:** `TC-04B`
- **Input:** Invalid TokenStream for `"SELECT name users"`
- **Steps:**
  - Call `parser.parse(stream)`.
- **Expected output:**
  - Throws `ParserException` ("Expected 'FROM' keyword").

### TC-04C. Syntax Error - Unexpected Token
- **Test method:** `parse_ShouldThrowException_WhenUnexpectedTokenEncountered`
- **Sequence diagram:** `TC-04C`
- **Input:** Invalid TokenStream for `"SELECT name WHERE users"`
- **Steps:**
  - Call `parser.parse(stream)`.
- **Expected output:**
  - Throws `ParserException` ("Unexpected token 'WHERE'").

### TC-04D. Syntax Error - Empty Token Stream
- **Test method:** `parse_ShouldThrowException_WhenStreamIsEmpty`
- **Sequence diagram:** `TC-04D`
- **Input:** Empty TokenStream
- **Steps:**
  - Call `parser.parse(stream)`.
- **Expected output:**
  - Throws `ParserException` ("Unexpected end of stream").

---

## 5. ASTTest

### TC-05. Create AST (Happy Path)
- **Test method:** `createAST_ShouldAssignRootNode_WhenProvided`
- **Sequence diagram:** `TC-05`
- **Input:** Mock `ASTNode`
- **Steps:**
  - `AST ast = new AST(mockNode)`
- **Expected output:**
  - `ast.getRootASTNode()` returns `mockNode`.

### TC-05A. Empty AST Initialization
- **Test method:** `defaultConstructor_ShouldInitializeNullRoot`
- **Sequence diagram:** `TC-05A`
- **Input:** None
- **Steps:**
  - `AST ast = new AST()`
- **Expected output:**
  - `ast.getRootASTNode()` returns `null`.

### TC-05B. Get Root AST Node
- **Test method:** `getRootASTNode_ShouldReturnAssignedRootNode`
- **Sequence diagram:** `TC-05B`
- **Input:** `SelectASTNode` instance
- **Steps:**
  - `AST ast = new AST(selectNode)`
- **Expected output:**
  - `getRootASTNode()` returns `selectNode`.

---

## 6. SelectASTNodeTest

### TC-06. Build Select AST Node (Happy Path)
- **Test method:** `buildSelectASTNode_ShouldStoreTableFieldsAndWhereCondition`
- **Sequence diagram:** `TC-06`
- **Input:** `tableName = "orders"`, fields = `["id", "total"]`, whereCondition = `mockWhereNode`
- **Steps:**
  - Construct `SelectASTNode`.
- **Expected output:**
  - `getTableName()` returns `"orders"`.
  - `getProjectionFields()` contains `"id"`, `"total"`.
  - `getWhereCondition()` returns `mockWhereNode`.

### TC-06A. Null WHERE Condition Allowed
- **Test method:** `buildSelectASTNode_ShouldAllowNullWhereCondition_WhenNoWhereClause`
- **Sequence diagram:** `TC-06A`
- **Input:** `tableName = "users"`, fields = `["name"]`, whereCondition = `null`
- **Steps:**
  - Construct `SelectASTNode`.
- **Expected output:**
  - `getWhereCondition()` returns `null`.

### TC-06B. Multiple Projection Fields
- **Test method:** `buildSelectASTNode_ShouldSupportMultipleProjectionFields`
- **Sequence diagram:** `TC-06B`
- **Input:** List of 5 field names
- **Steps:**
  - Construct `SelectASTNode`.
- **Expected output:**
  - `getProjectionFields()` has size 5.

---

## 7. BinaryOpASTNodeTest

### TC-07. Build Binary Operator AST Node (Happy Path)
- **Test method:** `buildBinaryOpNode_ShouldStoreOperatorAndLeftRightChildNodes`
- **Sequence diagram:** `TC-07`
- **Input:** operator = `">"`, left = `IdentifierASTNode("age")`, right = `LiteralASTNode("18", "INT")`
- **Steps:**
  - Construct `BinaryOpASTNode`.
- **Expected output:**
  - `getOperatorType()` returns `">"`.
  - `getLeftNode()` and `getRightNode()` return expected child nodes.

### TC-07A. Nested Binary Operators Evaluation
- **Test method:** `buildBinaryOpNode_ShouldSupportNestedBinaryOperators`
- **Sequence diagram:** `TC-07A`
- **Input:** BinaryOpNode `"AND"` with left = `(age > 18)` and right = `(status = 'ACTIVE')`
- **Steps:**
  - Construct nested `BinaryOpASTNode`.
- **Expected output:**
  - Both left and right sub-trees are correctly attached.

---

## 8. IdentifierASTNodeTest

### TC-08. Build Identifier AST Node (Happy Path)
- **Test method:** `buildIdentifierNode_ShouldStoreColumnName`
- **Sequence diagram:** `TC-08`
- **Input:** `columnName = "user_id"`
- **Steps:**
  - Construct `IdentifierASTNode("user_id")`.
- **Expected output:**
  - `getValue()` returns `"user_id"`.

### TC-08A. Qualified Column Identifier (table.col)
- **Test method:** `buildIdentifierNode_ShouldHandleQualifiedColumnNames`
- **Sequence diagram:** `TC-08A`
- **Input:** `columnName = "users.user_id"`
- **Steps:**
  - Construct `IdentifierASTNode("users.user_id")`.
- **Expected output:**
  - `getValue()` returns `"users.user_id"`.

---

## 9. LiteralASTNodeTest

### TC-09. Build Literal AST Node (Happy Path)
- **Test method:** `buildLiteralNode_ShouldStoreRawValueAndInferredType`
- **Sequence diagram:** `TC-09`
- **Input:** value = `"100"`, type = `"INT"`
- **Steps:**
  - Construct `LiteralASTNode("100", "INT")`.
- **Expected output:**
  - `getValue()` returns `"100"`.
  - `getInferredType()` returns `"INT"`.

### TC-09A. Integer Literal Node
- **Test method:** `buildLiteralNode_ShouldSupportIntegerLiteral`
- **Sequence diagram:** `TC-09A`
- **Input:** value = `"42"`, type = `"INT"`
- **Steps:**
  - Construct `LiteralASTNode("42", "INT")`.
- **Expected output:**
  - Type is `"INT"`.

### TC-09B. String Literal Node
- **Test method:** `buildLiteralNode_ShouldSupportStringLiteral`
- **Sequence diagram:** `TC-09B`
- **Input:** value = `"John"`, type = `"VARCHAR"`
- **Steps:**
  - Construct `LiteralASTNode("John", "VARCHAR")`.
- **Expected output:**
  - Type is `"VARCHAR"`.

### TC-09C. Boolean Literal Node
- **Test method:** `buildLiteralNode_ShouldSupportBooleanLiteral`
- **Sequence diagram:** `TC-09C`
- **Input:** value = `"true"`, type = `"BOOLEAN"`
- **Steps:**
  - Construct `LiteralASTNode("true", "BOOLEAN")`.
- **Expected output:**
  - Type is `"BOOLEAN"`.

### TC-09D. NULL Literal Node
- **Test method:** `buildLiteralNode_ShouldSupportNullLiteral`
- **Sequence diagram:** `TC-09D`
- **Input:** value = `"NULL"`, type = `"UNKNOWN"`
- **Steps:**
  - Construct `LiteralASTNode("NULL", "UNKNOWN")`.
- **Expected output:**
  - Value is `"NULL"`.

---

## 10. QueryOptimizerTest

### TC-10. Generate Logical Plan (Happy Path)
- **Test method:** `generateLogicalPlan_ShouldConvertASTToLogicalPlan`
- **Sequence diagram:** `TC-10`
- **Input:** Valid AST tree
- **Steps:**
  - Call `optimizer.generateLogicalPlan(ast)`.
- **Expected output:**
  - Returns a non-null `LogicalPlan`.

### TC-10A. Optimize Logical Plan to Physical Plan
- **Test method:** `optimize_ShouldTransformLogicalPlanToPhysicalPlan_WhenRulesApplied`
- **Sequence diagram:** `TC-10A`
- **Input:** `LogicalPlan`
- **Steps:**
  - Call `optimizer.optimize(logicalPlan)`.
- **Expected output:**
  - Returns optimized `PhysicalPlan`.

### TC-10B. Estimate Execution Plan Cost
- **Test method:** `estimateCost_ShouldComputeTotalPlanCost_WhenPhysicalPlanOptimized`
- **Sequence diagram:** `TC-10B`
- **Input:** `PhysicalPlan`
- **Steps:**
  - Call `physicalPlan.getEstimatedCost()`.
- **Expected output:**
  - Cost is computed and non-negative.

### TC-10C. Select IndexScan When Index Exists
- **Test method:** `optimize_ShouldSelectIndexScan_WhenIndexExistsOnFilterColumn`
- **Sequence diagram:** `TC-10C`
- **Input:** `LogicalPlan` with filter on indexed column `"email"`
- **Steps:**
  - Call `optimizer.optimize(logicalPlan)`.
- **Expected output:**
  - Physical plan uses `IndexScanNode` instead of `SeqScanNode`.

---

## 11. StatisticsManagerTest

### TC-11. Estimate Table Cardinality (Happy Path)
- **Test method:** `estimateCardinality_ShouldReturnTableRowCount`
- **Sequence diagram:** `TC-11`
- **Input:** `tableName = "users"`
- **Steps:**
  - Call `statsManager.estimateCardinality("users")`.
- **Expected output:**
  - Returns estimated row count (> 0).

### TC-11A. Estimate Predicate Selectivity
- **Test method:** `estimateSelectivity_ShouldComputeFractionalSelectivity_ForPredicate`
- **Sequence diagram:** `TC-11A`
- **Input:** `column = "status"`, `value = "ACTIVE"`
- **Steps:**
  - Call `statsManager.estimateSelectivity("status", "ACTIVE")`.
- **Expected output:**
  - Returns a value between `0.0` and `1.0`.

### TC-11B. Estimate Cardinality - Table Not Found
- **Test method:** `estimateCardinality_ShouldThrowException_WhenTableNotFound`
- **Sequence diagram:** `TC-11B`
- **Input:** Non-existing table `"unknown_table"`
- **Steps:**
  - Call `statsManager.estimateCardinality("unknown_table")`.
- **Expected output:**
  - Throws `TableNotFoundException`.

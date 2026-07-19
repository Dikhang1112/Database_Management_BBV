# SQL Parser Unit Test Scenarios

This document defines the unit test scenarios for `level_3.query_processor`.
The current scope covers SQL parser building blocks: `Token`, `TokenStream`, `AST`, and concrete `ASTNode` implementations.

Each test case follows this structure:

- **Test method:** the JUnit method name.
- **Sequence diagram:** related sequence diagram ID, if available.
- **Input:** objects or values used by the test.
- **Steps:** main test actions.
- **Expected output:** assertions expected from the test.

---

## 1. TokenStreamTest

### TC-01. Create Token Stream

- **Test method:** `createTokenStream_ShouldInitializeHeadPointer_WhenValidTokenListIsProvided`
- **Sequence diagram:** `SD-01`
- **Input:**
  - Token list:
    - `Token("KEYWORD", "SELECT")`
    - `Token("IDENTIFIER", "id")`
- **Steps:**
  - Create a new `TokenStream` with the token list.
  - Call `hasNext()`.
  - Call `lookAhead(0)`.
- **Expected output:**
  - `hasNext()` returns `true`.
  - `lookAhead(0)` returns `Token("KEYWORD", "SELECT")`.
  - The stream head pointer starts at index `0`.

### TC-02. Consume Sequential Tokens

- **Test method:** `consume_ShouldReturnNextToken_WhenMoreTokensExist`
- **Sequence diagram:** `SD-02`
- **Input:**
  - Token list:
    - `Token("KEYWORD", "SELECT")`
    - `Token("IDENTIFIER", "name")`
- **Steps:**
  - Create a new `TokenStream`.
  - Call `consume()` once.
  - Call `consume()` a second time.
  - Call `hasNext()`.
- **Expected output:**
  - First `consume()` returns `Token("KEYWORD", "SELECT")`.
  - Second `consume()` returns `Token("IDENTIFIER", "name")`.
  - `hasNext()` returns `false` after all tokens are consumed.

### TC-03. LookAhead Without Advancing

- **Test method:** `lookAhead_ShouldReturnTokenWithoutAdvancingPointer_WhenOffsetIsValid`
- **Sequence diagram:** `SD-03`
- **Input:**
  - Token list:
    - `Token("KEYWORD", "SELECT")`
    - `Token("IDENTIFIER", "age")`
- **Steps:**
  - Create a new `TokenStream`.
  - Call `lookAhead(0)`.
  - Call `lookAhead(1)`.
  - Call `consume()`.
- **Expected output:**
  - `lookAhead(0)` returns `Token("KEYWORD", "SELECT")`.
  - `lookAhead(1)` returns `Token("IDENTIFIER", "age")`.
  - `consume()` still returns the first token, proving `lookAhead()` does not advance the head pointer.

### TC-04. Empty Stream

- **Test method:** `emptyStream_ShouldBeEmpty`
- **Input:**
  - `new TokenStream()`
- **Steps:**
  - Call `hasNext()`.
  - Call `consume()`.
- **Expected output:**
  - `hasNext()` returns `false`.
  - `consume()` returns `null`.

### TC-05. LookAhead Boundary

- **Test method:** `lookAhead_ShouldReturnNull_WhenOffsetIsOutOfBounds`
- **Input:**
  - Token list:
    - `Token("KEYWORD", "WHERE")`
  - Offsets: `-1`, `1`, `99`
- **Steps:**
  - Create a new `TokenStream` with one token.
  - Call `lookAhead(-1)`.
  - Call `lookAhead(1)`.
  - Call `lookAhead(99)`.
- **Expected output:**
  - All out-of-bounds `lookAhead()` calls return `null`.

### TC-06. Consume After EOF

- **Test method:** `consume_ShouldReturnNull_WhenConsumedPastEOF`
- **Input:**
  - Token list:
    - `Token("KEYWORD", "FROM")`
- **Steps:**
  - Create a new `TokenStream`.
  - Call `consume()` to consume the only token.
  - Call `hasNext()`.
  - Call `consume()` again after EOF.
- **Expected output:**
  - `hasNext()` returns `false` after the only token is consumed.
  - `consume()` after EOF returns `null`.

---

## 2. TokenTest

### TC-07. Create Token

- **Test method:** `createToken_ShouldStoreTypeAndValue_WhenTokenIsConstructed`
- **Sequence diagram:** `SD-04`
- **Input:**
  - Type: `"KEYWORD"`
  - Value: `"SELECT"`
- **Steps:**
  - Create `Token("KEYWORD", "SELECT")`.
  - Call `getType()`.
  - Call `getValue()`.
- **Expected output:**
  - `getType()` returns `"KEYWORD"`.
  - `getValue()` returns `"SELECT"`.

### TC-08. Compare Tokens Equality

- **Test method:** `equals_ShouldReturnTrue_WhenTokensContainIdenticalTypeAndValue`
- **Sequence diagram:** `SD-05`
- **Input:**
  - `tokenA = Token("IDENTIFIER", "users")`
  - `tokenB = Token("IDENTIFIER", "users")`
- **Steps:**
  - Compare `tokenA.getType()` with `tokenB.getType()`.
  - Compare `tokenA.getValue()` with `tokenB.getValue()`.
- **Expected output:**
  - Both tokens have type `"IDENTIFIER"`.
  - Both tokens have value `"users"`.

### TC-09. Default Constructor

- **Test method:** `defaultConstructor_ShouldInitializeWithNullValues`
- **Input:**
  - `new Token()`
- **Steps:**
  - Call `getType()`.
  - Call `getValue()`.
- **Expected output:**
  - `getType()` returns `null`.
  - `getValue()` returns `null`.

---

## 3. ASTTest

### TC-10. Create AST

- **Test method:** `createAST_ShouldAssignRootNode_WhenRootNodeIsProvided`
- **Sequence diagram:** `SD-06`
- **Input:**
  - `mockRootNode`: mock or instance of `ASTNode`
- **Steps:**
  - Create `AST(mockRootNode)`.
  - Call `getRootASTNode()`.
- **Expected output:**
  - `getRootASTNode()` is not `null`.
  - `getRootASTNode()` returns the same `mockRootNode` instance.

### TC-11. Empty AST

- **Test method:** `defaultConstructor_ShouldInitializeWithNullRootNode`
- **Input:**
  - `new AST()`
- **Steps:**
  - Call `getRootASTNode()`.
- **Expected output:**
  - `getRootASTNode()` returns `null`.

### TC-12. Get Root Node

- **Test method:** `getRootASTNode_ShouldReturnAssignedRootNode`
- **Sequence diagram:** `SD-07`
- **Input:**
  - `mockRootNode`: mock or instance of `ASTNode`
- **Steps:**
  - Create `AST(mockRootNode)`.
  - Call `getRootASTNode()`.
- **Expected output:**
  - `getRootASTNode()` returns the same `mockRootNode` instance.

---

## 4. SelectASTNodeTest

### TC-13. Build Select AST Node

- **Test method:** `buildSelectASTNode_ShouldPopulateTableProjectionAndWhereClause_WhenParsingSelectStatement`
- **Sequence diagram:** `SD-08`
- **Input:**
  - Table name: `"employees"`
  - Projection fields: `["id", "name", "salary"]`
  - WHERE condition: `mockWhereCondition`
- **Steps:**
  - Create `SelectASTNode("employees", ["id", "name", "salary"], mockWhereCondition)`.
  - Call `getNodeName()`.
  - Call `getTableName()`.
  - Call `getProjectionFields()`.
  - Call `getWhereCondition()`.
- **Expected output:**
  - `getNodeName()` returns `"SelectASTNode"`.
  - `getTableName()` returns `"employees"`.
  - `getProjectionFields()` returns `["id", "name", "salary"]` in order.
  - `getWhereCondition()` returns the same `mockWhereCondition` instance.

### TC-14. Projection Fields

- **Test method:** `projectionFields_ShouldMaintainListOfColumns`
- **Input:**
  - Table name: `"my_table"`
  - Projection fields: `["column_a", "column_b"]`
  - WHERE condition: `null`
- **Steps:**
  - Create `SelectASTNode("my_table", ["column_a", "column_b"], null)`.
  - Call `getProjectionFields()`.
- **Expected output:**
  - Projection list size is `2`.
  - Projection list contains `"column_a"` and `"column_b"`.

### TC-15. WHERE Condition

- **Test method:** `whereCondition_ShouldReturnAssignedCondition`
- **Input:**
  - Table name: `"orders"`
  - Projection fields: `["order_id"]`
  - WHERE condition: `mockWhereCondition`
- **Steps:**
  - Create `SelectASTNode("orders", ["order_id"], mockWhereCondition)`.
  - Call `getWhereCondition()`.
- **Expected output:**
  - `getWhereCondition()` returns the same `mockWhereCondition` instance.

### TC-16. Build Complete AST

- **Test method:** `buildAST_ShouldConstructCompleteSyntaxTree_WhenParsingSelectStatement`
- **Sequence diagram:** `SD-12`
- **Input:**
  - Left operand: `IdentifierASTNode("age")`
  - Right operand: `LiteralASTNode("30", "INT")`
  - WHERE condition: `BinaryOpASTNode(">", leftOperand, rightOperand)`
  - Select node: `SelectASTNode("users", ["name", "age"], whereOp)`
- **Steps:**
  - Create the child AST nodes.
  - Create the `SelectASTNode`.
  - Create `AST(selectNode)`.
  - Inspect the root node and WHERE condition.
- **Expected output:**
  - `ast.getRootASTNode()` is an instance of `SelectASTNode`.
  - Root select node has `tableName = "users"`.
  - Root select node has `projectionFields = ["name", "age"]`.
  - `whereCondition` is an instance of `BinaryOpASTNode`.
  - `whereCondition.getOperatorType()` returns `">"`.
  - Left node is `IdentifierASTNode` with value `"age"`.
  - Right node is `LiteralASTNode` with value `"30"`.

### TC-17. Default Constructor

- **Test method:** `defaultConstructor_ShouldSetNodeName`
- **Input:**
  - `new SelectASTNode()`
- **Steps:**
  - Call `getNodeName()`.
  - Call `getTableName()`.
  - Call `getProjectionFields()`.
  - Call `getWhereCondition()`.
- **Expected output:**
  - `getNodeName()` returns `"SelectASTNode"`.
  - `getTableName()` returns `null`.
  - `getProjectionFields()` returns `null`.
  - `getWhereCondition()` returns `null`.

---

## 5. BinaryOpASTNodeTest

### TC-18. Build Binary Operator AST Node

- **Test method:** `buildBinaryOperatorASTNode_ShouldLinkLeftAndRightOperands_WhenParsingBinaryExpression`
- **Sequence diagram:** `SD-09`
- **Input:**
  - Operator: `"="`
  - Left node: `mockLeftNode`
  - Right node: `mockRightNode`
- **Steps:**
  - Create `BinaryOpASTNode("=", mockLeftNode, mockRightNode)`.
  - Call `getNodeName()`.
  - Call `getOperatorType()`.
  - Call `getLeftNode()`.
  - Call `getRightNode()`.
- **Expected output:**
  - `getNodeName()` returns `"BinaryOpASTNode"`.
  - `getOperatorType()` returns `"="`.
  - `getLeftNode()` returns the same `mockLeftNode` instance.
  - `getRightNode()` returns the same `mockRightNode` instance.

### TC-19. Child Operands Evaluation

- **Test method:** `getLeftNode_And_getRightNode_ShouldReturnChildNodes`
- **Input:**
  - Operator: `"AND"`
  - Left node: `mockLeftNode`
  - Right node: `mockRightNode`
- **Steps:**
  - Create `BinaryOpASTNode("AND", mockLeftNode, mockRightNode)`.
  - Call `getLeftNode()`.
  - Call `getRightNode()`.
- **Expected output:**
  - `getLeftNode()` returns the same `mockLeftNode` instance.
  - `getRightNode()` returns the same `mockRightNode` instance.

### TC-20. Default Constructor

- **Test method:** `defaultConstructor_ShouldSetNodeName`
- **Input:**
  - `new BinaryOpASTNode()`
- **Steps:**
  - Call `getNodeName()`.
  - Call `getOperatorType()`.
  - Call `getLeftNode()`.
  - Call `getRightNode()`.
- **Expected output:**
  - `getNodeName()` returns `"BinaryOpASTNode"`.
  - `getOperatorType()` returns `null`.
  - `getLeftNode()` returns `null`.
  - `getRightNode()` returns `null`.

---

## 6. IdentifierASTNodeTest

### TC-21. Build Identifier AST Node

- **Test method:** `buildIdentifierASTNode_ShouldPreserveQualifiedIdentifier_WhenParsingIdentifier`
- **Sequence diagram:** `SD-10`
- **Input:**
  - Identifier value: `"users.user_id"`
- **Steps:**
  - Create `IdentifierASTNode("users.user_id")`.
  - Call `getNodeName()`.
  - Call `getValue()`.
- **Expected output:**
  - `getNodeName()` returns `"IdentifierASTNode"`.
  - `getValue()` returns `"users.user_id"`.

### TC-22. Column Identifier

- **Test method:** `columnIdentifier_ShouldStoreSimpleColumnName`
- **Input:**
  - Identifier value: `"email"`
- **Steps:**
  - Create `IdentifierASTNode("email")`.
  - Call `getValue()`.
- **Expected output:**
  - `getValue()` returns `"email"`.

### TC-23. Default Constructor

- **Test method:** `defaultConstructor_ShouldInitializeWithNullValue`
- **Input:**
  - `new IdentifierASTNode()`
- **Steps:**
  - Call `getNodeName()`.
  - Call `getValue()`.
- **Expected output:**
  - `getNodeName()` returns `"IdentifierASTNode"`.
  - `getValue()` returns `null`.

---

## 7. LiteralASTNodeTest

### TC-24. Build Literal AST Node

- **Test method:** `buildLiteralASTNode_ShouldInferDataType_WhenParsingLiteralValue`
- **Sequence diagram:** `SD-11`
- **Input:**
  - Raw value: `"100"`
  - Inferred type: `"INTEGER"`
- **Steps:**
  - Create `LiteralASTNode("100", "INTEGER")`.
  - Call `getNodeName()`.
  - Call `getValue()`.
  - Call `getInferredType()`.
- **Expected output:**
  - `getNodeName()` returns `"LiteralASTNode"`.
  - `getValue()` returns `"100"`.
  - `getInferredType()` returns `"INTEGER"`.

### TC-25. Integer Literal

- **Test method:** `integerLiteral_ShouldStoreIntegerDataType`
- **Input:**
  - Raw value: `"42"`
  - Inferred type: `"INT"`
- **Steps:**
  - Create `LiteralASTNode("42", "INT")`.
  - Call `getValue()`.
  - Call `getInferredType()`.
- **Expected output:**
  - `getValue()` returns `"42"`.
  - `getInferredType()` returns `"INT"`.

### TC-26. String Literal

- **Test method:** `stringLiteral_ShouldStoreStringDataType`
- **Input:**
  - Raw value: `"'John Doe'"`
  - Inferred type: `"VARCHAR"`
- **Steps:**
  - Create `LiteralASTNode("'John Doe'", "VARCHAR")`.
  - Call `getValue()`.
  - Call `getInferredType()`.
- **Expected output:**
  - `getValue()` returns `"'John Doe'"`.
  - `getInferredType()` returns `"VARCHAR"`.

### TC-27. Boolean Literal

- **Test method:** `booleanLiteral_ShouldStoreBooleanDataType`
- **Input:**
  - Raw value: `"TRUE"`
  - Inferred type: `"BOOLEAN"`
- **Steps:**
  - Create `LiteralASTNode("TRUE", "BOOLEAN")`.
  - Call `getValue()`.
  - Call `getInferredType()`.
- **Expected output:**
  - `getValue()` returns `"TRUE"`.
  - `getInferredType()` returns `"BOOLEAN"`.

### TC-28. NULL Literal

- **Test method:** `nullLiteral_ShouldStoreNullDataType`
- **Input:**
  - Raw value: `"NULL"`
  - Inferred type: `"NULL"`
- **Steps:**
  - Create `LiteralASTNode("NULL", "NULL")`.
  - Call `getValue()`.
  - Call `getInferredType()`.
- **Expected output:**
  - `getValue()` returns `"NULL"`.
  - `getInferredType()` returns `"NULL"`.

### TC-29. Default Constructor

- **Test method:** `defaultConstructor_ShouldInitializeWithNullFields`
- **Input:**
  - `new LiteralASTNode()`
- **Steps:**
  - Call `getNodeName()`.
  - Call `getValue()`.
  - Call `getInferredType()`.
- **Expected output:**
  - `getNodeName()` returns `"LiteralASTNode"`.
  - `getValue()` returns `null`.
  - `getInferredType()` returns `null`.

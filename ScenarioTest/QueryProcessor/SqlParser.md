# Unit Test Scenarios - Lexical Stream & AST Hierarchy

## 1. TokenStreamTests

### Create Stream
- **Given**
  - A valid list of tokens.
- **When**
  - A new `TokenStream` is created.
- **Then**
  - The token list is initialized.
  - The head pointer starts at position `0`.
  - The stream is not empty.

### Empty Stream
- **Given**
  - An empty token list.
- **When**
  - A `TokenStream` is created.
- **Then**
  - `HasNext()` returns `false`.
  - `Consume()` returns EOF or throws an exception.
  - `LookAhead()` returns EOF.

### Consume Sequential Tokens
- **Given**
  - A stream containing multiple SQL tokens.
- **When**
  - `Consume()` is called repeatedly.
- **Then**
  - Tokens are returned in the original order.
  - The head pointer advances correctly.
  - No token is skipped or duplicated.

### LookAhead Boundary
- **Given**
  - A token stream with limited tokens.
- **When**
  - `LookAhead()` is called with different offsets.
- **Then**
  - The correct token is returned.
  - Looking beyond the end returns EOF.
  - The head pointer is not modified.

### Consume After EOF
- **Given**
  - All tokens have already been consumed.
- **When**
  - `Consume()` is called again.
- **Then**
  - EOF is returned or an EndOfStreamException is thrown.
  - The internal state remains unchanged.

---

## 2. TokenTests

### Create Token
- **Given**
  - A valid token type and token value.
- **When**
  - A new `Token` is created.
- **Then**
  - The token type is stored correctly.
  - The token value is stored correctly.

### Compare Tokens
- **Given**
  - Two identical tokens.
- **When**
  - The tokens are compared.
- **Then**
  - They are considered equal.

### Invalid Token
- **Given**
  - An invalid lexical character.
- **When**
  - The lexer creates a token.
- **Then**
  - The token type is marked as `INVALID`.

---

## 3. ASTTests

### Create AST
- **Given**
  - A valid root AST node.
- **When**
  - A new AST is created.
- **Then**
  - The root node is assigned correctly.

### Empty AST
- **Given**
  - No root node.
- **When**
  - An empty AST is created.
- **Then**
  - The root node is null.

### Replace Root
- **Given**
  - An existing AST.
- **When**
  - The root node is replaced.
- **Then**
  - The new root becomes the active root.

---

## 4. SelectASTNodeTests

### Build Select Node
- **Given**
  - A valid SELECT statement.
- **When**
  - The parser builds the AST.
- **Then**
  - The table name is stored.
  - Projection fields are created.
  - The WHERE node is linked correctly.

### Projection Fields
- **Given**
  - Multiple projected columns.
- **When**
  - The Select AST node is created.
- **Then**
  - All projection fields are stored in order.

### Wildcard Projection
- **Given**
  - A SELECT * statement.
- **When**
  - The AST is created.
- **Then**
  - The wildcard projection is represented correctly.

### WHERE Condition
- **Given**
  - A SELECT statement with a WHERE clause.
- **When**
  - The AST is created.
- **Then**
  - A WHERE subtree exists.

### Missing WHERE Clause
- **Given**
  - A SELECT statement without a WHERE clause.
- **When**
  - The AST is created.
- **Then**
  - The WHERE node is null.

---

## 5. BinaryOperatorASTNodeTests

### Create Binary Operator
- **Given**
  - A binary comparison expression.
- **When**
  - The AST node is created.
- **Then**
  - The operator is stored correctly.

### Left Operand
- **Given**
  - A binary expression.
- **When**
  - The AST node is created.
- **Then**
  - The left child references the correct AST node.

### Right Operand
- **Given**
  - A binary expression.
- **When**
  - The AST node is created.
- **Then**
  - The right child references the correct AST node.

### Nested Binary Expressions
- **Given**
  - Multiple logical expressions combined with AND/OR.
- **When**
  - The AST is constructed.
- **Then**
  - The binary operator hierarchy is built correctly.

### Invalid Operator
- **Given**
  - An unsupported operator.
- **When**
  - The parser constructs the AST.
- **Then**
  - A parser or semantic error is reported.

---

## 6. IdentifierASTNodeTests

### Create Identifier
- **Given**
  - A valid column identifier.
- **When**
  - The AST node is created.
- **Then**
  - The identifier value is stored correctly.

### Qualified Identifier
- **Given**
  - A fully-qualified identifier.
- **When**
  - The AST node is created.
- **Then**
  - Schema, table, and column names are preserved.

### Invalid Identifier
- **Given**
  - An invalid identifier format.
- **When**
  - The parser processes the statement.
- **Then**
  - A parser error is generated.

### Reserved Keyword
- **Given**
  - A reserved SQL keyword used as an identifier.
- **When**
  - The parser validates the identifier.
- **Then**
  - A syntax or semantic error is reported.

---

## 7. LiteralASTNodeTests

### Integer Literal
- **Given**
  - A valid integer literal.
- **When**
  - The AST node is created.
- **Then**
  - The value and inferred type are correct.

### Decimal Literal
- **Given**
  - A decimal literal.
- **When**
  - The AST node is created.
- **Then**
  - The inferred type is Decimal.

### String Literal
- **Given**
  - A quoted string literal.
- **When**
  - The AST node is created.
- **Then**
  - The inferred type is String.

### Boolean Literal
- **Given**
  - A boolean literal.
- **When**
  - The AST node is created.
- **Then**
  - The inferred type is Boolean.

### NULL Literal
- **Given**
  - A NULL literal.
- **When**
  - The AST node is created.
- **Then**
  - The inferred type is NULL.

### Date Literal
- **Given**
  - A valid date literal.
- **When**
  - The AST node is created.
- **Then**
  - The inferred type is Date.

### Invalid Literal
- **Given**
  - A malformed literal.
- **When**
  - The lexer or parser processes the input.
- **Then**
  - A lexical or syntax error is generated.
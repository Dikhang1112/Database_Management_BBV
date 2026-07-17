# Sequence Diagrams - Lexical Stream & AST Hierarchy

---

## SD-01. CreateTokenStream_ShouldInitializeHeadPointer_WhenValidTokenListIsProvided

```mermaid
sequenceDiagram
    title CreateTokenStream_ShouldInitializeHeadPointer_WhenValidTokenListIsProvided

    participant Test
    participant TokenStream

    Test->>TokenStream: Create(tokenList)
    TokenStream-->>Test: TokenStream Initialized
```

---

## SD-02. Consume_ShouldReturnNextToken_WhenMoreTokensExist

```mermaid
sequenceDiagram
    title Consume_ShouldReturnNextToken_WhenMoreTokensExist

    participant Test
    participant TokenStream
    participant Token

    Test->>TokenStream: Consume()
    TokenStream->>Token: Read Current Token
    Token-->>TokenStream: Token
    TokenStream-->>Test: Return Token
```

---

## SD-03. LookAhead_ShouldReturnTokenWithoutAdvancingPointer_WhenOffsetIsValid

```mermaid
sequenceDiagram
    title LookAhead_ShouldReturnTokenWithoutAdvancingPointer_WhenOffsetIsValid

    participant Test
    participant TokenStream
    participant Token

    Test->>TokenStream: LookAhead(offset)
    TokenStream->>Token: Read Token
    Token-->>TokenStream: Token
    TokenStream-->>Test: Return Token
```

---

## SD-04. CreateToken_ShouldStoreTypeAndValue_WhenTokenIsConstructed

```mermaid
sequenceDiagram
    title CreateToken_ShouldStoreTypeAndValue_WhenTokenIsConstructed

    participant Test
    participant Token

    Test->>Token: Create(type,value)
    Token-->>Test: Token Created
```

---

## SD-05. Equals_ShouldReturnTrue_WhenTokensContainIdenticalTypeAndValue

```mermaid
sequenceDiagram
    title Equals_ShouldReturnTrue_WhenTokensContainIdenticalTypeAndValue

    participant Test
    participant TokenA
    participant TokenB

    Test->>TokenA: Equals(TokenB)
    TokenA->>TokenB: Compare()
    TokenB-->>TokenA: Match Result
    TokenA-->>Test: True
```

---

## SD-06. CreateAST_ShouldAssignRootNode_WhenRootNodeIsProvided

```mermaid
sequenceDiagram
    title CreateAST_ShouldAssignRootNode_WhenRootNodeIsProvided

    participant Test
    participant AST
    participant ASTNode

    Test->>AST: Create(rootNode)
    AST->>ASTNode: Assign Root
    ASTNode-->>AST: Root Assigned
    AST-->>Test: AST Created
```

---

## SD-07. SetRoot_ShouldReplaceExistingRoot_WhenNewRootNodeIsAssigned

```mermaid
sequenceDiagram
    title SetRoot_ShouldReplaceExistingRoot_WhenNewRootNodeIsAssigned

    participant Test
    participant AST
    participant ASTNode

    Test->>AST: SetRoot(newRoot)
    AST->>ASTNode: Replace Root
    ASTNode-->>AST: Updated
    AST-->>Test: Success
```

---

## SD-08. BuildSelectASTNode_ShouldPopulateTableProjectionAndWhereClause_WhenParsingSelectStatement

```mermaid
sequenceDiagram
    title BuildSelectASTNode_ShouldPopulateTableProjectionAndWhereClause_WhenParsingSelectStatement

    participant Parser
    participant SelectASTNode
    participant ASTNode

    Parser->>SelectASTNode: Create()
    Parser->>SelectASTNode: Assign Table Name
    Parser->>SelectASTNode: Assign Projection Fields
    Parser->>ASTNode: Create WHERE Node
    ASTNode-->>SelectASTNode: WHERE Node
    SelectASTNode-->>Parser: Completed
```

---

## SD-09. BuildBinaryOperatorASTNode_ShouldLinkLeftAndRightOperands_WhenParsingBinaryExpression

```mermaid
sequenceDiagram
    title BuildBinaryOperatorASTNode_ShouldLinkLeftAndRightOperands_WhenParsingBinaryExpression

    participant Parser
    participant BinaryOpASTNode
    participant ASTNode

    Parser->>BinaryOpASTNode: Create(operator)
    Parser->>ASTNode: Create Left Operand
    ASTNode-->>BinaryOpASTNode: Left Child
    Parser->>ASTNode: Create Right Operand
    ASTNode-->>BinaryOpASTNode: Right Child
    BinaryOpASTNode-->>Parser: Completed
```

---

## SD-10. BuildIdentifierASTNode_ShouldPreserveQualifiedIdentifier_WhenParsingIdentifier

```mermaid
sequenceDiagram
    title BuildIdentifierASTNode_ShouldPreserveQualifiedIdentifier_WhenParsingIdentifier

    participant Parser
    participant IdentifierASTNode

    Parser->>IdentifierASTNode: Create(identifier)
    IdentifierASTNode-->>Parser: Identifier Node
```

---

## SD-11. BuildLiteralASTNode_ShouldInferDataType_WhenParsingLiteralValue

```mermaid
sequenceDiagram
    title BuildLiteralASTNode_ShouldInferDataType_WhenParsingLiteralValue

    participant Lexer
    participant LiteralASTNode

    Lexer->>LiteralASTNode: Create(literal)
    LiteralASTNode->>LiteralASTNode: Infer Type
    LiteralASTNode-->>Lexer: Literal Node
```

---

## SD-12. BuildAST_ShouldConstructCompleteSyntaxTree_WhenParsingSelectStatement

```mermaid
sequenceDiagram
    title BuildAST_ShouldConstructCompleteSyntaxTree_WhenParsingSelectStatement

    participant Parser
    participant IdentifierASTNode
    participant LiteralASTNode
    participant BinaryOpASTNode
    participant SelectASTNode
    participant AST

    Parser->>IdentifierASTNode: Create Identifier
    IdentifierASTNode-->>Parser: Identifier

    Parser->>LiteralASTNode: Create Literal
    LiteralASTNode-->>Parser: Literal

    Parser->>BinaryOpASTNode: Create Binary Expression
    BinaryOpASTNode-->>Parser: Binary Node

    Parser->>SelectASTNode: Create Select Node
    SelectASTNode-->>Parser: Select Node

    Parser->>AST: Assign Root Node
    AST-->>Parser: AST Completed
```
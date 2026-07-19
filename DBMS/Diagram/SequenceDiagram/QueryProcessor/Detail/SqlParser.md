```mermaid
sequenceDiagram
    autonumber
    actor Client as Client / QueryProcessor
    participant Parser as SQLParser
    participant TS as TokenStream
    participant T as Token
    participant AST as AST
    participant SelectNode as SelectASTNode
    participant BinOpNode as BinaryOpASTNode
    participant IdNode as IdentifierASTNode
    participant LitNode as LiteralASTNode

    %% =====================================================
    %% GIAI ĐOẠN 1: LEXICAL STREAM CONSUMPTION
    %% (Duyệt và lấy các Token từ TokenStream)
    %% =====================================================
    Client->>Parser: parse(tokenStream)
    activate Parser
    
    Note over Parser, TS: Phase 1: Stream Breakdown & Token Inspection
    Parser->>TS: hasNext()
    activate TS
    TS-->>Parser: boolean (true)
    deactivate TS

    Parser->>TS: lookAhead(0)
    activate TS
    TS-->>Parser: Token (SELECT)
    deactivate TS

    Parser->>TS: consume()
    activate TS
    TS-->>Parser: Token (SELECT)
    deactivate TS
    
    Parser->>T: getType()
    activate T
    T-->>Parser: "KEYWORD"
    deactivate T

    Parser->>T: getValue()
    activate T
    T-->>Parser: "SELECT"
    deactivate T

    %% =====================================================
    %% GIAI ĐOẠN 2: AST NODE TREE CONSTRUCTION
    %% (Xây dựng các nút cây AST đa hình & biểu thức điều kiện)
    %% =====================================================
    Note over Parser, LitNode: Phase 2: Building AST Nodes & Expression Tree

    %% Parsing Identifier AST Node
    Parser->>TS: consume()
    activate TS
    TS-->>Parser: Token (Identifier: "age")
    deactivate TS

    create participant IdNode as IdentifierASTNode
    Parser->>IdNode: new IdentifierASTNode("age")
    IdNode-->>Parser: instance
    Parser->>IdNode: getValue()
    activate IdNode
    IdNode-->>Parser: "age"
    deactivate IdNode

    %% Parsing Literal AST Node
    Parser->>TS: consume()
    activate TS
    TS-->>Parser: Token (Literal: "18")
    deactivate TS

    create participant LitNode as LiteralASTNode
    Parser->>LitNode: new LiteralASTNode("18", "INTEGER")
    LitNode-->>Parser: instance
    Parser->>LitNode: getValue()
    activate LitNode
    LitNode-->>Parser: "18"
    deactivate LitNode
    Parser->>LitNode: getInferredType()
    activate LitNode
    LitNode-->>Parser: "INTEGER"
    deactivate LitNode

    %% Parsing Binary Operation AST Node
    create participant BinOpNode as BinaryOpASTNode
    Parser->>BinOpNode: new BinaryOpASTNode(">", leftNode: IdentifierASTNode, rightNode: LiteralASTNode)
    BinOpNode-->>Parser: instance
    Parser->>BinOpNode: getOperatorType()
    activate BinOpNode
    BinOpNode-->>Parser: ">"
    deactivate BinOpNode
    Parser->>BinOpNode: getLeftNode()
    activate BinOpNode
    BinOpNode-->>Parser: IdentifierASTNode ("age")
    deactivate BinOpNode
    Parser->>BinOpNode: getRightNode()
    activate BinOpNode
    BinOpNode-->>Parser: LiteralASTNode ("18")
    deactivate BinOpNode

    %% Parsing Select AST Node
    create participant SelectNode as SelectASTNode
    Parser->>SelectNode: new SelectASTNode("users", projectionFields, whereCondition: BinaryOpASTNode)
    SelectNode-->>Parser: instance
    Parser->>SelectNode: getTableName()
    activate SelectNode
    SelectNode-->>Parser: "users"
    deactivate SelectNode
    Parser->>SelectNode: getProjectionFields()
    activate SelectNode
    SelectNode-->>Parser: ["id", "name", "age"]
    deactivate SelectNode
    Parser->>SelectNode: getWhereCondition()
    activate SelectNode
    SelectNode-->>Parser: BinaryOpASTNode (whereCondition)
    deactivate SelectNode

    %% =====================================================
    %% GIAI ĐOẠN 3: AST TREE ROOT ENCAPSULATION
    %% (Đóng gói nút gốc vào đối tượng AST)
    %% =====================================================
    Note over Parser, AST: Phase 3: Root AST Encapsulation
    create participant AST as AST
    Parser->>AST: new AST(rootASTNode: SelectASTNode)
    AST-->>Parser: instance

    Parser->>AST: getRootASTNode()
    activate AST
    AST-->>Parser: ASTNode (SelectASTNode)
    deactivate AST

    Parser-->>Client: AST
    deactivate Parser
```

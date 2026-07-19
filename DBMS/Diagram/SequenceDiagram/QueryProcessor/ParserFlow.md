```mermaid
sequenceDiagram
    autonumber
    
    %% --- FIXED ARCHITECTURAL OBJECTS (TOP ROW ONLY) ---
    participant QE as QueryIngestionEngine
    participant SP as SyntaxParser
    participant SL as SqlLexer
    participant AST as AbstractSyntaxTree

    %% --- WORKFLOW: SQL PARSING PHASE ---
    Note over QE, AST: PHASE 1: SQL PARSING LAYER (LEXICAL & SYNTAX ANALYSIS)
    
    QE->>SP: parseTree() - Compile sql string
    activate SP
    
    Note over SP: Parser initiates top-down grammar validation loop
    
    loop For Each SQL Word / Special Character
        SP->>SL: nextToken()
        activate SL
        Note over SL: Scans char-by-char via currentPosition pointer
        SL-->>SP: Return encoded Token instance (e.g., KEYWORD, IDENTIFIER)
        deactivate SL
        
        Note over SP: Evaluates Token sequence against strict SQL rules.
    end

    SP->>AST: Instantiates root node and aggregates child sub-nodes
    activate AST
    AST-->>SP: Return fully-formed AbstractSyntaxTree reference
    deactivate AST

    SP-->>QE: Return compiled AbstractSyntaxTree (AST)
    deactivate SP
```

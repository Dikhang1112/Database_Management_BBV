```mermaid
sequenceDiagram
    autonumber
    actor Client as "Client / Application"
    participant QP as QueryProcessor
    participant L as Lexer
    participant P as SQLParser
    participant AB as ASTBuilder
    participant SA as SemanticAnalyzer
    participant AST as AST
    participant NR as NameResolver
    participant TC as TypeChecker
    participant MM as MetadataModule
    participant QO as QueryOptimizer
    participant LP as LogicalPlan
    participant PG as PlanGenerator
    participant CE as CostEstimator
    participant SM as StatisticsManager
    participant EE as ExecutionEngine

    Client->>QP: executeIngestion(sqlText)
    activate QP
    
    %% =====================================================
    %% PHASE 1: LEXICAL ANALYSIS
    %% =====================================================
    QP->>L: tokenize(sqlText)
    activate L
    Note over L: Scans identifiers & numbers<br/>(scanIdentifier, scanNumber)
    L-->>QP: TokenStream
    deactivate L

    %% =====================================================
    %% PHASE 2: SYNTAX ANALYSIS
    %% =====================================================
    QP->>P: parse(TokenStream)
    activate P
    Note over P: parseSelectClause()<br/>parseWhereClause()
    P-->>QP: ParseTree
    deactivate P

    %% =====================================================
    %% PHASE 3: AST CONSTRUCTION
    %% =====================================================
    QP->>AB: buildAST(ParseTree)
    activate AB
    Note over AB: mapToLogicalNode(ParseTreeNode)
    AB-->>QP: AST
    deactivate AB

    %% =====================================================
    %% PHASE 4: SEMANTIC ANALYSIS
    %% =====================================================
    QP->>SA: validate(AST)
    activate SA
    
    SA->>AST: getRootASTNode()
    activate AST
    AST-->>SA: ASTNode
    deactivate AST

    SA->>NR: resolveIdentifiers(ASTNode)
    activate NR
    NR->>MM: checkTableExists(table)
    activate MM
    MM-->>NR: boolean
    deactivate MM
    NR->>MM: checkColumnExists(table, col)
    activate MM
    MM-->>NR: boolean
    deactivate MM
    NR-->>SA: void
    deactivate NR

    SA->>TC: checkTypeConformity(ASTNode)
    activate TC
    TC->>MM: fetchTableSchema(table)
    activate MM
    MM-->>TC: TableSchema
    deactivate MM
    TC-->>SA: void
    deactivate TC

    SA-->>QP: boolean (isValid)
    deactivate SA

    alt AST is Invalid
        QP-->>Client: Throw SemanticException
    else AST is Valid
        %% =====================================================
        %% PHASE 5: QUERY OPTIMIZATION (CBO ENGINE)
        %% =====================================================
        QP->>QO: generateLogicalPlan(AST)
        activate QO
        QO-->>QP: LogicalPlan
        deactivate QO

        QP->>QO: optimize(LogicalPlan)
        activate QO
        
        QO->>LP: getLogicalRoot()
        activate LP
        LP-->>QO: LogicalPlanNode
        deactivate LP

        QO->>PG: enumeratePhysicalPlans(LogicalPlan)
        activate PG
        PG-->>QO: List<PhysicalPlan>
        deactivate PG

        loop For each PhysicalPlan candidate
            QO->>CE: calculateIoCost(LogicalPlanNode)
            activate CE
            CE->>SM: estimateCardinality(tableName)
            activate SM
            SM-->>CE: cardinality
            deactivate SM
            CE-->>QO: ioCost
            deactivate CE

            QO->>CE: calculateCpuCost(LogicalPlanNode)
            activate CE
            CE->>SM: estimateSelectivity(column, value)
            activate SM
            SM-->>CE: selectivity
            deactivate SM
            CE-->>QO: cpuCost
            deactivate CE
        end

        QO->>MM: fetchTableSchema(table) (Inspect B+ Tree Index configs)
        activate MM
        MM-->>QO: TableSchema (Index configurations)
        deactivate MM

        QO->>PG: selectBestPlan(plans)
        activate PG
        PG-->>QO: PhysicalPlan (lowest cost)
        deactivate PG

        QO-->>QP: PhysicalPlan
        deactivate QO
        
        QP-->>Client: PhysicalPlan
    end
    deactivate QP

    %% =====================================================
    %% PHASE 6: PHYSICAL PLAN EXECUTION
    %% =====================================================
    Client->>EE: executePhysicalPlan(PhysicalPlan)
    activate EE
    EE-->>Client: ResultSet
    deactivate EE
```

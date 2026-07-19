```mermaid
sequenceDiagram
    autonumber
    
    %% --- FIXED ARCHITECTURAL OBJECTS (TOP ROW ONLY) ---
    %% Elements matched exactly with the Class Diagram
    participant QE as QueryIngestionEngine
    participant QV as QueryValidator
    participant QO as QueryOptimizer
    participant MC as ObjectMetadataCache
    participant PE as PlanExecutor
    participant CTX as SessionContext

    %% --- WORKFLOW: QUERY INGESTION PHASE ---
    Note over QE, PE: QUERY INGESTION LAYER (VALIDATION, OPTIMIZATION & DISPATCH)
    
    Note over QE: Facade holding the validated AST Pointer
    
    %% --- STEP 1: SEMANTIC VALIDATION & BINDING ---
    QE->>QV: validateAst(ast, session)
    activate QV
    
    QV->>MC: getByObjectName(tableNameString)
    activate MC
    Note over MC: Verifies if target table/column entities exist on RAM Cache
    MC-->>QV: Return system ObjectID verification status
    deactivate MC
    
    QV->>CTX: getCurrentUser()
    activate CTX
    CTX-->>QV: Return current user role privileges
    deactivate CTX
        
    %% FIXED: Updates the structural '-boolean isSemantic' attribute declared in Class Diagram
    QV->>QV: evaluateSemanticsAndPermissions()
    Note over QV: Sets internal state: isSemantic = true/false <br/>based on catalog mapping & user privileges
    
    %% FIXED: Returns the exact result of the validateAst method signature
    QV-->>QE: Return boolean (isSemantic value)
    deactivate QV

    %% --- STEP 2: ALTERNATIVE FLOW BASED ON ISSEMANTIC VALUE ---
    alt isSemantic == false
        Note over QE: Security/Schema Violation detected!
        QE-->>User: Abort query & throw Exception
    else isSemantic == true
        
        %% --- STEP 3: COST-BASED OPTIMIZATION (CBO) ---
        QE->>QO: optimizePlan(ast)
        activate QO
        Note over QO: Evaluates disk page footprint statistics to find lowest cost path
        QO-->>QE: Return ExecutionPlan instance
        deactivate QO

        %% --- STEP 4: DISPATCH TO EXECUTION CORE ---
        QE->>PE: executePlan(plan, session)
        activate PE
        Note over PE: Handoff boundary: Plan is pushed down to open the Volcano streaming operators
        PE-->>QE: Execution Pipeline Initialized Successfully
        deactivate PE
    end
```

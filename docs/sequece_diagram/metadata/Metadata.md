```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant CM as CatalogManager
    participant DB as Database
    participant S as Schema
    participant T as Table
    participant C as Column
    participant CT as Constraint
    participant I as Index
    participant SP as StoredProcedure

    %% -----------------------------------------------------
    %% Phase 1: Database, Schema, and Table Setup
    %% -----------------------------------------------------
    Note over Client, DB: Phase 1: Database & Schema Initialization
    Client->>CM: createDatabase("sales_db")
    activate CM
    create participant DB
    CM-->>DB: new Database("sales_db")
    CM-->>Client: Database instance
    deactivate CM

    Client->>DB: createSchema("public")
    activate DB
    create participant S
    DB-->>S: new Schema("public")
    DB-->>Client: Schema instance
    deactivate DB

    %% -----------------------------------------------------
    %% Phase 2: Table Creation with Columns, Constraints, & Indexes
    %% -----------------------------------------------------
    Note over Client, T: Phase 2: Table & Schema definition
    Client->>S: createTable("orders")
    activate S
    create participant T
    S-->>T: new Table("orders")
    S-->>Client: Table instance
    deactivate S

    Client->>T: addColumn(orderIdColumn)
    activate T
    T->>C: changeDataType(INT)
    activate C
    C-->>T: void
    deactivate C
    T-->>Client: void
    deactivate T

    Client->>T: addConstraint(pkConstraint)
    activate T
    T->>CT: validate()
    activate CT
    CT-->>T: true
    deactivate CT
    T-->>Client: void
    deactivate T

    Client->>T: addIndex(orderDateIdx)
    activate T
    T->>I: enable()
    activate I
    I-->>T: void
    deactivate I
    T-->>Client: void
    deactivate T

    %% -----------------------------------------------------
    %% Phase 3: Stored Procedures & Advanced objects
    %% -----------------------------------------------------
    Note over Client, SP: Phase 3: Procedures & Advanced objects

    Client->>S: createProcedure("archive_old_orders")
    activate S
    create participant SP
    S-->>SP: new StoredProcedure("archive_old_orders")
    S->>SP: compile()
    activate SP
    SP-->>S: void
    deactivate SP
    S-->>Client: StoredProcedure instance
    deactivate S

    Client->>SP: execute()
    activate SP
    Note over SP: Performs procedural logic
    SP-->>Client: ExecutionResult
    deactivate SP
```
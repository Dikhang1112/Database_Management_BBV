```mermaid
sequenceDiagram
    autonumber

    actor User
    participant Schema
    participant Table
    participant Column
    participant Constraint
    participant Index

    Note over User,Index: Table Metadata Lifecycle

    User->>+Schema: createTable("Student")
    activate Schema

    Schema->>+Schema: containsTable()

    alt Table does not exist
        Schema->>+Table: new Table("Student")
        activate Table
        Table-->>-Schema: Table
        deactivate Table
        Schema->>+Schema: registerTable(Table)
    else Table already exists
        Schema-->>-User: TableAlreadyExistsException
    end

    User->>+Table: addColumn(id)
    activate Table

    Table->>+Table: containsColumn()

    alt Column does not exist
        Table->>+Column: new Column(id)
        activate Column
        Column-->>-Table: Column
        deactivate Column
        Table->>+Table: registerColumn(Column)
    else Column already exists
        Table-->>-User: DuplicateColumnException
    end

    User->>+Table: addConstraint(PrimaryKey)

    Table->>+Constraint: validate()
    activate Constraint
    Constraint-->>-Table: Valid
    deactivate Constraint
    Table->>+Table: registerConstraint()

    User->>+Table: addIndex(pk_student)

    Table->>+Index: rebuild()
    activate Index
    Index-->>-Table: Ready
    deactivate Index
    Table->>+Table: registerIndex()

    Table-->>-User: Metadata Updated
    deactivate Table
    deactivate Schema
```
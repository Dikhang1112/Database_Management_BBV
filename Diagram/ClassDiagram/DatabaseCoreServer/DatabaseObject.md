classDiagram
    %% --- CORE INTERFACES ---
    class DatabaseObject {
        <<interface>>
        +getObjectID() int
        +getObjectName() String
    }
    %% Comment: Base interface providing system identity (ID and Name) for all components

    class SystemNode {
        <<interface>>
        +addChild(DatabaseObject child) void
        +getChild(int childID) DatabaseObject
    }
    %% Comment: Tree node interface managing physical hierarchy (Catalog -> Schema -> Table)

    class Constraint {
        <<interface>>
        +validate(Field field, Object newValue) boolean
    }
    %% Comment: Strategy interface for validating database structural rules and data integrity

    %% --- METADATA CLASSES (STATIC COMPONENTS) ---
    class DatabaseCatalog {
        -int catalogID [PK]
        -String catalogName
        -List~Schema~ schemas
    }
    %% Comment: The root node of the system, encapsulating multiple schemas

    class Schema {
        -int schemaID [PK]
        -int catalogID [FK]
        -String schemaName
        -List~DatabaseObject~ tables
    }
    %% Comment: A logical namespace grouping tables, views, and system objects

    class Table {
        -int tableID [PK]
        -int schemaID [FK]
        -String tableName
        -List~Column~ columns
        -List~Constraint~ constraints
        +validateRow(Row row) boolean
    }
    %% Comment: Represents a data table, supervising columns, rows, and constraints validation

    class Column {
        -int columnID [PK]
        -int tableID [FK]
        -String columnName
        -DataType dataType
        -boolean isNullable
    }
    %% Comment: Structural definition of a table field, specifying types and nullability rules

    %% --- RUNTIME CLASSES (DYNAMIC COMPONENTS) ---
    class DataType {
        -int dataTypeID [PK]
        -String typeName
        -int byteSize
    }
    %% Comment: Defines primitive storage types and physical byte footprints on memory/disk

    class TableConstraint {
        -int constraintID [PK]
        -int tableID [FK]
        -String constraintName
        -String expression
    }
    %% Comment: Concrete implementation of data rules (PK, FK, Check Constraints)

    class Row {
        -int rowID
        -List~Field~ fields
        +getFields() List~Field~
        +getFieldByColumnID(int columnID) Field
    }
    %% Comment: Transient memory structure aggregating fields for transactional data movement

    class Field {
        -int columnID [FK]
        -Object rawValue
        +getValue() Object
    }
    %% Comment: Atomic value container mapped to a specific column architecture during runtime

    %% --- UML RELATIONSHIPS ---
    
    %% Interface Inheritance (Noline arrow)
    DatabaseObject <|-- SystemNode
    DatabaseObject <|-- Constraint
    
    %% Interface Realization (Dotted arrow <|..)
    DatabaseObject <|.. Column
    SystemNode <|.. DatabaseCatalog
    SystemNode <|.. Schema
    SystemNode <|.. Table
    Constraint <|.. TableConstraint

    %% Static Composition & Aggregation
    DatabaseCatalog "1" *-- "*" Schema : catalogID
    Schema "1" *-- "*" Table : schemaID
    Table "1" *-- "*" Column : tableID
    Table "1" o-- "*" TableConstraint : tableID
    Column "1" --> "1" DataType : dataTypeID

    %% Dynamic Runtime Associations
    Table "1" ..> Row : instantiates and evaluates rows
    Row "1" *-- "*" Field : contains
    Field "1" --> "1" Column : maps to metadata definition

# Sequence Diagrams for Key Design Patterns in Metadata Module

This document contains **13 Mermaid Sequence Diagrams** illustrating interaction flows for core and operational **Design Patterns** in the `metadata` module, matching 100% with `ListPatterm.md` by object hierarchy from **Root Catalog** ➔ **Database & Schema** ➔ **Table** ➔ **Column, Constraint, & Index** ➔ **Schema Observers**.

---

## 1. CatalogManager & MetadataModule Level (Root Catalog Level)

### 1.1. Singleton Pattern
* **Pattern**: Singleton Pattern
* **Class/Interface Applied**: CatalogManager
* **Method**: `getInstance()`

```mermaid
sequenceDiagram
    autonumber
    participant Module as MetadataModule
    participant CM as CatalogManager
    
    Module->>CM: getInstance()
    alt Instance is null
        CM->>CM: create new CatalogManager()
    end
    CM-->>Module: CatalogManager instance
```

---

### 1.2. Facade Pattern
* **Pattern**: Facade Pattern
* **Class/Interface Applied**: MetadataModule
* **Method**: `getTable(databaseName, schemaName, tableName)`, `getDatabase(databaseName)`, `getCatalogManager()`, `executeDDL(command)`

```mermaid
sequenceDiagram
    autonumber
    participant Caller as Internal Caller
    participant Facade as MetadataModule
    participant CM as CatalogManager
    participant DB as Database
    participant Schema as Schema
    participant Table as Table
    participant Cmd as DDLCommand

    Caller->>Facade: getTable("sales_db", "public", "orders")
    Facade->>CM: getDatabase("sales_db")
    CM-->>Facade: Database instance
    Facade->>DB: getSchema("public")
    DB-->>Facade: Schema instance
    Facade->>Schema: getTable("orders")
    Schema-->>Facade: Table instance
    Facade-->>Caller: Table instance

    Caller->>Facade: executeDDL(command)
    Facade->>Cmd: execute()
    Cmd-->>Facade: executed
```

---

### 1.3. Composite Pattern
* **Pattern**: Composite Pattern
* **Class/Interface Applied**: MetadataElement (implemented by CatalogManager, Database, Schema, Table, Column)
* **Method**: `getElementName()`

```mermaid
sequenceDiagram
    autonumber
    participant Module as MetadataModule
    participant CM as CatalogManager
    participant DB as Database
    participant Schema as Schema
    participant Table as Table

    Module->>CM: getElementName()
    CM-->>Module: "CatalogManager"
    Module->>DB: getElementName()
    DB-->>Module: "sales_db"
    Module->>Schema: getElementName()
    Schema-->>Module: "public"
    Module->>Table: getElementName()
    Table-->>Module: "orders"
```

---

## 2. Database Level

### 2.1. State Pattern
* **Pattern**: State Pattern
* **Class/Interface Applied**: DatabaseStatus, Database
* **Method**: `setStatus(status)`, `createSchema(schemaName)`

```mermaid
sequenceDiagram
    autonumber
    participant DB as Database
    participant Schema as Schema

    note over DB: Database Status = OFFLINE
    DB->>DB: setStatus(DatabaseStatus.OFFLINE)
    DB->>DB: createSchema("sales")
    DB-->>DB: throw IllegalStateException("Database is offline")

    note over DB: Database Status = ONLINE
    DB->>DB: setStatus(DatabaseStatus.ONLINE)
    DB->>Schema: new Schema("sales")
    Schema-->>DB: schemaInstance
```

---

## 3. Schema Level

### 3.1. Factory Method Pattern
* **Pattern**: Factory Method Pattern
* **Class/Interface Applied**: Schema
* **Method**: `createTable(tableName)`

```mermaid
sequenceDiagram
    autonumber
    participant Schema as Schema
    participant Table as Table

    Schema->>Table: createTable("orders")
    Table-->>Schema: tableInstance
```

---

### 3.2. Command Pattern
* **Pattern**: Command Pattern
* **Class/Interface Applied**: DDLCommand (CreateTableCommand, DropTableCommand, CreateSchemaCommand, DropSchemaCommand, RenameSchemaCommand)
* **Method**: `execute()`, `undo()`

```mermaid
sequenceDiagram
    autonumber
    participant Cmd as CreateTableCommand
    participant Schema as Schema

    Cmd->>Schema: execute() / createTable("orders")
    Schema-->>Cmd: executed

    note over Cmd, Schema: Rollback Operation Triggered
    Cmd->>Schema: undo() / dropTable("orders")
    Schema-->>Cmd: undone
```

---

## 4. Table Level

### 4.1. Prototype Pattern
* **Pattern**: Prototype Pattern
* **Class/Interface Applied**: Table, Column
* **Method**: `clone()`

```mermaid
sequenceDiagram
    autonumber
    participant Schema as Schema
    participant TableOrig as Table ("orders")
    participant TableClone as Table ("orders_copy")

    Schema->>TableOrig: clone()
    TableOrig->>TableClone: new Table("orders_copy")
    loop Clone Columns
        TableOrig->>TableClone: createColumn(column.clone())
    end
    TableOrig-->>Schema: TableClone instance
```

---

### 4.2. Memento Pattern
* **Pattern**: Memento Pattern
* **Class/Interface Applied**: TableMemento, Table
* **Method**: `createMemento()`, `restore(memento)`

```mermaid
sequenceDiagram
    autonumber
    participant Schema as Schema
    participant Table as Table
    participant Memento as TableMemento

    Schema->>Table: createMemento()
    Table->>Memento: new TableMemento(tableName, columns)
    Memento-->>Table: mementoInstance
    Table-->>Schema: mementoInstance

    note over Schema, Table: Operation Failed - Restore State
    Schema->>Table: restore(mementoInstance)
    Table->>Memento: getColumnsSnapshot()
    Memento-->>Table: columnsList
    Table-->>Schema: tableRestored
```

---

### 4.3. Observer Pattern (Subject)
* **Pattern**: Observer Pattern (Subject)
* **Class/Interface Applied**: Table, MetadataChangeListener
* **Method**: `registerListener(listener)`, `removeListener(listener)`, `notifyListeners(eventType, targetName)`

```mermaid
sequenceDiagram
    autonumber
    participant Table as Table (Subject)
    participant Listener as MetadataChangeListener (Observer)

    Table->>Table: registerListener(listener)
    Table->>Table: dropColumn("email")
    Table->>Table: notifyListeners("COLUMN_REMOVED", "email")
    Table->>Listener: onMetadataChanged("COLUMN_REMOVED", "email")
```

---

## 5. Column Level

### 5.1. Builder Pattern
* **Pattern**: Builder Pattern
* **Class/Interface Applied**: ColumnBuilder
* **Method**: `setName(name)`, `setType(dataType)`, `setNullable(nullable)`, `setDefaultValue(value)`, `build()`

```mermaid
sequenceDiagram
    autonumber
    participant Table as Table
    participant CB as ColumnBuilder
    participant Col as Column

    Table->>CB: new ColumnBuilder()
    CB-->>Table: ColumnBuilder
    Table->>CB: setName("user_id")
    CB-->>Table: ColumnBuilder
    Table->>CB: setType(DataType.INT)
    CB-->>Table: ColumnBuilder
    Table->>CB: setNullable(false)
    CB-->>Table: ColumnBuilder
    Table->>CB: setDefaultValue("0")
    CB-->>Table: ColumnBuilder
    Table->>CB: build()
    CB->>Col: new Column("user_id", DataType.INT)
    Col-->>CB: columnInstance
    CB-->>Table: columnInstance
```

---

## 6. Constraint Level

### 6.1. Factory Method Pattern (Constraint)
* **Pattern**: Factory Method Pattern
* **Class/Interface Applied**: ConstraintFactory
* **Method**: `createConstraint(type, name, args)`

```mermaid
sequenceDiagram
    autonumber
    participant Schema as Schema
    participant CF as ConstraintFactory
    participant FK as ForeignKeyConstraint

    Schema->>CF: createConstraint("FOREIGN_KEY", "FK_User_Role")
    CF->>FK: new ForeignKeyConstraint("FK_User_Role")
    FK-->>CF: constraintInstance
    CF-->>Schema: constraintInstance
```

---

### 6.2. Template Method Pattern
* **Pattern**: Template Method Pattern
* **Class/Interface Applied**: Constraint
* **Method**: `validate()`, `preValidate()`, `doValidate()`, `postValidate()`

```mermaid
sequenceDiagram
    autonumber
    participant Schema as Schema
    participant Base as Constraint (Abstract)
    participant Sub as ForeignKeyConstraint

    Schema->>Base: validate()
    Base->>Base: preValidate()
    Base->>Sub: doValidate() / validateReference()
    Sub-->>Base: isReferenceValid
    Base->>Base: postValidate(isReferenceValid)
    Base-->>Schema: isValid
```

---

### 6.3. Chain of Responsibility Pattern
* **Pattern**: Chain of Responsibility Pattern
* **Class/Interface Applied**: ConstraintValidationChain
* **Method**: `addConstraint(constraint)`, `removeConstraint(constraint)`, `validateAll()`

```mermaid
sequenceDiagram
    autonumber
    participant Table as Table
    participant Chain as ConstraintValidationChain
    participant PK as PrimaryKeyConstraint
    participant FK as ForeignKeyConstraint

    Table->>Chain: addConstraint(pkConstraint)
    Table->>Chain: addConstraint(fkConstraint)
    Table->>Chain: validateAll()
    Chain->>PK: validate()
    alt PK Valid
        PK-->>Chain: true
        Chain->>FK: validate()
        FK-->>Chain: true
        Chain-->>Table: true (validateAll Passed)
    else PK Invalid
        PK-->>Chain: false
        Chain-->>Table: false (validateAll Failed)
    end
```

---

## 7. Index Level

### 7.1. Strategy Pattern
* **Pattern**: Strategy Pattern
* **Class/Interface Applied**: Index, IndexRebuildStrategy
* **Method**: `setRebuildStrategy(strategy)`, `rebuild()`

```mermaid
sequenceDiagram
    autonumber
    participant Table as Table
    participant Index as Index
    participant Strategy as IndexRebuildStrategy

    Table->>Index: setRebuildStrategy(strategy)
    Table->>Index: rebuild()
    Index->>Strategy: rebuildIndex(this)
    Strategy-->>Index: rebuildSuccess
    Index-->>Table: rebuildSuccess
```

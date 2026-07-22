# Design Patterns in Metadata Module (Core & Operational)

This document details the **13 core and operational Design Patterns** in the `metadata` module, organized by object hierarchy from **Root Catalog** ➔ **Database & Schema** ➔ **Table** ➔ **Column, Constraint, & Index** ➔ **Schema Observers**.

---

## 1. CatalogManager & MetadataModule Level (Root Catalog Level)

### 1.1. Singleton Pattern
* **Pattern**: Singleton Pattern
* **Class/Interface Applied**: CatalogManager
* **Method**: `getInstance()`
* **Reason for Use**: Ensures that only a single instance of `CatalogManager` is instantiated and accessed across the DBMS to manage the central database map (`databases`), preventing duplicate instantiations and in-memory metadata conflicts.

### 1.2. Facade Pattern
* **Pattern**: Facade Pattern
* **Class/Interface Applied**: MetadataModule
* **Method**: `getTable(databaseName, schemaName, tableName)`, `getDatabase(databaseName)`, `getCatalogManager()`, `executeDDL(command)`
* **Reason for Use**: Provides a unified, high-level API entry point for external modules (Query Processor, Execution Engine) to access metadata, encapsulating complex interactions between CatalogManager, Database, Schema, and Table.

### 1.3. Composite Pattern
* **Pattern**: Composite Pattern
* **Class/Interface Applied**: MetadataElement (implemented by CatalogManager, Database, Schema, Table, Column)
* **Method**: `getElementName()`
* **Reason for Use**: Constructs a unified tree hierarchy for the catalog, enabling consistent operations and queries across both container nodes (`CatalogManager`, `Database`, `Schema`) and leaf nodes (`Table`, `Column`).

---

## 2. Database Level

### 2.1. State Pattern
* **Pattern**: State Pattern
* **Class/Interface Applied**: DatabaseStatus, Database
* **Method**: `setStatus(status)`, `createSchema(schemaName)`
* **Reason for Use**: Dynamically alters operation behavior (such as `createSchema()`) based on the current database state (`ONLINE`, `OFFLINE`, `READ_ONLY`). If the status is `OFFLINE`, operations are blocked and an `IllegalStateException` is thrown.

---

## 3. Schema Level

### 3.1. Factory Method Pattern
* **Pattern**: Factory Method Pattern
* **Class/Interface Applied**: Schema
* **Method**: `createTable(tableName)`, `createView(viewName, sql)`
* **Reason for Use**: Serves as a Creator encapsulating object instantiation logic for schema components, storing and managing created database objects within the Schema.

### 3.2. Command Pattern
* **Pattern**: Command Pattern
* **Class/Interface Applied**: DDLCommand (CreateTableCommand, DropTableCommand, CreateSchemaCommand, DropSchemaCommand, RenameSchemaCommand)
* **Method**: `execute()`, `undo()`
* **Reason for Use**: Encapsulates DDL schema modification statements into standalone command objects supporting `execute()` and `undo()`, enabling DDL transaction rollback and schema migration execution.

---

## 4. Table Level

### 4.1. Prototype Pattern
* **Pattern**: Prototype Pattern
* **Class/Interface Applied**: Table, Column
* **Method**: `clone()`
* **Reason for Use**: Performs deep cloning of existing table structures (including columns, constraints, and indexes) to support DDL operations like `CREATE TABLE ... LIKE ...` without re-parsing SQL definitions.

### 4.2. Memento Pattern
* **Pattern**: Memento Pattern
* **Class/Interface Applied**: TableMemento, Table
* **Method**: `createMemento()`, `restore(memento)`
* **Reason for Use**: Captures a snapshot of the table structure before executing schema modifications (e.g., `ALTER TABLE`). Restores previous table state if modification operations encounter errors.

### 4.3. Observer Pattern (Subject)
* **Pattern**: Observer Pattern (Subject)
* **Class/Interface Applied**: Table, MetadataChangeListener
* **Method**: `registerListener(listener)`, `removeListener(listener)`, `notifyListeners(eventType, targetName)`
* **Reason for Use**: `Table` acts as a Subject, automatically dispatching event notifications to registered subscribers (`View`) whenever column definitions are modified (`createColumn`, `dropColumn`, `renameColumn`).

---

## 5. Column Level

### 5.1. Builder Pattern
* **Pattern**: Builder Pattern
* **Class/Interface Applied**: ColumnBuilder
* **Method**: `setName(name)`, `setType(dataType)`, `setNullable(nullable)`, `setDefaultValue(value)`, `build()`
* **Reason for Use**: Provides a fluent API to construct complex `Column` objects with multiple optional parameters in a clean and readable manner.

---

## 6. Constraint Level

### 6.1. Factory Method Pattern (Constraint)
* **Pattern**: Factory Method Pattern
* **Class/Interface Applied**: ConstraintFactory
* **Method**: `createConstraint(type, name, args)`
* **Reason for Use**: Encapsulates instantiation logic for specific constraint subclasses (`PrimaryKeyConstraint`, `ForeignKeyConstraint`, `UniqueConstraint`, `CheckConstraint`) based on parameter types.

### 6.2. Template Method Pattern
* **Pattern**: Template Method Pattern
* **Class/Interface Applied**: Constraint
* **Method**: `validate()`, `preValidate()`, `doValidate()`, `postValidate()`
* **Reason for Use**: Defines a fixed 3-step validation algorithm skeleton in `validate()`, allowing subclasses to override specific validation logic in `doValidate()`.

### 6.3. Chain of Responsibility Pattern
* **Pattern**: Chain of Responsibility Pattern
* **Class/Interface Applied**: ConstraintValidationChain
* **Method**: `addConstraint(constraint)`, `removeConstraint(constraint)`, `validateAll()`
* **Reason for Use**: Manages sequential data validation across a chain of constraints (PK ➔ FK ➔ Unique ➔ Check). If any constraint validation fails, execution halts immediately.

---

## 7. Index Level

### 7.1. Strategy Pattern
* **Pattern**: Strategy Pattern
* **Class/Interface Applied**: Index, IndexRebuildStrategy
* **Method**: `setRebuildStrategy(strategy)`, `rebuild()`
* **Reason for Use**: Decouples index rebuild algorithms from the `Index` class, allowing runtime selection and execution of different rebuild strategies (e.g., B-Tree, Hash, Concurrent).

---

## 8. View Level (Schema Observers)

### 8.1. Observer Pattern (Subscriber)
* **Pattern**: Observer Pattern (Subscriber)
* **Class/Interface Applied**: View, MetadataChangeListener
* **Method**: `onMetadataChanged(eventType, targetName)`, `compile()`, `refresh()`
* **Reason for Use**: Acts as Subscribers registered to receive notification events from `Table`. When table structures change, they automatically trigger `compile()` or `refresh()` to update definitions.

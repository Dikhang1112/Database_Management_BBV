# Metadata Subsystem Unit Test Scenarios

This document defines the unit test scenarios for `level_3.Metadata`.
The current scope covers Metadata catalog management and database object implementations: `CatalogManager`, `Database`, `Schema`, `Table`, `Column`, `Index`, and concrete `Constraint` classes.

Each test case follows this structure:

- **Test method:** the JUnit method name.
- **Sequence diagram:** related sequence diagram ID, if available.
- **Input:** objects or values used by the test.
- **Steps:** main test actions.
- **Expected output:** assertions expected from the test.

---

## 1. CatalogManagerTest

### TC-01. Create Database

- **Test method:** `createDatabase_ShouldAddDatabaseToCatalog_WhenValidNameIsProvided`
- **Sequence diagram:** `TC-01`
- **Input:**
  - Database name: `"sales_db"`
- **Steps:**
  - Call `catalogManager.createDatabase("sales_db")`.
  - Check `catalogManager.containsDatabase("sales_db")`.
  - Retrieve database with `getDatabase("sales_db")`.
- **Expected output:**
  - `createDatabase()` returns a non-null `Database` instance.
  - `containsDatabase("sales_db")` returns `true`.
  - Returned database instance has name `"sales_db"`.

### TC-02. Drop Database

- **Test method:** `dropDatabase_ShouldRemoveDatabaseFromCatalog_WhenDatabaseExists`
- **Sequence diagram:** `TC-02`
- **Input:**
  - Database name: `"temp_db"`
- **Steps:**
  - Create database `"temp_db"`.
  - Call `catalogManager.dropDatabase("temp_db")`.
  - Call `catalogManager.containsDatabase("temp_db")`.
- **Expected output:**
  - `containsDatabase("temp_db")` returns `false` after drop.

### TC-03. Get & List Databases

- **Test method:** `listDatabases_ShouldReturnAllRegisteredDatabases_WhenDatabasesAreCreated`
- **Sequence diagram:** `TC-03`
- **Input:**
  - Database names: `"db1"`, `"db2"`
- **Steps:**
  - Create `"db1"` and `"db2"`.
  - Call `catalogManager.listDatabases()`.
  - Call `catalogManager.getDatabase("db1")`.
- **Expected output:**
  - `listDatabases()` returns a list containing 2 database instances.
  - `getDatabase("db1")` returns the matching database instance.

### TC-04. Clear Catalog

- **Test method:** `clear_ShouldRemoveAllDatabases_WhenCatalogIsCleared`
- **Sequence diagram:** `TC-04`
- **Input:**
  - Database names: `"db1"`
- **Steps:**
  - Create `"db1"`.
  - Call `catalogManager.clear()`.
  - Call `catalogManager.listDatabases()`.
- **Expected output:**
  - `listDatabases()` returns an empty list.

---

## 2. DatabaseTest

### TC-05. Create & Drop Schema

- **Test method:** `createSchema_ShouldManageSchemasInDatabase_WhenSchemaIsAddedAndDropped`
- **Sequence diagram:** `TC-05`
- **Input:**
  - Database: `new Database("app_db")`
  - Schema name: `"public"`
- **Steps:**
  - Call `database.createSchema("public")`.
  - Check `database.containsSchema("public")`.
  - Call `database.dropSchema("public")`.
  - Check `database.containsSchema("public")`.
- **Expected output:**
  - `containsSchema("public")` returns `true` after creation.
  - `containsSchema("public")` returns `false` after drop.

### TC-06. Database Status & Rename

- **Test method:** `setStatus_And_rename_ShouldUpdateDatabaseState_WhenModified`
- **Sequence diagram:** `TC-06`
- **Input:**
  - Initial database: `new Database("old_name")`
  - New name: `"new_name"`
  - Status: `DatabaseStatus.READ_ONLY`
- **Steps:**
  - Call `database.rename("new_name")`.
  - Call `database.setStatus(DatabaseStatus.READ_ONLY)`.
- **Expected output:**
  - Database name is updated to `"new_name"`.
  - Database status is updated to `READ_ONLY`.

---

## 3. SchemaTest

### TC-07. Create & Manage Tables

- **Test method:** `createTable_ShouldRegisterTableInSchema_WhenValidTableNameIsProvided`
- **Sequence diagram:** `TC-07`
- **Input:**
  - Schema: `new Schema("public")`
  - Table name: `"users"`
- **Steps:**
  - Call `schema.createTable("users")`.
  - Call `schema.containsTable("users")`.
  - Call `schema.getTable("users")`.
- **Expected output:**
  - `containsTable("users")` returns `true`.
  - `getTable("users")` returns non-null `Table` instance.

### TC-08. Rename Schema & List Tables

- **Test method:** `rename_And_listTables_ShouldUpdateSchemaNameAndMaintainTables_WhenRenamed`
- **Sequence diagram:** `TC-08`
- **Input:**
  - Schema: `new Schema("raw_schema")`
  - Tables: `"t1"`, `"t2"`
  - New schema name: `"prod_schema"`
- **Steps:**
  - Create tables `"t1"` and `"t2"`.
  - Call `schema.listTables()`.
  - Call `schema.rename("prod_schema")`.
- **Expected output:**
  - `listTables()` returns a list of size 2.
  - Schema name is updated to `"prod_schema"`.

---

## 4. TableTest

### TC-09. Add & Manage Columns

- **Test method:** `addColumn_ShouldAttachColumnToTable_WhenColumnIsAdded`
- **Sequence diagram:** `TC-09`
- **Input:**
  - Table: `new Table("orders")`
  - Column: `new Column("order_id", DataType.BIGINT)`
- **Steps:**
  - Call `table.addColumn(column)`.
  - Call `table.listColumns()`.
  - Call `table.containsColumn("order_id")`.
- **Expected output:**
  - `listColumns()` contains the added column.
  - `containsColumn("order_id")` returns `true`.

---

## 5. ColumnTest

### TC-10. Column Operations

- **Test method:** `changeDataType_And_setDefaultValue_ShouldUpdateColumnProperties_WhenModified`
- **Sequence diagram:** `TC-10`
- **Input:**
  - Column: `new Column("age", DataType.INT)`
- **Steps:**
  - Call `column.setNullable(false)`.
  - Call `column.setDefaultValue("18")`.
  - Call `column.changeDataType(DataType.BIGINT)`.
  - Call `column.rename("user_age")`.
- **Expected output:**
  - Column properties are updated without throwing errors.

---

## 6. IndexTest

### TC-11. Add Index to Table

- **Test method:** `addIndex_ShouldAttachIndexToTable_WhenIndexIsAddedAndRebuilt`
- **Sequence diagram:** `TC-11`
- **Input:**
  - Table: `new Table("users")`
  - Index: `new Index("idx_user_email", IndexType.BTREE)`
- **Steps:**
  - Call `table.addIndex(index)`.
  - Call `index.disable()`.
  - Call `index.rebuild()`.
- **Expected output:**
  - Index is successfully added to table.
  - `rebuild()` reenables the index.

---

## 7. ConstraintTest

### TC-12. Primary Key Constraint

- **Test method:** `validate_ShouldReturnConstraintStatus_WhenPrimaryKeyIsChecked`
- **Sequence diagram:** `TC-12`
- **Input:**
  - Constraint: `new PrimaryKeyConstraint("pk_users")`
- **Steps:**
  - Call `constraint.validate()`.
  - Call `constraint.disable()`.
  - Call `constraint.validate()`.
- **Expected output:**
  - `validate()` returns `true` when enabled.
  - `validate()` returns `false` when disabled.

### TC-13. Foreign Key Constraint

- **Test method:** `validateReference_ShouldReturnTrue_WhenForeignKeyReferencesValidTableAndColumn`
- **Sequence diagram:** `TC-13`
- **Input:**
  - Table: `new Table("parent_table")`
  - Column: `new Column("id", DataType.INT)`
  - FK Constraint: `new ForeignKeyConstraint("fk_child", table, column)`
- **Steps:**
  - Call `fkConstraint.validateReference()`.
- **Expected output:**
  - `validateReference()` returns `true`.

### TC-14. Check Constraint Evaluation

- **Test method:** `evaluate_ShouldReturnTrue_WhenCheckConstraintExpressionIsValid`
- **Sequence diagram:** `TC-14`
- **Input:**
  - Check Constraint: `new CheckConstraint("chk_age", "age >= 18")`
- **Steps:**
  - Call `checkConstraint.evaluate()`.
- **Expected output:**
  - `evaluate()` returns `true`.

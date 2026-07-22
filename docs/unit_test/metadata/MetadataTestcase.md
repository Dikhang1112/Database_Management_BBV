# Metadata Subsystem Unit Test Scenarios

This document defines all unit test scenarios for `Metadata` subsystem (`CatalogManager`, `Database`, `Schema`, `Table`, `Column`, `Index`, and `Constraint` classes), covering both positive (happy path) and negative (edge cases and exceptions) scenarios as specified in [MindmapTest.md](file:///d:/BBV/Database_Management_BBV/docs/unit_test/metadata/MindmapTest.md) and [SequeceTestCase.md](file:///d:/BBV/Database_Management_BBV/docs/unit_test/metadata/SequeceTestCase.md).

Each test scenario follows this standard format:
- **Test method:** JUnit method name.
- **Sequence diagram:** Corresponding sequence diagram ID in `SequeceTestCase.md`.
- **Input:** Input objects or parameters.
- **Steps:** Actions performed in the test.
- **Expected output:** Assertions or expected exceptions.

---

## 1. CatalogManagerTest

### TC-01. Create Database (Happy Path)
- **Test method:** `createDatabase_ShouldAddDatabaseToCatalog_WhenValidNameIsProvided`
- **Sequence diagram:** `TC-01`
- **Input:** Database name: `"sales_db"`
- **Steps:**
  - Call `catalogManager.createDatabase("sales_db")`.
  - Check `catalogManager.containsDatabase("sales_db")`.
  - Call `catalogManager.getDatabase("sales_db")`.
- **Expected output:**
  - `createDatabase()` returns a non-null `Database` instance.
  - `containsDatabase("sales_db")` returns `true`.
  - Returned database has name `"sales_db"`.

### TC-01A. Create Database - Already Exists
- **Test method:** `createDatabase_ShouldThrowException_WhenDatabaseAlreadyExists`
- **Sequence diagram:** `TC-01A`
- **Input:** Database name: `"sales_db"` (already registered)
- **Steps:**
  - Create database `"sales_db"`.
  - Call `catalogManager.createDatabase("sales_db")` again.
- **Expected output:**
  - Throws `DatabaseAlreadyExistsException`.

### TC-01B. Create Database - Invalid Name
- **Test method:** `createDatabase_ShouldThrowException_WhenDatabaseNameIsInvalid`
- **Sequence diagram:** `TC-01B`
- **Input:** Invalid database name: `""` or `null`
- **Steps:**
  - Call `catalogManager.createDatabase("")`.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Value is empty").

### TC-01C. Create Database - Permission Denied
- **Test method:** `createDatabase_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-01C`
- **Input:** Database name: `"protected_db"`, User without `CREATE_DB` permission
- **Steps:**
  - Call `catalogManager.createDatabase("protected_db")`.
- **Expected output:**
  - Throws `SecurityException`.

### TC-01D. Create Database - Special Characters
- **Test method:** `createDatabase_ShouldThrowException_WhenDatabaseNameContainsSpecialCharacters`
- **Sequence diagram:** `TC-01D`
- **Input:** Database name: `"sales@db!"`
- **Steps:**
  - Call `catalogManager.createDatabase("sales@db!")`.
- **Expected output:**
  - Throws `IllegalArgumentException`.

### TC-01E. Rename Database (Happy Path)
- **Test method:** `renameDatabase_ShouldUpdateNameInCatalog_WhenValid`
- **Sequence diagram:** `TC-01E`
- **Input:** Old database: `"old_db"`, New database: `"new_db"`
- **Steps:**
  - Call `catalogManager.createDatabase("old_db")`.
  - Call `catalogManager.renameDatabase("old_db", "new_db")`.
- **Expected output:**
  - `containsDatabase("old_db")` returns `false`.
  - `containsDatabase("new_db")` returns `true`.

### TC-01F. Rename Database - Old Database Missing
- **Test method:** `renameDatabase_ShouldThrowException_WhenOldDatabaseNotFound`
- **Sequence diagram:** `TC-01F`
- **Input:** Missing database: `"missing_db"`
- **Steps:**
  - Call `catalogManager.renameDatabase("missing_db", "new_db")`.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Database not found").

### TC-01G. Rename Database - Duplicate New Name
- **Test method:** `renameDatabase_ShouldThrowException_WhenNewNameAlreadyExists`
- **Sequence diagram:** `TC-01G`
- **Input:** Existing databases: `"db1"` and `"db2"`
- **Steps:**
  - Call `catalogManager.renameDatabase("db1", "db2")`.
- **Expected output:**
  - Throws `IllegalStateException` ("Database already exists").

---

### TC-02. Drop Database (Happy Path)
- **Test method:** `dropDatabase_ShouldRemoveDatabaseFromCatalog_WhenDatabaseExists`
- **Sequence diagram:** `TC-02`
- **Input:** Database name: `"temp_db"`
- **Steps:**
  - Create database `"temp_db"`.
  - Call `catalogManager.dropDatabase("temp_db")`.
  - Call `catalogManager.containsDatabase("temp_db")`.
- **Expected output:**
  - `containsDatabase("temp_db")` returns `false`.

### TC-02A. Drop Database - Not Found
- **Test method:** `dropDatabase_ShouldThrowException_WhenDatabaseNotFound`
- **Sequence diagram:** `TC-02A`
- **Input:** Non-existing database name: `"missing_db"`
- **Steps:**
  - Call `catalogManager.dropDatabase("missing_db")`.
- **Expected output:**
  - Throws `DatabaseNotFoundException`.

### TC-02B. Drop Database - Database Not Empty
- **Test method:** `dropDatabase_ShouldThrowException_WhenDatabaseIsNotEmpty`
- **Sequence diagram:** `TC-02B`
- **Input:** Database name: `"db_with_schemas"` containing schemas
- **Steps:**
  - Create database `"db_with_schemas"` and add schema `"public"`.
  - Call `catalogManager.dropDatabase("db_with_schemas")`.
- **Expected output:**
  - Throws `DatabaseNotEmptyException`.

### TC-02C. Drop Database - Permission Denied
- **Test method:** `dropDatabase_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-02C`
- **Input:** Database name: `"prod_db"`, User without `DROP_DB` permission
- **Steps:**
  - Call `catalogManager.dropDatabase("prod_db")`.
- **Expected output:**
  - Throws `SecurityException`.

---

### TC-03. List Databases
- **Test method:** `listDatabases_ShouldReturnAllRegisteredDatabases_WhenDatabasesAreCreated`
- **Sequence diagram:** `TC-03`
- **Input:** Database names: `"db1"`, `"db2"`
- **Steps:**
  - Create `"db1"` and `"db2"`.
  - Call `catalogManager.listDatabases()`.
  - Call `catalogManager.getDatabase("db1")`.
- **Expected output:**
  - `listDatabases()` returns list containing `"db1"` and `"db2"`.
  - `getDatabase("db1")` returns matching instance.

---

### TC-04. Clear Catalog
- **Test method:** `clear_ShouldRemoveAllDatabases_WhenCatalogIsCleared`
- **Sequence diagram:** `TC-04`
- **Input:** Database names: `"db1"`
- **Steps:**
  - Create `"db1"`.
  - Call `catalogManager.clear()`.
  - Call `catalogManager.listDatabases()`.
- **Expected output:**
  - `listDatabases()` returns empty list.

---

## 2. DatabaseTest

### TC-05. Create & Manage Schemas (Happy Path)
- **Test method:** `createSchema_ShouldManageSchemasInDatabase_WhenSchemaIsAddedAndDropped`
- **Sequence diagram:** `TC-05`
- **Input:** Database: `new Database("app_db")`, Schema name: `"public"`
- **Steps:**
  - Call `database.createSchema("public")`.
  - Check `database.containsSchema("public")`.
  - Call `database.dropSchema("public")`.
  - Check `database.containsSchema("public")`.
- **Expected output:**
  - `containsSchema("public")` returns `true` after creation.
  - `containsSchema("public")` returns `false` after drop.

### TC-05A. Create Schema - Already Exists
- **Test method:** `createSchema_ShouldThrowException_WhenSchemaAlreadyExists`
- **Sequence diagram:** `TC-05A`
- **Input:** Schema name: `"public"` (already existing)
- **Steps:**
  - Create schema `"public"`.
  - Call `database.createSchema("public")` again.
- **Expected output:**
  - Throws `SchemaAlreadyExistsException`.

### TC-05B. Create Schema - Database Offline
- **Test method:** `createSchema_ShouldThrowException_WhenDatabaseIsOffline`
- **Sequence diagram:** `TC-05B`
- **Input:** Database with status `DatabaseStatus.OFFLINE`
- **Steps:**
  - Set `database.setStatus(DatabaseStatus.OFFLINE)`.
  - Call `database.createSchema("public")`.
- **Expected output:**
  - Throws `DatabaseOfflineException`.

### TC-05C. Create Schema - Permission Denied
- **Test method:** `createSchema_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-05C`
- **Input:** Schema name: `"secure_schema"`, User without permission
- **Steps:**
  - Call `database.createSchema("secure_schema")`.
- **Expected output:**
  - Throws `SecurityException`.

### TC-05D. Create Schema - Special Characters
- **Test method:** `createSchema_ShouldThrowException_WhenSchemaNameContainsSpecialCharacters`
- **Sequence diagram:** `TC-05D`
- **Input:** Schema name: `"schema#123!"`
- **Steps:**
  - Call `database.createSchema("schema#123!")`.
- **Expected output:**
  - Throws `IllegalArgumentException`.

---

### TC-06. Database Status & Rename (Happy Path)
- **Test method:** `setStatus_And_rename_ShouldUpdateDatabaseState_WhenModified`
- **Sequence diagram:** `TC-06`
- **Input:** Database: `new Database("old_name")`, New name: `"new_name"`, Status: `DatabaseStatus.READ_ONLY`
- **Steps:**
  - Call `database.rename("new_name")`.
  - Call `database.setStatus(DatabaseStatus.READ_ONLY)`.
- **Expected output:**
  - Name is updated to `"new_name"`.
  - Status is updated to `READ_ONLY`.

### TC-06A. Rename Database - Duplicate Name
- **Test method:** `renameDatabase_ShouldThrowException_WhenDuplicateDatabaseName`
- **Sequence diagram:** `TC-06A`
- **Input:** Existing database name: `"existing_db_name"`
- **Steps:**
  - Call `database.rename("existing_db_name")`.
- **Expected output:**
  - Throws `DuplicateDatabaseNameException`.

### TC-06B. Rename Database - Invalid Name
- **Test method:** `renameDatabase_ShouldThrowException_WhenNameIsInvalid`
- **Sequence diagram:** `TC-06B`
- **Input:** Invalid name: `""`
- **Steps:**
  - Call `database.rename("")`.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Value is empty").

### TC-06C. Rename Schema in Database
- **Test method:** `renameSchema_ShouldRenameTargetSchema_WhenExists`
- **Sequence diagram:** `TC-06C`
- **Input:** Schema `"raw_schema"` inside database `"app_db"`, rename to `"prod_schema"`
- **Steps:**
  - Call `database.createSchema("raw_schema")`.
  - Call `database.renameSchema("raw_schema", "prod_schema")`.
- **Expected output:**
  - `containsSchema("raw_schema")` returns `false`.
  - `containsSchema("prod_schema")` returns `true`.

---

## 3. SchemaTest

### TC-07. Create Table (Happy Path)
- **Test method:** `createTable_ShouldRegisterTableInSchema_WhenValidTableNameIsProvided`
- **Sequence diagram:** `TC-07`
- **Input:** Schema: `new Schema("public")`, Table name: `"users"`
- **Steps:**
  - Call `schema.createTable("users")`.
  - Call `schema.containsTable("users")`.
  - Call `schema.getTable("users")`.
- **Expected output:**
  - `containsTable("users")` returns `true`.
  - `getTable("users")` returns non-null `Table` instance.

### TC-07A. Create Table - Already Exists
- **Test method:** `createTable_ShouldThrowException_WhenTableAlreadyExists`
- **Sequence diagram:** `TC-07A`
- **Input:** Table name: `"users"` (already existing)
- **Steps:**
  - Call `schema.createTable("users")`.
  - Call `schema.createTable("users")` again.
- **Expected output:**
  - Throws `TableAlreadyExistsException`.

### TC-07B. Create Table - Permission Denied
- **Test method:** `createTable_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-07B`
- **Input:** Table name: `"users"`, User without `CREATE_TABLE` permission
- **Steps:**
  - Call `schema.createTable("users")`.
- **Expected output:**
  - Throws `SecurityException`.

### TC-07C. Create Table - Schema Read Only
- **Test method:** `createTable_ShouldThrowException_WhenSchemaIsReadOnly`
- **Sequence diagram:** `TC-07C`
- **Input:** Schema with `readOnly = true`
- **Steps:**
  - Set `schema.setReadOnly(true)`.
  - Call `schema.createTable("users")`.
- **Expected output:**
  - Throws `SchemaReadOnlyException`.

### TC-07D. Create Table - Special Characters
- **Test method:** `createTable_ShouldThrowException_WhenTableNameContainsSpecialCharacters`
- **Sequence diagram:** `TC-07D`
- **Input:** Table name: `"user@table!"`
- **Steps:**
  - Call `schema.createTable("user@table!")`.
- **Expected output:**
  - Throws `IllegalArgumentException`.

---

### TC-08. Rename Schema & List Tables (Happy Path)
- **Test method:** `rename_And_listTables_ShouldUpdateSchemaNameAndMaintainTables_WhenRenamed`
- **Sequence diagram:** `TC-08`
- **Input:** Schema: `new Schema("raw_schema")`, Tables: `"t1"`, `"t2"`, New name: `"prod_schema"`
- **Steps:**
  - Create tables `"t1"` and `"t2"`.
  - Call `schema.listTables()`.
  - Call `schema.rename("prod_schema")`.
- **Expected output:**
  - `listTables()` returns list of size 2.
  - Schema name updated to `"prod_schema"`.

### TC-08A. Rename Schema - Not Found
- **Test method:** `renameSchema_ShouldThrowException_WhenSchemaNotFound`
- **Sequence diagram:** `TC-08A`
- **Input:** Missing schema name `"missing_schema"`
- **Steps:**
  - Call `database.renameSchema("missing_schema", "new_name")`.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Schema not found").

### TC-08B. Create View in Schema
- **Test method:** `createView_ShouldAddViewToSchema`
- **Sequence diagram:** `TC-08B`
- **Input:** View name `"v_active_users"`, SQL `"SELECT * FROM users WHERE active = true"`
- **Steps:**
  - Call `schema.createView("v_active_users", sql)`.
- **Expected output:**
  - Returns non-null `View` instance.
  - `listViews()` contains the created view.

## 4. TableTest

### TC-09. Add Column (Happy Path)
- **Test method:** `addColumn_ShouldAttachColumnToTable_WhenColumnIsAdded`
- **Sequence diagram:** `TC-09`
- **Input:** Table: `new Table("orders")`, Column: `new Column("order_id", DataType.BIGINT)`
- **Steps:**
  - Call `table.addColumn(column)`.
  - Call `table.listColumns()`.
  - Call `table.containsColumn("order_id")`.
- **Expected output:**
  - `listColumns()` contains added column.
  - `containsColumn("order_id")` returns `true`.

### TC-09A. Add Column - Column Already Exists
- **Test method:** `addColumn_ShouldThrowException_WhenColumnAlreadyExists`
- **Sequence diagram:** `TC-09A`
- **Input:** Column `"order_id"` (already in table)
- **Steps:**
  - Add column `"order_id"`.
  - Call `table.addColumn(column)` again with `"order_id"`.
- **Expected output:**
  - Throws `ColumnAlreadyExistsException`.

### TC-09B. Add Column - Table Locked
- **Test method:** `addColumn_ShouldThrowException_WhenTableIsLocked`
- **Sequence diagram:** `TC-09B`
- **Input:** Locked table
- **Steps:**
  - Set `table.setLocked(true)`.
  - Call `table.addColumn(column)`.
- **Expected output:**
  - Throws `TableLockedException`.

### TC-09C. Add Column - Permission Denied
- **Test method:** `addColumn_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-09C`
- **Input:** User without `ALTER_TABLE` permission
- **Steps:**
  - Call `table.addColumn(column)`.
- **Expected output:**
  - Throws `SecurityException`.

### TC-09D. Add Column - Special Characters
- **Test method:** `addColumn_ShouldThrowException_WhenColumnNameContainsSpecialCharacters`
- **Sequence diagram:** `TC-09D`
- **Input:** Column with name `"col#name!"`
- **Steps:**
  - Call `table.addColumn(column)`.
- **Expected output:**
  - Throws `IllegalArgumentException`.

### TC-09E. Create Column Facade
- **Test method:** `createColumn_ShouldAddColumnToTable`
- **Sequence diagram:** `TC-09E`
- **Input:** Column with name `"user_id"`
- **Steps:**
  - Call `table.createColumn(column)`.
- **Expected output:**
  - `containsColumn("user_id")` returns `true`.

---

### TC-10. Remove Column (Happy Path)
- **Test method:** `removeColumn_ShouldDetachColumnFromTable_WhenColumnExists`
- **Sequence diagram:** `TC-10`
- **Input:** Table with column `"temp_col"`
- **Steps:**
  - Call `table.removeColumn("temp_col")`.
  - Call `table.containsColumn("temp_col")`.
- **Expected output:**
  - `containsColumn("temp_col")` returns `false`.

### TC-10A. Remove Column - Column Not Found
- **Test method:** `removeColumn_ShouldThrowException_WhenColumnNotFound`
- **Sequence diagram:** `TC-10A`
- **Input:** Non-existing column name: `"missing_col"`
- **Steps:**
  - Call `table.removeColumn("missing_col")`.
- **Expected output:**
  - Throws `ColumnNotFoundException`.

### TC-10B. Remove Column - Referenced by Constraint
- **Test method:** `removeColumn_ShouldThrowException_WhenReferencedByConstraint`
- **Sequence diagram:** `TC-10B`
- **Input:** Column `"id"` referenced by Primary Key or Foreign Key constraint
- **Steps:**
  - Call `table.removeColumn("id")`.
- **Expected output:**
  - Throws `ColumnInUseException`.

### TC-10C. Drop Column Facade
- **Test method:** `dropColumn_ShouldRemoveColumnFromTable`
- **Sequence diagram:** `TC-10C`
- **Input:** Column `"temp_col"`
- **Steps:**
  - Add `"temp_col"` to table.
  - Call `table.dropColumn("temp_col")`.
- **Expected output:**
  - `containsColumn("temp_col")` returns `false`.

### TC-10D. Rename Column
- **Test method:** `renameColumn_ShouldUpdateColumnName_WhenExists`
- **Sequence diagram:** `TC-10D`
- **Input:** Old column name `"old_col"`, new name `"new_col"`
- **Steps:**
  - Add `"old_col"` to table.
  - Call `table.renameColumn("old_col", "new_col")`.
- **Expected output:**
  - `containsColumn("old_col")` returns `false`.
  - `containsColumn("new_col")` returns `true`.

---

## 5. ColumnTest

### TC-11. Change Data Type (Happy Path)
- **Test method:** `changeDataType_ShouldUpdateColumnDataType_WhenValid`
- **Sequence diagram:** `TC-11`
- **Input:** Column: `new Column("age", DataType.INT)`, New type: `DataType.BIGINT`
- **Steps:**
  - Call `column.changeDataType(DataType.BIGINT)`.
- **Expected output:**
  - Column data type updated to `BIGINT`.

### TC-11A. Change Data Type - Unsupported Conversion
- **Test method:** `changeDataType_ShouldThrowException_WhenUnsupportedConversion`
- **Sequence diagram:** `TC-11A`
- **Input:** Column with type `INT`, Incompatible target type: `DataType.BOOLEAN`
- **Steps:**
  - Call `column.changeDataType(DataType.BOOLEAN)`.
- **Expected output:**
  - Throws `DataTypeConversionException`.

### TC-11B. Change Data Type - Permission Denied
- **Test method:** `changeDataType_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-11B`
- **Input:** User without `ALTER_COLUMN` permission
- **Steps:**
  - Call `column.changeDataType(DataType.VARCHAR)`.
- **Expected output:**
  - Throws `SecurityException`.

---

### TC-12. Set Default Value & Properties (Happy Path)
- **Test method:** `changeDataType_And_setDefaultValue_ShouldUpdateColumnProperties_WhenModified`
- **Sequence diagram:** `TC-12`
- **Input:** Column: `new Column("age", DataType.INT)`
- **Steps:**
  - Call `column.setNullable(false)`.
  - Call `column.setDefaultValue("18")`.
  - Call `column.changeDataType(DataType.BIGINT)`.
  - Call `column.rename("user_age")`.
- **Expected output:**
  - Column properties updated without errors.

### TC-12A. Set Default Value - Invalid Default Value
- **Test method:** `setDefaultValue_ShouldThrowException_WhenDefaultValueIsInvalid`
- **Sequence diagram:** `TC-12A`
- **Input:** Column type `INT`, Invalid value string: `"abc_not_an_int"`
- **Steps:**
  - Call `column.setDefaultValue("abc_not_an_int")`.
- **Expected output:**
  - Throws `InvalidDefaultValueException`.

---

## 6. IndexTest

### TC-13. Add Index (Happy Path)
- **Test method:** `addIndex_ShouldAttachIndexToTable_WhenIndexIsAddedAndRebuilt`
- **Sequence diagram:** `TC-13`
- **Input:** Table: `new Table("users")`, Index: `new Index("idx_user_email", IndexType.BTREE)`
- **Steps:**
  - Call `table.addIndex(index)`.
  - Call `index.disable()`.
  - Call `index.rebuild()`.
- **Expected output:**
  - Index attached to table.
  - `rebuild()` reenables index.

### TC-13A. Add Index - Duplicate Index Name
- **Test method:** `addIndex_ShouldThrowException_WhenDuplicateIndexName`
- **Sequence diagram:** `TC-13A`
- **Input:** Index `"idx_user_email"` (already existing on table)
- **Steps:**
  - Add index `"idx_user_email"`.
  - Call `table.addIndex(index)` again.
- **Expected output:**
  - Throws `DuplicateIndexException`.

### TC-13B. Add Index - Column Not Found
- **Test method:** `addIndex_ShouldThrowException_WhenIndexedColumnNotFound`
- **Sequence diagram:** `TC-13B`
- **Input:** Index targeting non-existing column `"non_existing_col"`
- **Steps:**
  - Call `table.addIndex(index)`.
- **Expected output:**
  - Throws `ColumnNotFoundException`.

### TC-13C. Add and Remove Index Facade
- **Test method:** `addAndRemoveIndex_ShouldManageIndexesInTable`
- **Sequence diagram:** `TC-13C`
- **Input:** `Index` instance
- **Steps:**
  - Call `table.addIndex(index)`.
  - Call `table.removeIndex("idx_order_date")`.
- **Expected output:**
  - `listIndexes()` correctly updates.

---

### TC-14. Rebuild Index (Happy Path)
- **Test method:** `rebuildIndex_ShouldReenableIndex_WhenRebuilt`
- **Sequence diagram:** `TC-14`
- **Input:** Disabled Index instance
- **Steps:**
  - Call `index.disable()`.
  - Call `index.rebuild()`.
- **Expected output:**
  - Index status is enabled (`true`).

### TC-14A. Rebuild Index - Disabled & Corrupted
- **Test method:** `rebuildIndex_ShouldThrowException_WhenIndexIsDisabledAndCorrupted`
- **Sequence diagram:** `TC-14A`
- **Input:** Corrupted index file/data
- **Steps:**
  - Call `index.rebuild()`.
- **Expected output:**
  - Throws `IndexDisabledException`.

---

## 7. ConstraintTest

### TC-15. Validate Primary Key Constraint
- **Test method:** `validate_ShouldReturnConstraintStatus_WhenPrimaryKeyIsChecked`
- **Sequence diagram:** `TC-15`
- **Input:** Constraint: `new PrimaryKeyConstraint("pk_users")`
- **Steps:**
  - Call `constraint.validate()`.
  - Call `constraint.disable()`.
  - Call `constraint.validate()`.
- **Expected output:**
  - `validate()` returns `true` when enabled.
  - `validate()` returns `false` when disabled.

### TC-15A. Add and Remove Constraint Facade
- **Test method:** `addAndRemoveConstraint_ShouldManageConstraintsInTable`
- **Sequence diagram:** `TC-15A`
- **Input:** `Constraint` instance
- **Steps:**
  - Call `table.addConstraint(constraint)`.
  - Call `table.removeConstraint("pk_order")`.
- **Expected output:**
  - `listConstraints()` correctly updates.

---

### TC-16. Validate Foreign Key Reference (Happy Path)
- **Test method:** `validateReference_ShouldReturnTrue_WhenForeignKeyReferencesValidTableAndColumn`
- **Sequence diagram:** `TC-16`
- **Input:** Table: `new Table("parent_table")`, Column: `new Column("id", DataType.INT)`, FK: `new ForeignKeyConstraint("fk_child", table, column)`
- **Steps:**
  - Call `fkConstraint.validateReference()`.
- **Expected output:**
  - `validateReference()` returns `true`.

### TC-16A. Validate Foreign Key - Referenced Table Missing
- **Test method:** `validateForeignKey_ShouldReturnFalse_WhenReferencedTableMissing`
- **Sequence diagram:** `TC-16A`
- **Input:** FK referencing deleted or non-existing parent table
- **Steps:**
  - Call `fkConstraint.validateReference()`.
- **Expected output:**
  - `validateReference()` returns `false`.

### TC-16B. Validate Foreign Key - Referenced Column Missing
- **Test method:** `validateForeignKey_ShouldReturnFalse_WhenReferencedColumnMissing`
- **Sequence diagram:** `TC-16B`
- **Input:** FK referencing deleted column on parent table
- **Steps:**
  - Call `fkConstraint.validateReference()`.
- **Expected output:**
  - `validateReference()` returns `false`.

### TC-16C. Validate Foreign Key - Parent Row Missing
- **Test method:** `validateForeignKey_ShouldReturnFalse_WhenParentRowMissing`
- **Sequence diagram:** `TC-16C`
- **Input:** FK pointing to non-existent parent primary key value
- **Steps:**
  - Call `fkConstraint.validateReference()`.
- **Expected output:**
  - `validateReference()` returns `false`.

---

### TC-17. Evaluate Check Constraint (Happy Path)
- **Test method:** `evaluate_ShouldReturnTrue_WhenCheckConstraintExpressionIsValid`
- **Sequence diagram:** `TC-17`
- **Input:** Check Constraint: `new CheckConstraint("chk_age", "age >= 18")`
- **Steps:**
  - Call `checkConstraint.evaluate()`.
- **Expected output:**
  - `evaluate()` returns `true`.

### TC-17A. Evaluate Check Constraint - Invalid Expression
- **Test method:** `evaluate_ShouldReturnFalse_WhenExpressionIsInvalid`
- **Sequence diagram:** `TC-17A`
- **Input:** Invalid syntax expression: `"age >= "`
- **Steps:**
  - Call `checkConstraint.evaluate()`.
- **Expected output:**
  - `evaluate()` returns `false`.

---

## 8. MetadataModuleTest

### TC-18. Get Table Facade (Happy Path)
- **Test method:** `getTable_ShouldReturnTable_WhenDatabaseSchemaAndTableExist`
- **Sequence diagram:** `TC-18`
- **Input:** Database: `"sales_db"`, Schema: `"public"`, Table: `"orders"`
- **Steps:**
  - Call `metadataModule.getTable("sales_db", "public", "orders")`.
- **Expected output:**
  - Returns target `Table` instance.

### TC-18A. Execute DDL Facade
- **Test method:** `executeDDL_ShouldInvokeCommandExecute_WhenCommandIsProvided`
- **Sequence diagram:** `TC-18A`
- **Input:** `DDLCommand` mock object
- **Steps:**
  - Call `metadataModule.executeDDL(command)`.
- **Expected output:**
  - `command.execute()` is invoked once.

---

## 9. DDLCommandTest

### TC-19A. CreateDatabaseCommand Execution
- **Test method:** `execute_ShouldCallCatalogCreateDatabase`
- **Sequence diagram:** `TC-19A`
- **Input:** `CreateDatabaseCommand("sales_db")`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Creates database `"sales_db"` in `CatalogManager`.

### TC-19B. DropDatabaseCommand Execution
- **Test method:** `execute_ShouldCallCatalogDropDatabase`
- **Sequence diagram:** `TC-19B`
- **Input:** `DropDatabaseCommand("temp_db")`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Removes database `"temp_db"` from `CatalogManager`.

### TC-19C. RenameDatabaseCommand Execution
- **Test method:** `execute_ShouldCallCatalogRenameDatabase`
- **Sequence diagram:** `TC-19C`
- **Input:** `RenameDatabaseCommand("old_db", "new_db")`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Renames database `"old_db"` to `"new_db"`.

### TC-19D. CreateSchemaCommand Execution
- **Test method:** `execute_ShouldCallDatabaseCreateSchema`
- **Sequence diagram:** `TC-19D`
- **Input:** `CreateSchemaCommand("public")`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Schema `"public"` created in target database.

### TC-19E. DropSchemaCommand Execution
- **Test method:** `execute_ShouldCallDatabaseDropSchema`
- **Sequence diagram:** `TC-19E`
- **Input:** `DropSchemaCommand("staging")`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Schema `"staging"` removed from target database.

### TC-19F. RenameSchemaCommand Execution
- **Test method:** `execute_ShouldCallDatabaseRenameSchema`
- **Sequence diagram:** `TC-19F`
- **Input:** `RenameSchemaCommand("old_schema", "new_schema")`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Schema renamed to `"new_schema"`.

### TC-19G. CreateTableCommand Execution
- **Test method:** `execute_ShouldCallSchemaCreateTable`
- **Sequence diagram:** `TC-19G`
- **Input:** `CreateTableCommand(schema, "users")`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Table `"users"` created in target schema.

### TC-19H. DropTableCommand Execution
- **Test method:** `execute_ShouldCallSchemaDropTable`
- **Sequence diagram:** `TC-19H`
- **Input:** `DropTableCommand("users")`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Table `"users"` dropped from schema.

### TC-19I. RenameTableCommand Execution
- **Test method:** `execute_ShouldCallTableRename`
- **Sequence diagram:** `TC-19I`
- **Input:** `RenameTableCommand("old_table", "new_table")`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Table renamed to `"new_table"`.

### TC-19J. CreateColumnCommand Execution
- **Test method:** `execute_ShouldCallTableAddColumn`
- **Sequence diagram:** `TC-19J`
- **Input:** `CreateColumnCommand(column)`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Column added to table.

### TC-19K. DropColumnCommand Execution
- **Test method:** `execute_ShouldCallTableRemoveColumn`
- **Sequence diagram:** `TC-19K`
- **Input:** `DropColumnCommand("age")`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Column `"age"` removed from table.

### TC-19L. RenameColumnCommand Execution
- **Test method:** `execute_ShouldCallTableRenameColumn`
- **Sequence diagram:** `TC-19L`
- **Input:** `RenameColumnCommand("old_col", "new_col")`
- **Steps:**
  - Call `command.execute()`.
- **Expected output:**
  - Column renamed to `"new_col"`.

---

## 10. ColumnBuilderTest

### TC-20. Build Column
- **Test method:** `build_ShouldConstructColumn_WhenValidPropertiesSet`
- **Sequence diagram:** `TC-20`
- **Input:** `name: "email"`, `dataType: DataType.VARCHAR`, `nullable: false`, `defaultValue: "N/A"`
- **Steps:**
  - Invoke `new ColumnBuilder("email").setType(DataType.VARCHAR).setNullable(false).setDefaultValue("N/A").build()`.
- **Expected output:**
  - Non-null `Column` with exact matching properties.

### TC-20A. Build Column with Default Values
- **Test method:** `build_ShouldApplyDefaults_WhenOptionalPropertiesOmitted`
- **Sequence diagram:** `TC-20A`
- **Input:** `name: "id"`, `dataType: DataType.INT`
- **Steps:**
  - Invoke `new ColumnBuilder("id").setType(DataType.INT).build()`.
- **Expected output:**
  - `nullable` defaults to `true`, `defaultValue` is `null`.

---

## 11. TableMementoTest

### TC-21. Memento Snapshot and Restore
- **Test method:** `createMemento_ShouldCaptureSnapshot_And_restore_ShouldRevertState`
- **Sequence diagram:** `TC-21`
- **Input:** `Table("products")` with initial columns
- **Steps:**
  - Call `TableMemento memento = table.createMemento()`.
  - Modify table (add column).
  - Call `table.restore(memento)`.
- **Expected output:**
  - `memento.getTableName()` returns `"products"`.
  - Restored `table` matches initial snapshot columns.

---

## 12. ConstraintValidationChainTest

### TC-22. Validate Chain (Pass)
- **Test method:** `validateAll_ShouldReturnTrue_WhenAllConstraintsInChainAreValid`
- **Sequence diagram:** `TC-22`
- **Input:** Chain with valid PK and Check constraints
- **Steps:**
  - Call `chain.addConstraint(pkConstraint)`.
  - Call `chain.addConstraint(checkConstraint)`.
  - Call `chain.validateAll()`.
- **Expected output:**
  - `validateAll()` returns `true`.

### TC-22A. Validate Chain (Fail Fast)
- **Test method:** `validateAll_ShouldReturnFalse_WhenAnyConstraintInChainFails`
- **Sequence diagram:** `TC-22A`
- **Input:** Chain containing an invalid constraint
- **Steps:**
  - Call `chain.addConstraint(invalidConstraint)`.
  - Call `chain.validateAll()`.
- **Expected output:**
  - `validateAll()` returns `false`.

# Metadata Subsystem Unit Test Scenarios

This document defines all unit test scenarios for `Metadata` subsystem (`MetadataModule`, `CatalogManager`, `Database`, `Schema`, `Table`, `Column`, `Index`, `Constraint`, and helper classes), covering both positive (happy path) and negative (edge cases and exceptions) scenarios as specified in [MindmapTest.md](file:///d:/BBV/Database_Management_BBV/docs/unit_test/metadata/MindmapTest.md) and [SequeceTestCase.md](file:///d:/BBV/Database_Management_BBV/docs/unit_test/metadata/SequeceTestCase.md).

Each test scenario follows this standard format:
- **Test method:** JUnit method name.
- **Sequence diagram:** Corresponding sequence diagram ID in `SequeceTestCase.md`.
- **Input:** Input objects or parameters.
- **Why:** Rationale and business requirement for verifying this test case.
- **Expected output:** Assertions or expected exceptions.

---

## 1. MetadataModuleTest

### TC-01. Get Table Facade (Happy Path)
- **Test method:** `getTable_ShouldReturnTable_WhenDatabaseSchemaAndTableExist`
- **Sequence diagram:** `TC-01`
- **Input:** Database: `"sales_db"`, Schema: `"public"`, Table: `"orders"`
- **Why:** Verifies Facade pattern correctness when retrieving table information across Catalog -> Database -> Schema -> Table layers.
- **Expected output:**
  - Returns target `Table` instance.

### TC-01A. Execute DDL Facade (Happy Path)
- **Test method:** `executeDDL_ShouldInvokeCommandExecute_WhenCommandIsProvided`
- **Sequence diagram:** `TC-01A`
- **Input:** `DDLCommand` mock object
- **Why:** Ensures the `executeDDL` Facade entry point receives DDL commands and executes them following the Command pattern.
- **Expected output:**
  - `command.execute()` is invoked once.

### TC-01B. Get Database Facade (Happy Path)
- **Test method:** `getDatabase_ShouldReturnDatabase_WhenExists`
- **Sequence diagram:** `TC-01B`
- **Input:** Database name: `"app_db"`
- **Why:** Verifies the Facade entry point retrieves `Database` instances directly from `CatalogManager`.
- **Expected output:**
  - Returns matching `Database` instance.

### TC-01C. Get Database Facade - Invalid Name
- **Test method:** `getDatabase_ShouldThrowException_WhenDatabaseNameIsInvalid`
- **Sequence diagram:** `TC-01C`
- **Input:** Invalid database name: `"invalid#db"`
- **Why:** Ensures the Facade applies `CatalogValidator` to reject invalid database names at the entry boundary.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Database name contains invalid characters").

### TC-01D. Get Table Facade - Invalid Identifier
- **Test method:** `getTable_ShouldThrowException_WhenAnyIdentifierIsInvalid`
- **Sequence diagram:** `TC-01D`
- **Input:** Invalid database name: `"invalid#db"`, Schema: `"public"`, Table: `"users"`
- **Why:** Ensures invalid identifiers at any hierarchy level (Database/Schema/Table) are blocked early by the Facade.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Database name contains invalid characters").

### TC-01E. Get Table Facade - Not Found
- **Test method:** `getTable_ShouldReturnNull_WhenDatabaseDoesNotExist`
- **Sequence diagram:** `TC-01E`
- **Input:** Non-existing database: `"missing_db"`, Schema: `"public"`, Table: `"users"`
- **Why:** Ensures the Facade safely returns `null` when retrieving a table from a non-existent database to prevent crashes.
- **Expected output:**
  - Returns `null`.

### TC-01F. Execute DDL Facade - Null Command
- **Test method:** `executeDDL_ShouldDoNothing_WhenCommandIsNull`
- **Sequence diagram:** `TC-01F`
- **Input:** `null` command
- **Why:** Ensures the Facade handles null commands safely without throwing `NullPointerException`.
- **Expected output:**
  - Completes normally without exception.

---

## 2. CatalogManagerTest

### TC-02. Create Database (Happy Path)
- **Test method:** `createDatabase_ShouldAddDatabaseToCatalog_WhenValidNameIsProvided`
- **Sequence diagram:** `TC-02`
- **Input:** Database name: `"sales_db"`
- **Why:** Ensures `CatalogManager` creates and registers a new `Database` instance in the root catalog.
- **Expected output:**
  - `createDatabase()` returns a non-null `Database` instance.
  - `containsDatabase("sales_db")` returns `true`.
  - Returned database has name `"sales_db"`.

### TC-02A. Create Database - Already Exists
- **Test method:** `createDatabase_ShouldThrowException_WhenDatabaseAlreadyExists`
- **Sequence diagram:** `TC-02A`
- **Input:** Database name: `"sales_db"` (already registered)
- **Why:** Prevents duplicate Database names in the system to avoid overwriting catalog entries.
- **Expected output:**
  - Throws `IllegalStateException` ("Database already exists").

### TC-02B. Create Database - Invalid Name
- **Test method:** `createDatabase_ShouldThrowException_WhenDatabaseNameIsInvalid`
- **Sequence diagram:** `TC-02B`
- **Input:** Invalid database name: `""` or `null`
- **Why:** Ensures Database names adhere to identifier validation rules (non-empty, non-null).
- **Expected output:**
  - Throws `IllegalArgumentException` ("Value is empty").

### TC-02C. Create Database - Permission Denied
- **Test method:** `createDatabase_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-02C`
- **Input:** Database name: `"protected_db"`
- **Why:** Verifies security access control by blocking unauthorized database creation.
- **Expected output:**
  - Throws `SecurityException` ("Permission denied").

### TC-02D. Create Database - Special Characters
- **Test method:** `createDatabase_ShouldThrowException_WhenDatabaseNameContainsSpecialCharacters`
- **Sequence diagram:** `TC-02D`
- **Input:** Invalid database name: `"sales@db!"`
- **Why:** Ensures Database names do not contain special characters that could cause SQL or filesystem errors.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Database name contains invalid characters").

### TC-02E. Rename Database (Happy Path)
- **Test method:** `renameDatabase_ShouldUpdateNameInCatalog_WhenValid`
- **Sequence diagram:** `TC-02E`
- **Input:** `oldName: "old_db"`, `newName: "new_db"`
- **Why:** Ensures `CatalogManager` updates storage keys correctly when renaming a Database.
- **Expected output:**
  - Database renamed successfully and accessible under `"new_db"`.

### TC-02F. Rename Database - Old Database Missing
- **Test method:** `renameDatabase_ShouldThrowException_WhenOldDatabaseNotFound`
- **Sequence diagram:** `TC-02F`
- **Input:** `oldName: "missing_db"`, `newName: "new_db"`
- **Why:** Prevents renaming a non-existent Database.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Database not found").

### TC-02G. Rename Database - New Name Duplicate
- **Test method:** `renameDatabase_ShouldThrowException_WhenNewNameAlreadyExists`
- **Sequence diagram:** `TC-02G`
- **Input:** `oldName: "db1"`, `newName: "db2"` (db2 exists)
- **Why:** Avoids name collisions with existing Databases during renaming.
- **Expected output:**
  - Throws `IllegalStateException` ("Database already exists").

---

### TC-03. Drop Database (Happy Path)
- **Test method:** `dropDatabase_ShouldRemoveDatabaseFromCatalog_WhenDatabaseExists`
- **Sequence diagram:** `TC-03`
- **Input:** Database name: `"temp_db"`
- **Why:** Ensures successful removal of a Database from the root catalog when invoking `dropDatabase`.
- **Expected output:**
  - Database `"temp_db"` is removed from catalog.

### TC-03A. Drop Database - Not Found
- **Test method:** `dropDatabase_ShouldThrowException_WhenDatabaseNotFound`
- **Sequence diagram:** `TC-03A`
- **Input:** Non-existing database name: `"missing_db"`
- **Why:** Ensures explicit error reporting when attempting to drop a non-existent Database.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Database not found").

### TC-03B. Drop Database - Database Not Empty
- **Test method:** `dropDatabase_ShouldThrowException_WhenDatabaseIsNotEmpty`
- **Sequence diagram:** `TC-03B`
- **Input:** Database containing non-empty schemas
- **Why:** Protects data integrity by preventing accidental deletion of non-empty Databases.
- **Expected output:**
  - Throws `IllegalStateException` ("Database is not empty").

### TC-03C. Drop Database - Permission Denied
- **Test method:** `dropDatabase_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-03C`
- **Input:** Protected database name: `"prod_db"`
- **Why:** Enforces security access control by blocking unauthorized Database deletion.
- **Expected output:**
  - Throws `SecurityException` ("Permission denied").

---

### TC-04. List Databases (Happy Path)
- **Test method:** `listDatabases_ShouldReturnAllRegisteredDatabases_WhenDatabasesAreCreated`
- **Sequence diagram:** `TC-04`
- **Input:** Registered databases `"db1"`, `"db2"`
- **Why:** Ensures the API lists all registered Databases in the root catalog.
- **Expected output:**
  - Returns collection containing `"db1"` and `"db2"`.

### TC-04A. Get Database - Invalid Name Format
- **Test method:** `getDatabase_ShouldThrowException_WhenNameIsInvalid`
- **Sequence diagram:** `TC-04A`
- **Input:** Invalid name: `"db#123"`
- **Why:** Verifies database name format validation during lookup operations.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Database name contains invalid characters").

---

### TC-05. Clear Catalog (Happy Path)
- **Test method:** `clear_ShouldRemoveAllDatabases_WhenCatalogIsCleared`
- **Sequence diagram:** `TC-05`
- **Input:** Active catalog with multiple databases
- **Why:** Ensures complete state reset of Singleton `CatalogManager` to prevent test state leakage.
- **Expected output:**
  - Catalog becomes empty (`listDatabases()` returns empty collection).

---

## 3. DatabaseTest

### TC-06. Create Schema (Happy Path)
- **Test method:** `createSchema_ShouldManageSchemasInDatabase_WhenSchemaIsAddedAndDropped`
- **Sequence diagram:** `TC-06`
- **Input:** Schema name: `"public"`
- **Why:** Ensures lifecycle management (create/drop) of Schemas within a Database instance.
- **Expected output:**
  - Schema `"public"` created, registered, and successfully dropped.

### TC-06A. Create Schema - Already Exists
- **Test method:** `createSchema_ShouldThrowException_WhenSchemaAlreadyExists`
- **Sequence diagram:** `TC-06A`
- **Input:** Schema name: `"public"` (already existing)
- **Why:** Prevents duplicate Schema creation within the same Database.
- **Expected output:**
  - Throws `IllegalStateException` ("Schema already exists").

### TC-06B. Create Schema - Database Offline
- **Test method:** `createSchema_ShouldThrowException_WhenDatabaseIsOffline`
- **Sequence diagram:** `TC-06B`
- **Input:** Database set to `OFFLINE` status
- **Why:** Enforces State pattern rules by blocking schema modifications while the Database is in OFFLINE state.
- **Expected output:**
  - Throws `IllegalStateException` ("Database is offline").

### TC-06C. Create Schema - Permission Denied
- **Test method:** `createSchema_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-06C`
- **Input:** Restricted schema name: `"secure_schema"`
- **Why:** Verifies security access control during Schema creation.
- **Expected output:**
  - Throws `SecurityException` ("Permission denied").

### TC-06D. Create Schema - Special Characters
- **Test method:** `createSchema_ShouldThrowException_WhenSchemaNameContainsSpecialCharacters`
- **Sequence diagram:** `TC-06D`
- **Input:** Invalid schema name: `"schema#123!"`
- **Why:** Ensures Schema names adhere to valid identifier character rules.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Schema name contains invalid characters").

---

### TC-07. Database Status Management
- **Test method:** `setStatus_ShouldUpdateDatabaseStatus_WhenModified`
- **Sequence diagram:** `TC-07`
- **Input:** `DatabaseStatus.READ_ONLY`
- **Why:** Verifies updating operational status properties of a Database instance.
- **Expected output:**
  - Database status updated to `READ_ONLY`.

---

## 4. SchemaTest

### TC-08. Create Table (Happy Path)
- **Test method:** `createTable_ShouldRegisterTableInSchema_WhenValidTableNameIsProvided`
- **Sequence diagram:** `TC-08`
- **Input:** Table name: `"users"`
- **Why:** Ensures `Schema` registers and manages child `Table` instances correctly.
- **Expected output:**
  - Table `"users"` registered and retrieved from Schema.

### TC-08A. Create Table - Already Exists
- **Test method:** `createTable_ShouldThrowException_WhenTableAlreadyExists`
- **Sequence diagram:** `TC-08A`
- **Input:** Table name: `"users"` (already existing)
- **Why:** Prevents creating duplicate Table names within the same Schema.
- **Expected output:**
  - Throws `IllegalStateException` ("Table already exists").

### TC-08B. Create Table - Permission Denied
- **Test method:** `createTable_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-08B`
- **Input:** Restricted table name
- **Why:** Verifies security access control for Table creation inside a Schema.
- **Expected output:**
  - Throws `SecurityException` ("Permission denied").

### TC-08C. Create Table - Read-Only Schema
- **Test method:** `createTable_ShouldThrowException_WhenSchemaIsReadOnly`
- **Sequence diagram:** `TC-08C`
- **Input:** Schema with `readOnly = true`
- **Why:** Prevents structural modifications and table additions when the Schema is in READ_ONLY mode.
- **Expected output:**
  - Throws `IllegalStateException` ("Schema is read-only").

### TC-08D. Create Table - Special Characters
- **Test method:** `createTable_ShouldThrowException_WhenTableNameContainsSpecialCharacters`
- **Sequence diagram:** `TC-08D`
- **Input:** Invalid table name: `"user@table!"`
- **Why:** Validates that table names do not contain forbidden special characters.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Table name contains invalid characters").

---

### TC-09. List Tables in Schema
- **Test method:** `listTables_ShouldReturnManagedTables_WhenTablesExist`
- **Sequence diagram:** `TC-09`
- **Input:** Schema with tables `"t1"`, `"t2"`
- **Why:** Verifies retrieving all child tables managed within a Schema instance.
- **Expected output:**
  - Returns list containing 2 table instances.

### TC-09B. Get Table - Invalid Name Format
- **Test method:** `getTable_ShouldThrowException_WhenNameIsInvalid`
- **Sequence diagram:** `TC-09B`
- **Input:** Invalid table name: `"table#invalid"`
- **Why:** Validates table name format during lookup queries from a Schema.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Table name contains invalid characters").

---

## 5. TableTest

### TC-10. Add Column (Happy Path)
- **Test method:** `addColumn_ShouldAttachColumnToTable_WhenColumnIsAdded`
- **Sequence diagram:** `TC-10`
- **Input:** `Column("order_id", DataType.BIGINT)`
- **Why:** Ensures `Table` attaches and manages child Column instances correctly.
- **Expected output:**
  - Column attached and accessible via `listColumns()`.

### TC-10A. Add Column - Column Already Exists
- **Test method:** `addColumn_ShouldThrowException_WhenColumnAlreadyExists`
- **Sequence diagram:** `TC-10A`
- **Input:** Column with duplicate name `"order_id"`
- **Why:** Prevents creating duplicate Column names within the same Table.
- **Expected output:**
  - Throws `IllegalStateException` ("Column already exists").

### TC-10B. Add Column - Table Locked
- **Test method:** `addColumn_ShouldThrowException_WhenTableIsLocked`
- **Sequence diagram:** `TC-10B`
- **Input:** Table with `locked = true`
- **Why:** Ensures data consistency by blocking structural modifications when the Table is locked.
- **Expected output:**
  - Throws `IllegalStateException` ("Table is locked").

### TC-10C. Add Column - Permission Denied
- **Test method:** `addColumn_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-10C`
- **Input:** Restricted column addition
- **Why:** Verifies security access control for table schema modifications.
- **Expected output:**
  - Throws `SecurityException` ("Permission denied").

### TC-10D. Add Column - Special Characters
- **Test method:** `addColumn_ShouldThrowException_WhenColumnNameContainsSpecialCharacters`
- **Sequence diagram:** `TC-10D`
- **Input:** Invalid column name: `"col#name!"`
- **Why:** Blocks special characters in Column names.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Column name contains invalid characters").

### TC-10E. Create Column Facade (Happy Path)
- **Test method:** `createColumn_ShouldAddColumnToTable`
- **Sequence diagram:** `TC-10E`
- **Input:** `Column("user_id", DataType.INT)`
- **Why:** Verifies the `createColumn` helper API creates and attaches columns successfully.
- **Expected output:**
  - Column added to table.

---

### TC-11. Remove Column (Happy Path)
- **Test method:** `removeColumn_ShouldDetachColumnFromTable_WhenColumnExists`
- **Sequence diagram:** `TC-11`
- **Input:** Column name: `"temp_col"`
- **Why:** Ensures proper column removal from a Table when the target column exists and is eligible for deletion.
- **Expected output:**
  - Column `"temp_col"` removed from table.

### TC-11A. Remove Column - Not Found
- **Test method:** `removeColumn_ShouldThrowException_WhenColumnNotFound`
- **Sequence diagram:** `TC-11A`
- **Input:** Non-existing column name: `"missing_col"`
- **Why:** Reports explicit errors when trying to remove a non-existent column.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Column not found").

### TC-11B. Remove Column - Referenced by Constraint
- **Test method:** `removeColumn_ShouldThrowException_WhenReferencedByConstraint`
- **Sequence diagram:** `TC-11B`
- **Input:** Column referenced by Primary Key or Foreign Key constraint
- **Why:** Enforces referential integrity by preventing deletion of columns referenced by Primary Key or Foreign Key constraints.
- **Expected output:**
  - Throws `IllegalStateException` ("Column is referenced by constraint").

### TC-11C. Remove Column - Table Locked
- **Test method:** `removeColumn_ShouldThrowException_WhenTableIsLocked`
- **Sequence diagram:** `TC-11C`
- **Input:** Table locked
- **Why:** Blocks column removal when the Table is locked.
- **Expected output:**
  - Throws `IllegalStateException` ("Table is locked").

### TC-11D. Remove Column - Invalid Name Format
- **Test method:** `removeColumn_ShouldThrowException_WhenColumnNameIsInvalid`
- **Sequence diagram:** `TC-11D`
- **Input:** Invalid name: `""`
- **Why:** Validates Column name format during deletion calls.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Value is empty").

### TC-11E. Get Column - Invalid Name Format
- **Test method:** `getColumn_ShouldThrowException_WhenNameIsInvalid`
- **Sequence diagram:** `TC-11E`
- **Input:** Invalid column name: `"col#invalid"`
- **Why:** Validates Column name format during lookup operations from a Table.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Column name contains invalid characters").

### TC-11F. Get Index - Invalid Name Format
- **Test method:** `getIndex_ShouldThrowException_WhenNameIsInvalid`
- **Sequence diagram:** `TC-11F`
- **Input:** Invalid index name: `"idx#invalid"`
- **Why:** Validates Index name format during lookup operations from a Table.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Index name contains invalid characters").

### TC-11G. Get Constraint - Invalid Name Format
- **Test method:** `getConstraint_ShouldThrowException_WhenNameIsInvalid`
- **Sequence diagram:** `TC-11G`
- **Input:** Invalid constraint name: `"fk#invalid"`
- **Why:** Validates Constraint name format during lookup operations from a Table.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Constraint name contains invalid characters").

---

## 6. ColumnTest

### TC-12. Change Data Type (Happy Path)
- **Test method:** `changeDataType_ShouldUpdateColumnDataType_WhenValid`
- **Sequence diagram:** `TC-12`
- **Input:** Existing `INT` column, target type `BIGINT`
- **Why:** Ensures column data type is updated when the conversion is supported.
- **Expected output:**
  - Column data type updated to `BIGINT`.

### TC-12A. Change Data Type - Unsupported Conversion
- **Test method:** `changeDataType_ShouldThrowException_WhenUnsupportedConversion`
- **Sequence diagram:** `TC-12A`
- **Input:** Existing `INT` column, target type `BOOLEAN`
- **Why:** Blocks incompatible data type conversions (e.g. `INT` to `BOOLEAN`).
- **Expected output:**
  - Throws `IllegalArgumentException` ("Unsupported conversion").

### TC-12B. Change Data Type - Permission Denied
- **Test method:** `changeDataType_ShouldThrowException_WhenPermissionDenied`
- **Sequence diagram:** `TC-12B`
- **Input:** Restricted column alter operation
- **Why:** Verifies security access control when modifying column data types.
- **Expected output:**
  - Throws `SecurityException` ("Permission denied").

---

### TC-13. Set Default Value (Happy Path)
- **Test method:** `changeDataType_And_setDefaultValue_ShouldUpdateColumnProperties_WhenModified`
- **Sequence diagram:** `TC-13`
- **Input:** `nullable: false`, `defaultValue: "18"`
- **Why:** Ensures accurate updates of column default values and nullability properties.
- **Expected output:**
  - Properties updated accordingly.

### TC-13A. Set Default Value - Invalid Value Format
- **Test method:** `setDefaultValue_ShouldThrowException_WhenDefaultValueIsInvalid`
- **Sequence diagram:** `TC-13A`
- **Input:** `INT` column, default value: `"abc_not_an_int"`
- **Why:** Blocks default values whose format or data type conflicts with the column's data type.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Invalid default value").

---

## 7. IndexTest

### TC-14. Add Index (Happy Path)
- **Test method:** `addIndex_ShouldAttachIndexToTable_WhenIndexIsAddedAndRebuilt`
- **Sequence diagram:** `TC-14`
- **Input:** `Index("idx_user_email", IndexType.BTREE)`
- **Why:** Ensures successful attachment of an Index to a Table and manages index rebuild lifecycle.
- **Expected output:**
  - Index attached, disabled, and re-enabled after rebuild.

### TC-14A. Add Index - Duplicate Index Name
- **Test method:** `addIndex_ShouldThrowException_WhenDuplicateIndexName`
- **Sequence diagram:** `TC-14A`
- **Input:** Index with name `"idx_user_email"` (already existing)
- **Why:** Blocks creating duplicate Index names within the same Table.
- **Expected output:**
  - Throws `IllegalStateException` ("Index already exists").

### TC-14B. Add Index - Indexed Column Not Found
- **Test method:** `addIndex_ShouldThrowException_WhenIndexedColumnNotFound`
- **Sequence diagram:** `TC-14B`
- **Input:** Index targeting non-existing column `"missing_col"`
- **Why:** Ensures columns targeted by an Index actually exist in the Table.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Indexed column not found").

### TC-14C. Remove Index (Happy Path)
- **Test method:** `removeIndex_ShouldRemoveIndexFromTable_WhenIndexExists`
- **Sequence diagram:** `TC-14C`
- **Input:** Index name `"idx_user_email"`
- **Why:** Ensures successful removal of an Index from a Table.
- **Expected output:**
  - Index removed from table.

### TC-14D. Remove Index - Not Found
- **Test method:** `removeIndex_ShouldThrowException_WhenIndexNotFound`
- **Sequence diagram:** `TC-14D`
- **Input:** Non-existing index name `"missing_idx"`
- **Why:** Reports explicit errors when removing a non-existent Index.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Index not found").

### TC-14E. Remove Index - Table Locked
- **Test method:** `removeIndex_ShouldThrowException_WhenTableIsLocked`
- **Sequence diagram:** `TC-14E`
- **Input:** Table locked
- **Why:** Blocks Index removal when the Table is locked.
- **Expected output:**
  - Throws `IllegalStateException` ("Table is locked").

### TC-14F. Remove Index - Invalid Name Format
- **Test method:** `removeIndex_ShouldThrowException_WhenIndexNameIsInvalid`
- **Sequence diagram:** `TC-14F`
- **Input:** Invalid name: `""`
- **Why:** Validates Index name format during deletion calls.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Value is empty").

---

### TC-15. Rebuild Index (Happy Path)
- **Test method:** `rebuildIndex_ShouldReenableIndex_WhenRebuilt`
- **Sequence diagram:** `TC-15`
- **Input:** Disabled index
- **Why:** Verifies restoring `enabled = true` status after rebuilding an Index.
- **Expected output:**
  - Index enabled status set to `true`.

### TC-15A. Rebuild Index - Corrupted Index
- **Test method:** `rebuildIndex_ShouldThrowException_WhenIndexIsDisabledAndCorrupted`
- **Sequence diagram:** `TC-15A`
- **Input:** Corrupted/invalid index state
- **Why:** Blocks re-enabling an Index with corrupted structural data.
- **Expected output:**
  - Throws `IllegalStateException` ("Index is corrupted").

---

## 8. ConstraintTest

### TC-16. Validate Primary Key Constraint (Happy Path)
- **Test method:** `validate_ShouldReturnConstraintStatus_WhenPrimaryKeyIsChecked`
- **Sequence diagram:** `TC-16`
- **Input:** `PrimaryKeyConstraint("pk_users")`
- **Why:** Verifies Primary Key constraint status checking when toggled enabled/disabled.
- **Expected output:**
  - Returns `true` when enabled, `false` when disabled.

### TC-16A. Remove Constraint (Happy Path)
- **Test method:** `removeConstraint_ShouldRemoveConstraintFromTable_WhenConstraintExists`
- **Sequence diagram:** `TC-16A`
- **Input:** Constraint name `"pk_orders"`
- **Why:** Ensures successful removal of a Constraint from a Table.
- **Expected output:**
  - Constraint removed from table.

### TC-16B. Remove Constraint - Not Found
- **Test method:** `removeConstraint_ShouldThrowException_WhenConstraintNotFound`
- **Sequence diagram:** `TC-16B`
- **Input:** Non-existing constraint name `"missing_pk"`
- **Why:** Reports explicit errors when removing a non-existent Constraint.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Constraint not found").

### TC-16C. Remove Constraint - Table Locked
- **Test method:** `removeConstraint_ShouldThrowException_WhenTableIsLocked`
- **Sequence diagram:** `TC-16C`
- **Input:** Table locked
- **Why:** Blocks Constraint removal when the Table is locked.
- **Expected output:**
  - Throws `IllegalStateException` ("Table is locked").

---

### TC-17. Validate Foreign Key (Happy Path)
- **Test method:** `validateReference_ShouldReturnTrue_WhenForeignKeyReferencesValidTableAndColumn`
- **Sequence diagram:** `TC-17`
- **Input:** Foreign Key referencing valid parent table and column
- **Why:** Ensures Foreign Key constraints are valid when referenced parent tables and columns exist.
- **Expected output:**
  - `validateReference()` returns `true`.

### TC-17A. Validate Foreign Key - Referenced Table Missing
- **Test method:** `validateForeignKey_ShouldReturnFalse_WhenReferencedTableMissing`
- **Sequence diagram:** `TC-17A`
- **Input:** Foreign Key referencing missing parent table
- **Why:** Reports referential integrity errors when the referenced parent table is missing.
- **Expected output:**
  - `validateReference()` returns `false`.

### TC-17B. Validate Foreign Key - Referenced Column Missing
- **Test method:** `validateForeignKey_ShouldReturnFalse_WhenReferencedColumnMissing`
- **Sequence diagram:** `TC-17B`
- **Input:** Foreign Key referencing missing column in parent table
- **Why:** Reports referential integrity errors when the referenced parent column is missing.
- **Expected output:**
  - `validateReference()` returns `false`.

### TC-17C. Validate Foreign Key - Parent Row Missing
- **Test method:** `validateForeignKey_ShouldReturnFalse_WhenParentRowMissing`
- **Sequence diagram:** `TC-17C`
- **Input:** Foreign Key value with no matching parent row
- **Why:** Verifies row-level referential data integrity when no matching parent row exists.
- **Expected output:**
  - `validateReference()` returns `false`.

---

### TC-18. Evaluate Check Constraint (Happy Path)
- **Test method:** `evaluate_ShouldReturnTrue_WhenCheckConstraintExpressionIsValid`
- **Sequence diagram:** `TC-18`
- **Input:** `CheckConstraint("chk_age", "age >= 18")`
- **Why:** Ensures valid Check Constraint expressions evaluate to `true`.
- **Expected output:**
  - `evaluate()` returns `true`.

### TC-18A. Evaluate Check Constraint - Invalid Expression
- **Test method:** `evaluate_ShouldReturnFalse_WhenExpressionIsInvalid`
- **Sequence diagram:** `TC-18A`
- **Input:** Invalid syntax expression: `"age >= "`
- **Why:** Verifies syntax error handling for invalid Check Constraint expressions.
- **Expected output:**
  - `evaluate()` returns `false`.

---

## 9. ColumnBuilderTest

### TC-19. Build Column (Happy Path)
- **Test method:** `build_ShouldConstructColumn_WhenValidPropertiesSet`
- **Sequence diagram:** `TC-19`
- **Input:** `name: "email"`, `dataType: DataType.VARCHAR`, `nullable: false`, `defaultValue: "N/A"`
- **Why:** Verifies the Builder pattern constructs a fully populated `Column` object with exact properties.
- **Expected output:**
  - Non-null `Column` with exact matching properties.

### TC-19A. Build Column with Default Values
- **Test method:** `build_ShouldApplyDefaults_WhenOptionalPropertiesOmitted`
- **Sequence diagram:** `TC-19A`
- **Input:** `name: "id"`, `dataType: DataType.INT`
- **Why:** Verifies default property values (`nullable = true`, `defaultValue = null`) when optional parameters are omitted.
- **Expected output:**
  - `nullable` defaults to `true`, `defaultValue` is `null`.

### TC-19B. Build Column - Null or Empty Name
- **Test method:** `build_ShouldThrowException_WhenColumnNameIsNullOrEmpty`
- **Sequence diagram:** `TC-19B`
- **Input:** `name: null` or `""`
- **Why:** Prevents creating unnamed or empty-named Columns via the Builder.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Value is empty").

### TC-19C. Build Column - Invalid Characters in Name
- **Test method:** `build_ShouldThrowException_WhenColumnNameContainsInvalidCharacters`
- **Sequence diagram:** `TC-19C`
- **Input:** `name: "invalid#col"`
- **Why:** Ensures the Builder applies identifier validation against special characters.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Column name contains invalid characters").

### TC-19D. Build Column - Null Data Type
- **Test method:** `build_ShouldThrowException_WhenDataTypeIsNull`
- **Sequence diagram:** `TC-19D`
- **Input:** `name: "email"`, `dataType: null`
- **Why:** Ensures Columns are built with an explicitly required DataType.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Data type cannot be null").

### TC-19E. Build Column - Invalid Default Value
- **Test method:** `build_ShouldThrowException_WhenDefaultValueIsInvalidForType`
- **Sequence diagram:** `TC-19E`
- **Input:** `name: "age"`, `dataType: DataType.INT`, `defaultValue: "not_an_int"`
- **Why:** Verifies the Builder validates compatibility between default value format and Column data type.
- **Expected output:**
  - Throws `IllegalArgumentException` ("Invalid default value").

---

## 10. TableMementoTest

### TC-20. Memento Snapshot and Restore (Happy Path)
- **Test method:** `createMemento_ShouldCaptureSnapshot_And_restore_ShouldRevertState`
- **Sequence diagram:** `TC-20`
- **Input:** `Table("products")` with initial columns
- **Why:** Verifies the Memento pattern restores table columns to their original snapshot state after modifications.
- **Expected output:**
  - Restored `table` matches initial snapshot columns.

### TC-20A. Restore with Null Memento
- **Test method:** `restore_ShouldDoNothing_WhenMementoIsNull`
- **Sequence diagram:** `TC-20A`
- **Input:** `null` Memento
- **Why:** Ensures `restore` handles null Memento objects safely without throwing exceptions.
- **Expected output:**
  - Table state remains unchanged without throwing exceptions.

### TC-20B. Restore Table Name and Columns
- **Test method:** `restore_ShouldRevertTableNameAndColumns_WhenRenamedAndColumnsRemoved`
- **Sequence diagram:** `TC-20B`
- **Input:** Renamed table and removed columns
- **Why:** Ensures rollback restores both table name and columns previously removed.
- **Expected output:**
  - Table name and columns restored to original snapshot state.

### TC-20C. Memento Snapshot Immutability
- **Test method:** `createMemento_ShouldMaintainIndependentSnapshot_WhenTableIsModified`
- **Sequence diagram:** `TC-20C`
- **Input:** Table modified after creating Memento
- **Why:** Verifies Memento encapsulation and immutability, ensuring subsequent table changes do not affect the snapshot.
- **Expected output:**
  - Memento snapshot maintains original state independently.

### TC-20D. Restore to Empty Columns
- **Test method:** `restore_ShouldRevertToEmptyColumns_WhenSnapshotWasEmpty`
- **Sequence diagram:** `TC-20D`
- **Input:** Snapshot taken when table had 0 columns
- **Why:** Verifies rollback capability to an initial empty-column state.
- **Expected output:**
  - Restored table has 0 columns.

---

## 11. ConstraintValidationChainTest

### TC-21. Validate Chain (Pass)
- **Test method:** `validateAll_ShouldReturnTrue_WhenAllConstraintsInChainAreValid`
- **Sequence diagram:** `TC-21`
- **Input:** Chain with valid PK and Check constraints
- **Why:** Verifies the Chain of Responsibility pattern evaluates all constraints in the validation chain.
- **Expected output:**
  - `validateAll()` returns `true`.

### TC-21A. Validate Chain (Fail Fast)
- **Test method:** `validateAll_ShouldReturnFalse_WhenAnyConstraintInChainFails`
- **Sequence diagram:** `TC-21A`
- **Input:** Chain containing an invalid constraint
- **Why:** Verifies fail-fast behavior returning `false` immediately upon encountering an invalid constraint.
- **Expected output:**
  - `validateAll()` returns `false`.

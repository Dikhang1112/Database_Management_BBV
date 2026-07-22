# Metadata Unit Test Scenarios Mindmap

This mindmap represents the structural taxonomy of the Metadata unit test scenarios, showing the coverage across `CatalogManager`, `Database`, `Schema`, `Table`, `Column`, `Index`, and `Constraint` classes.

```mermaid
flowchart LR
    Root(("Metadata Unit Tests"))

    %% =====================================================
    %% Categories
    %% =====================================================

    Cat1(["1. CatalogManagerTest"])
    Cat2(["2. DatabaseTest"])
    Cat3(["3. SchemaTest"])
    Cat4(["4. TableTest"])
    Cat5(["5. ColumnTest"])
    Cat6(["6. IndexTest"])
    Cat7(["7. ConstraintTest"])
    Cat8(["8. MetadataModuleTest"])
    Cat9(["9. DDLCommandTest"])
    Cat10(["10. ColumnBuilderTest"])
    Cat11(["11. TableMementoTest"])
    Cat12(["12. ConstraintValidationChainTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4
    Root --> Cat5
    Root --> Cat6
    Root --> Cat7
    Root --> Cat8
    Root --> Cat9
    Root --> Cat10
    Root --> Cat11
    Root --> Cat12

    %% =====================================================
    %% CatalogManager
    %% =====================================================

    Cat1 --> TC01("TC-01 CreateDatabase")
    TC01 --> TC01A("Database already exists")
    TC01 --> TC01B("Invalid database name")
    TC01 --> TC01C("Permission denied")
    TC01 --> TC01D("Special characters")

    Cat1 --> TC02("TC-02 DropDatabase")
    TC02 --> TC02A("Database not found")
    TC02 --> TC02B("Database not empty")
    TC02 --> TC02C("Permission denied")

    Cat1 --> TC03("TC-03 ListDatabases")

    Cat1 --> TC04("TC-04 ClearCatalog")

    %% =====================================================
    %% Database
    %% =====================================================

    Cat2 --> TC05("TC-05 CreateSchema")
    TC05 --> TC05A("Schema already exists")
    TC05 --> TC05B("Database offline")
    TC05 --> TC05C("Permission denied")
    TC05 --> TC05D("Special characters")

    Cat2 --> TC06("TC-06 RenameDatabase")
    TC06 --> TC06A("Duplicate database name")
    TC06 --> TC06B("Invalid name")

    %% =====================================================
    %% Schema
    %% =====================================================

    Cat3 --> TC07("TC-07 CreateTable")
    TC07 --> TC07A("Table already exists")
    TC07 --> TC07B("Permission denied")
    TC07 --> TC07C("Schema read-only")
    TC07 --> TC07D("Special characters")

    Cat3 --> TC08("TC-08 RenameSchema")
    TC08 --> TC08A("Schema not found")

    %% =====================================================
    %% Table
    %% =====================================================

    Cat4 --> TC09("TC-09 AddColumn")
    TC09 --> TC09A("Column already exists")
    TC09 --> TC09B("Table locked")
    TC09 --> TC09C("Permission denied")
    TC09 --> TC09D("Special characters")

    Cat4 --> TC10("TC-10 RemoveColumn")
    TC10 --> TC10A("Column not found")
    TC10 --> TC10B("Referenced by constraint")

    %% =====================================================
    %% Column
    %% =====================================================

    Cat5 --> TC11("TC-11 ChangeDataType")
    TC11 --> TC11A("Unsupported conversion")
    TC11 --> TC11B("Permission denied")

    Cat5 --> TC12("TC-12 SetDefaultValue")
    TC12 --> TC12A("Invalid default value")

    %% =====================================================
    %% Index
    %% =====================================================

    Cat6 --> TC13("TC-13 AddIndex")
    TC13 --> TC13A("Duplicate index")
    TC13 --> TC13B("Invalid indexed column")

    Cat6 --> TC14("TC-14 RebuildIndex")
    TC14 --> TC14A("Disabled index")

    %% =====================================================
    %% Constraint
    %% =====================================================

    Cat7 --> TC15("TC-15 ValidateConstraint")

    Cat7 --> TC16("TC-16 ValidateForeignKey")
    TC16 --> TC16A("Referenced table missing")
    TC16 --> TC16B("Referenced column missing")
    TC16 --> TC16C("Parent row missing")

    Cat7 --> TC17("TC-17 EvaluateCheckConstraint")
    TC17 --> TC17A("Expression invalid")

    %% =====================================================
    %% MetadataModule
    %% =====================================================

    Cat8 --> TC18("TC-18 GetTableFacade")
    Cat8 --> TC18A("ExecuteDDLFacade")

    %% =====================================================
    %% DDLCommand
    %% =====================================================

    Cat9 --> TC19("TC-19 DDLCommandExecution")
    TC19 --> TC19A("CreateDatabaseCommand")
    TC19 --> TC19B("DropDatabaseCommand")
    TC19 --> TC19C("RenameDatabaseCommand")
    TC19 --> TC19D("CreateSchemaCommand")
    TC19 --> TC19E("DropSchemaCommand")
    TC19 --> TC19F("RenameSchemaCommand")
    TC19 --> TC19G("CreateTableCommand")
    TC19 --> TC19H("DropTableCommand")
    TC19 --> TC19I("RenameTableCommand")
    TC19 --> TC19J("CreateColumnCommand")
    TC19 --> TC19K("DropColumnCommand")
    TC19 --> TC19L("RenameColumnCommand")

    %% =====================================================
    %% ColumnBuilder
    %% =====================================================

    Cat10 --> TC20("TC-20 BuildColumn")
    TC20 --> TC20A("BuildColumnDefaults")

    %% =====================================================
    %% TableMemento
    %% =====================================================

    Cat11 --> TC21("TC-21 MementoSnapshotRestore")

    %% =====================================================
    %% ConstraintValidationChain
    %% =====================================================

    Cat12 --> TC22("TC-22 ValidateChainPass")
    TC22 --> TC22A("ValidateChainFail")
```


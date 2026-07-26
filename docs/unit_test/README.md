# DBMS Unit Test Scenarios & Test Coverage Architecture

This document provides a comprehensive roadmap of all unit test scenarios across all DBMS system modules and class/interface components.
---

## 1. Summary Testcase Statistics Tables

### Table 1: Summary Statistics by Module

| STT | Module | Number of Test Classes | Total Testcases | Status |
|:---:|:---|:---:|:---:|:---:|
| 1 | Metadata | 12 | 77 | Done |
| 2 | Query Processor | 12 | 43 | Doing |
| 3 | Database Core Server | 5 | 35 | Planned |
| 4 | Execution Engine | 9 | 55 | Planned |
| 5 | Storage Engine | 10 | 65 | Planned |
| 6 | Durability & Recovery | 4 | 25 | Planned |
| 7 | Security & Permissions | 4 | 22 | Planned |
| 8 | Performance & Scalability | 3 | 18 | Planned |
| 9 | Monitoring | 2 | 12 | Planned |
| 10 | Automation | 2 | 10 | Planned |
| **Total** | **10 Modules** | **63 Classes** | **362 Testcases** | |

---

## 2. Master System Unit Test Suite Flowchart

```mermaid
flowchart LR
    Root(("DBMS Master Unit Test Suite"))

    Mod1(["1. Metadata Tests"])
    Mod2(["2. Query Processor Tests"])
    Mod3(["3. Database Core Server Tests"])
    Mod4(["4. Execution Engine Tests"])
    Mod5(["5. Storage Engine Tests"])
    Mod6(["6. Durability & Recovery Tests"])
    Mod7(["7. Security & Permissions Tests"])
    Mod8(["8. Performance & Scalability Tests"])
    Mod9(["9. Monitoring Tests"])
    Mod10(["10. Automation Tests"])

    Root --> Mod1
    Root --> Mod2
    Root --> Mod3
    Root --> Mod4
    Root --> Mod5
    Root --> Mod6
    Root --> Mod7
    Root --> Mod8
    Root --> Mod9
    Root --> Mod10
```

---

## 3. Module Unit Test Flowcharts

### 3.1. Metadata Module (`Metadata`)

```mermaid
flowchart LR
    Root(("Metadata Unit Tests"))

    %% =====================================================
    %% Categories (Top to Bottom Hierarchy)
    %% =====================================================

    Cat1(["1. MetadataModuleTest"])
    Cat2(["2. CatalogManagerTest"])
    Cat3(["3. DatabaseTest"])
    Cat4(["4. SchemaTest"])
    Cat5(["5. TableTest"])
    Cat6(["6. ColumnTest"])
    Cat7(["7. IndexTest"])
    Cat8(["8. ConstraintTest"])
    Cat9(["9. ColumnBuilderTest"])
    Cat10(["10. TableMementoTest"])
    Cat11(["11. ConstraintValidationChainTest"])

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

    %% =====================================================
    %% 1. MetadataModule (Facade Entry Point)
    %% =====================================================

    Cat1 --> TC01("TC-01 GetTableFacade")
    Cat1 --> TC01A("TC-01A ExecuteDDLFacade")
    Cat1 --> TC01B("TC-01B GetDatabaseFacade")
    Cat1 --> TC01C("TC-01C GetDatabaseFacade invalid name")
    Cat1 --> TC01D("TC-01D GetTableFacade invalid identifier")
    Cat1 --> TC01E("TC-01E GetTableFacade not found")
    Cat1 --> TC01F("TC-01F ExecuteDDLFacade null command")

    %% =====================================================
    %% 2. CatalogManager (Root Catalog Level)
    %% =====================================================

    Cat2 --> TC02("TC-02 CreateDatabase")
    TC02 --> TC02A("TC-02A Database already exists")
    TC02 --> TC02B("TC-02B Invalid database name")
    TC02 --> TC02C("TC-02C Permission denied")
    TC02 --> TC02D("TC-02D Special characters")
    TC02 --> TC02E("TC-02E Rename database valid")
    TC02 --> TC02F("TC-02F Rename old database missing")
    TC02 --> TC02G("TC-02G Rename new database duplicate")

    Cat2 --> TC03("TC-03 DropDatabase")
    TC03 --> TC03A("TC-03A Database not found")
    TC03 --> TC03B("TC-03B Database not empty")
    TC03 --> TC03C("TC-03C Permission denied")

    Cat2 --> TC04("TC-04 ListDatabases")
    TC04 --> TC04A("TC-04A Get Database invalid name")

    Cat2 --> TC05("TC-05 ClearCatalog")

    %% =====================================================
    %% 3. Database (Database Level)
    %% =====================================================

    Cat3 --> TC06("TC-06 CreateSchema")
    TC06 --> TC06A("TC-06A Schema already exists")
    TC06 --> TC06B("TC-06B Database offline")
    TC06 --> TC06C("TC-06C Permission denied")
    TC06 --> TC06D("TC-06D Special characters")

    Cat3 --> TC07("TC-07 DatabaseStatus")

    %% =====================================================
    %% 4. Schema (Schema Level)
    %% =====================================================

    Cat4 --> TC08("TC-08 CreateTable")
    TC08 --> TC08A("TC-08A Table already exists")
    TC08 --> TC08B("TC-08B Permission denied")
    TC08 --> TC08C("TC-08C Schema read-only")
    TC08 --> TC08D("TC-08D Special characters")

    Cat4 --> TC09("TC-09 ListTables")
    TC09 --> TC09B("TC-09B Get Table invalid name")

    %% =====================================================
    %% 5. Table (Table Level)
    %% =====================================================

    Cat5 --> TC10("TC-10 AddColumn")
    TC10 --> TC10A("TC-10A Column already exists")
    TC10 --> TC10B("TC-10B Table locked")
    TC10 --> TC10C("TC-10C Permission denied")
    TC10 --> TC10D("TC-10D Special characters")
    TC10 --> TC10E("TC-10E Create column facade")

    Cat5 --> TC11("TC-11 RemoveColumn")
    TC11 --> TC11A("TC-11A Column not found")
    TC11 --> TC11B("TC-11B Referenced by constraint")
    TC11 --> TC11C("TC-11C Table locked when remove column")
    TC11 --> TC11D("TC-11D Invalid column name when remove column")
    TC11 --> TC11E("TC-11E Get Column invalid name")
    TC11 --> TC11F("TC-11F Get Index invalid name")
    TC11 --> TC11G("TC-11G Get Constraint invalid name")

    %% =====================================================
    %% 6. Column (Column Level)
    %% =====================================================

    Cat6 --> TC12("TC-12 ChangeDataType")
    TC12 --> TC12A("TC-12A Unsupported conversion")
    TC12 --> TC12B("TC-12B Permission denied")

    Cat6 --> TC13("TC-13 SetDefaultValue")
    TC13 --> TC13A("TC-13A Invalid default value")
    TC13 --> TC13B("TC-13B Rename column permission denied")
    TC13 --> TC13C("TC-13C Rename column special characters")

    %% =====================================================
    %% 7. Index (Index Level)
    %% =====================================================

    Cat7 --> TC14("TC-14 AddIndex")
    TC14 --> TC14A("TC-14A Duplicate index")
    TC14 --> TC14B("TC-14B Invalid indexed column")
    TC14 --> TC14C("TC-14C Remove index")
    TC14 --> TC14D("TC-14D Index not found when remove index")
    TC14 --> TC14E("TC-14E Table locked when remove index")
    TC14 --> TC14F("TC-14F Invalid index name format")

    Cat7 --> TC15("TC-15 RebuildIndex")
    TC15 --> TC15A("TC-15A Disabled index")

    %% =====================================================
    %% 8. Constraint (Constraint Level)
    %% =====================================================

    Cat8 --> TC16("TC-16 ValidateConstraint")
    TC16 --> TC16A("TC-16A Remove constraint")
    TC16 --> TC16B("TC-16B Constraint not found when remove constraint")
    TC16 --> TC16C("TC-16C Table locked when remove constraint")

    Cat8 --> TC17("TC-17 ValidateForeignKey")
    TC17 --> TC17A("TC-17A Referenced table missing")
    TC17 --> TC17B("TC-17B Referenced column missing")
    TC17 --> TC17C("TC-17C Parent row missing")

    Cat8 --> TC18("TC-18 EvaluateCheckConstraint")
    TC18 --> TC18A("TC-18A Expression invalid")

    %% =====================================================
    %% 9. ColumnBuilder
    %% =====================================================

    Cat9 --> TC19("TC-19 BuildColumn")
    TC19 --> TC19A("TC-19A BuildColumnDefaults")
    TC19 --> TC19B("TC-19B Null or empty name")
    TC19 --> TC19C("TC-19C Invalid name characters")
    TC19 --> TC19D("TC-19D Null data type")
    TC19 --> TC19E("TC-19E Invalid default value")

    %% =====================================================
    %% 10. TableMemento
    %% =====================================================

    Cat10 --> TC20("TC-20 MementoSnapshotRestore")
    TC20 --> TC20A("TC-20A Restore null memento")
    TC20 --> TC20B("TC-20B Restore table name and columns")
    TC20 --> TC20C("TC-20C Memento snapshot immutability")
    TC20 --> TC20D("TC-20D Restore to empty columns")

    %% =====================================================
    %% 11. ConstraintValidationChain
    %% =====================================================

    Cat11 --> TC21("TC-21 ValidateChainPass")
    TC21 --> TC21A("TC-21A ValidateChainFail")
```

---

### 3.2. Query Processor Module (`QueryProcessor`)

```mermaid
flowchart LR
    Root(("Query Processor Unit Tests"))

    Cat1(["1. LexerTest"])
    Cat2(["2. TokenStreamTest"])
    Cat3(["3. TokenTest"])
    Cat4(["4. SQLParserTest"])
    Cat5(["5. ASTBuilderTest"])
    Cat6(["6. ASTTest"])
    Cat7(["7. SelectASTNodeTest"])
    Cat8(["8. BinaryOpASTNodeTest"])
    Cat9(["9. IdentifierASTNodeTest"])
    Cat10(["10. LiteralASTNodeTest"])
    Cat11(["11. QueryOptimizerTest"])
    Cat12(["12. StatisticsManagerTest"])

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

    Cat1 --> TC01("TC-01 Tokenize Valid SQL")
    TC01 --> TC01A("Case-insensitive keywords")
    TC01 --> TC01B("Numeric and string literals")
    TC01 --> TC01C("Operators and punctuation")
    TC01 --> TC01D("Unterminated string literal")
    TC01 --> TC01E("Invalid character error")

    Cat2 --> TC02("TC-02 Consume Tokens")
    TC02 --> TC02A("LookAhead offset without advancing")
    TC02 --> TC02B("Empty stream boundary")
    TC02 --> TC02C("LookAhead out of bounds")
    TC02 --> TC02D("Consume past EOF")

    Cat3 --> TC03("TC-03 Create Token")
    TC03 --> TC03A("Token equality comparison")
    TC03 --> TC03B("Default constructor initialization")

    Cat4 --> TC04("TC-04 Parse Select Query")
    TC04 --> TC04A("Parse query without WHERE clause")
    TC04 --> TC04B("Syntax error missing FROM keyword")
    TC04 --> TC04C("Unexpected token error")
    TC04 --> TC04D("Empty token stream error")

    Cat5 --> TC05("TC-05 Build AST from ParseTree")
    TC05 --> TC05A("Map ParseTreeNode to ASTNode")

    Cat6 --> TC06("TC-06 Create AST")
    TC06 --> TC06A("Empty AST initialization")
    TC06 --> TC06B("Get root AST node")

    Cat7 --> TC07("TC-07 Build Select AST Node")
    TC07 --> TC07A("Null WHERE condition allowed")
    TC07 --> TC07B("Multiple projection fields")

    Cat8 --> TC08("TC-08 Build Binary Op Node")
    TC08 --> TC08A("Nested binary operators evaluation")

    Cat9 --> TC09("TC-09 Build Identifier Node")
    TC09 --> TC09A("Qualified column identifier (table.col)")

    Cat10 --> TC10("TC-10 Build Literal Node")
    TC10 --> TC10A("Integer literal node")
    TC10 --> TC10B("String literal node")
    TC10 --> TC10C("Boolean literal node")
    TC10 --> TC10D("NULL literal node")

    Cat11 --> TC11("TC-11 Generate Logical Plan")
    TC11 --> TC11A("Optimize logical plan to physical plan")
    TC11 --> TC11B("Estimate execution plan cost")
    TC11 --> TC11C("Select IndexScan when index exists")

    Cat12 --> TC12("TC-12 Estimate Table Cardinality")
    TC12 --> TC12A("Estimate predicate selectivity")
    TC12 --> TC12B("Table not found exception")
```

---

### 3.3. Database Core Server Module (`DatabaseCoreServer`)

```mermaid
flowchart LR
    Root(("Database Core Server Unit Tests"))

    Cat1(["1. DatabaseServerTest"])
    Cat2(["2. SessionManagerTest"])
    Cat3(["3. ConnectionManagerTest"])
    Cat4(["4. ConfigurationManagerTest"])
    Cat5(["5. ClientProtocolTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4
    Root --> Cat5

    Cat1 --> TC01("TC-01 startServer")
    Cat1 --> TC02("TC-02 stopServer")
    Cat1 --> TC03("TC-03 restartServer")
    Cat1 --> TC04("TC-04 checkServerStatus")
    Cat1 --> TC05("TC-05 handleServerPanic")
    Cat1 --> TC06("TC-06 reloadServerConfiguration")
    Cat1 --> TC07("TC-07 serverAlreadyRunningException")
    Cat1 --> TC08("TC-08 portInUseException")

    Cat2 --> TC09("TC-09 createSession")
    Cat2 --> TC10("TC-10 terminateSession")
    Cat2 --> TC11("TC-11 getSessionContext")
    Cat2 --> TC12("TC-12 validateSessionToken")
    Cat2 --> TC13("TC-13 sessionTimeoutExpiry")
    Cat2 --> TC14("TC-14 maxSessionsLimitExceeded")
    Cat2 --> TC15("TC-15 getActiveSessionsCount")
    Cat2 --> TC16("TC-16 invalidSessionTokenException")

    Cat3 --> TC17("TC-17 acceptConnection")
    Cat3 --> TC18("TC-18 closeConnection")
    Cat3 --> TC19("TC-19 getActiveConnectionCount")
    Cat3 --> TC20("TC-20 connectionPoolAcquire")
    Cat3 --> TC21("TC-21 connectionPoolRelease")
    Cat3 --> TC22("TC-22 connectionTimeoutException")
    Cat3 --> TC23("TC-23 maxConnectionsReachedException")

    Cat4 --> TC24("TC-24 loadConfiguration")
    Cat4 --> TC25("TC-25 getProperty")
    Cat4 --> TC26("TC-26 updateProperty")
    Cat4 --> TC27("TC-27 reloadOnFly")
    Cat4 --> TC28("TC-28 invalidConfigFileException")
    Cat4 --> TC29("TC-29 defaultPropertyFallback")
    Cat4 --> TC30("TC-30 exportConfiguration")

    Cat5 --> TC31("TC-31 protocolHandshake")
    Cat5 --> TC32("TC-32 parseProtocolMessage")
    Cat5 --> TC33("TC-33 formatResponsePacket")
    Cat5 --> TC34("TC-34 handleSslTlsHandshake")
    Cat5 --> TC35("TC-35 protocolVersionMismatchException")
```

---

### 3.4. Execution Engine Module (`ExecutionEngine`)

```mermaid
flowchart LR
    Root(("Execution Engine Unit Tests"))

    Cat1(["1. ExecutionPlannerTest"])
    Cat2(["2. ExecutionContextTest"])
    Cat3(["3. QueryExecutorTest"])
    Cat4(["4. ScanOperatorTest"])
    Cat5(["5. FilterOperatorTest"])
    Cat6(["6. JoinOperatorTest"])
    Cat7(["7. AggregateOperatorTest"])
    Cat8(["8. SortOperatorTest"])
    Cat9(["9. ResultSetTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4
    Root --> Cat5
    Root --> Cat6
    Root --> Cat7
    Root --> Cat8
    Root --> Cat9

    Cat1 --> TC01("TC-01 createExecutionPlan")
    Cat1 --> TC02("TC-02 validateExecutionPlan")
    Cat1 --> TC03("TC-03 pushDownPredicates")
    Cat1 --> TC04("TC-04 costBasedPlanSelection")
    Cat1 --> TC05("TC-05 unsupportedPlanException")
    Cat1 --> TC06("TC-06 optimizeSubqueryPlan")

    Cat2 --> TC07("TC-07 initializeContext")
    Cat2 --> TC08("TC-08 setVariable")
    Cat2 --> TC09("TC-09 getVariable")
    Cat2 --> TC10("TC-10 getTransactionContext")
    Cat2 --> TC11("TC-11 clearVariable")
    Cat2 --> TC12("TC-12 contextMemoryQuotaExceeded")

    Cat3 --> TC13("TC-13 executeQuery")
    Cat3 --> TC14("TC-14 executeUpdate")
    Cat3 --> TC15("TC-15 executeBatch")
    Cat3 --> TC16("TC-16 cancelExecution")
    Cat3 --> TC17("TC-17 queryExecutionTimeout")
    Cat3 --> TC18("TC-18 queryExecutionError")
    Cat3 --> TC19("TC-19 getExecutionMetrics")

    Cat4 --> TC20("TC-20 seqScanFetchNext")
    Cat4 --> TC21("TC-21 seqScanFilterPredicate")
    Cat4 --> TC22("TC-22 seqScanEmptyTable")
    Cat4 --> TC23("TC-23 indexScanFetchNext")
    Cat4 --> TC24("TC-24 indexScanKeyLookup")
    Cat4 --> TC25("TC-25 indexScanRangeScan")

    Cat5 --> TC26("TC-26 filterEvaluateTrue")
    Cat5 --> TC27("TC-27 filterEvaluateFalse")
    Cat5 --> TC28("TC-28 filterNullValue")
    Cat5 --> TC29("TC-29 filterCompositePredicate")
    Cat5 --> TC30("TC-30 filterExpressionError")

    Cat6 --> TC31("TC-31 nestedLoopJoinMatch")
    Cat6 --> TC32("TC-32 nestedLoopJoinEmpty")
    Cat6 --> TC33("TC-33 hashJoinBuildTable")
    Cat6 --> TC34("TC-34 hashJoinProbeTable")
    Cat6 --> TC35("TC-35 hashJoinNullKey")
    Cat6 --> TC36("TC-36 sortMergeJoin")

    Cat7 --> TC37("TC-37 countAggregate")
    Cat7 --> TC38("TC-38 sumAggregate")
    Cat7 --> TC39("TC-39 avgAggregate")
    Cat7 --> TC40("TC-40 minMaxAggregate")
    Cat7 --> TC41("TC-41 groupByAggregation")
    Cat7 --> TC42("TC-42 emptyGroupAggregate")

    Cat8 --> TC43("TC-43 sortAscending")
    Cat8 --> TC44("TC-44 sortDescending")
    Cat8 --> TC45("TC-45 sortMultipleColumns")
    Cat8 --> TC46("TC-46 externalSortDiskOverflow")
    Cat8 --> TC47("TC-47 sortEmptyResult")
    Cat8 --> TC48("TC-48 sortNullFirstLast")

    Cat9 --> TC49("TC-49 fetchNextRow")
    Cat9 --> TC50("TC-50 getColumnValue")
    Cat9 --> TC51("TC-51 closeResultSet")
    Cat9 --> TC52("TC-52 getResultSetMetadata")
    Cat9 --> TC53("TC-53 scrollResultSet")
    Cat9 --> TC54("TC-54 fetchPastLastRowException")
    Cat9 --> TC55("TC-55 typeMismatchException")
```

---

### 3.5. Storage Engine Module (`StorageEngine`)

```mermaid
flowchart LR
    Root(("Storage Engine Unit Tests"))

    Cat1(["1. FileManagerTest"])
    Cat2(["2. PageManagerTest"])
    Cat3(["3. BufferPoolManagerTest"])
    Cat4(["4. BufferPoolTest"])
    Cat5(["5. BufferFrameTest"])
    Cat6(["6. DataPageTest"])
    Cat7(["7. PageReplacerTest"])
    Cat8(["8. LockManagerTest"])
    Cat9(["9. LockTableTest"])
    Cat10(["10. TransactionManagerTest"])

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

    Cat1 --> TC01("TC-01 createDataFile")
    Cat1 --> TC02("TC-02 readBlock")
    Cat1 --> TC03("TC-03 writeBlock")
    Cat1 --> TC04("TC-04 deleteDataFile")
    Cat1 --> TC05("TC-05 extendFile")
    Cat1 --> TC06("TC-06 seekBlockPosition")
    Cat1 --> TC07("TC-07 diskFullException")
    Cat1 --> TC08("TC-08 fileNotFoundException")

    Cat2 --> TC09("TC-09 allocatePage")
    Cat2 --> TC10("TC-10 deallocatePage")
    Cat2 --> TC11("TC-11 getPageHeader")
    Cat2 --> TC12("TC-12 updatePageHeader")
    Cat2 --> TC13("TC-13 formatDataPage")
    Cat2 --> TC14("TC-14 checkPageChecksum")
    Cat2 --> TC15("TC-15 corruptedPageChecksumException")

    Cat3 --> TC16("TC-16 fetchPageFromBuffer")
    Cat3 --> TC17("TC-17 flushDirtyPage")
    Cat3 --> TC18("TC-18 unpinFrame")
    Cat3 --> TC19("TC-19 flushAllPages")
    Cat3 --> TC20("TC-20 pinCountManagement")
    Cat3 --> TC21("TC-21 bufferPoolCapacityFull")
    Cat3 --> TC22("TC-22 fetchNonExistentPageException")

    Cat4 --> TC23("TC-23 allocateFrames")
    Cat4 --> TC24("TC-24 getFrame")
    Cat4 --> TC25("TC-25 evictFrame")
    Cat4 --> TC26("TC-26 markFrameDirty")
    Cat4 --> TC27("TC-27 clearPool")

    Cat5 --> TC28("TC-28 pinFrame")
    Cat5 --> TC29("TC-29 unpinFrame")
    Cat5 --> TC30("TC-30 isDirty")
    Cat5 --> TC31("TC-31 getPageId")
    Cat5 --> TC32("TC-32 getContent")

    Cat6 --> TC33("TC-33 insertRecord")
    Cat6 --> TC34("TC-34 updateRecord")
    Cat6 --> TC35("TC-35 deleteRecord")
    Cat6 --> TC36("TC-36 compactPage")
    Cat6 --> TC37("TC-37 getRecordSlot")
    Cat6 --> TC38("TC-38 pageOverflowException")

    Cat7 --> TC39("TC-39 clockSelectVictim")
    Cat7 --> TC40("TC-40 clockAdvanceHand")
    Cat7 --> TC41("TC-41 clockSecondChanceFlag")
    Cat7 --> TC42("TC-42 allFramesPinnedException")
    Cat7 --> TC43("TC-43 fifoReplacerFallback")
    Cat7 --> TC44("TC-44 lruReplacerPolicy")

    Cat8 --> TC45("TC-45 acquireSharedLock")
    Cat8 --> TC46("TC-46 acquireExclusiveLock")
    Cat8 --> TC47("TC-47 releaseLock")
    Cat8 --> TC48("TC-48 promoteLock")
    Cat8 --> TC49("TC-49 checkLockConflict")
    Cat8 --> TC50("TC-50 lockTimeoutException")
    Cat8 --> TC51("TC-51 detectDeadlockCycle")
    Cat8 --> TC52("TC-52 lockUpgradeException")

    Cat9 --> TC53("TC-53 addLockEntry")
    Cat9 --> TC54("TC-54 removeLockEntry")
    Cat9 --> TC55("TC-55 getWaitingTransactions")
    Cat9 --> TC56("TC-56 isLockedByTransaction")
    Cat9 --> TC57("TC-57 clearLockTable")

    Cat10 --> TC58("TC-58 beginTransaction")
    Cat10 --> TC59("TC-59 commitTransaction")
    Cat10 --> TC60("TC-60 rollbackTransaction")
    Cat10 --> TC61("TC-61 setSavepoint")
    Cat10 --> TC62("TC-62 rollbackToSavepoint")
    Cat10 --> TC63("TC-63 getTransactionState")
    Cat10 --> TC64("TC-64 transactionAbortedStateException")
    Cat10 --> TC65("TC-65 concurrentTransactionsIsolation")
```

---

### 3.6. Durability & Recovery Data Module (`DurabilityData`)

```mermaid
flowchart LR
    Root(("Durability & Recovery Unit Tests"))

    Cat1(["1. WALManagerTest"])
    Cat2(["2. CheckpointManagerTest"])
    Cat3(["3. RecoveryManagerTest"])
    Cat4(["4. LogRecordTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4

    Cat1 --> TC01("TC-01 appendLogRecord")
    Cat1 --> TC02("TC-02 flushLogBuffer")
    Cat1 --> TC03("TC-03 getLSN")
    Cat1 --> TC04("TC-04 truncateWAL")
    Cat1 --> TC05("TC-05 parseLogRecord")
    Cat1 --> TC06("TC-06 logBufferOverflow")
    Cat1 --> TC07("TC-07 walWriteErrorException")
    Cat1 --> TC08("TC-08 getFlushedLSN")

    Cat2 --> TC09("TC-09 createCheckpoint")
    Cat2 --> TC10("TC-10 flushDirtyPagesOnCheckpoint")
    Cat2 --> TC11("TC-11 logCheckpointRecord")
    Cat2 --> TC12("TC-12 periodicCheckpointTrigger")
    Cat2 --> TC13("TC-13 checkpointFailureRecovery")
    Cat2 --> TC14("TC-14 getLastCheckpointLSN")

    Cat3 --> TC15("TC-15 performAnalysisPass")
    Cat3 --> TC16("TC-16 performRedoPass")
    Cat3 --> TC17("TC-17 performUndoPass")
    Cat3 --> TC18("TC-18 crashRecoveryWorkflow")
    Cat3 --> TC19("TC-19 emptyLogRecovery")
    Cat3 --> TC20("TC-20 corruptedLogRecordRecoveryException")

    Cat4 --> TC21("TC-21 createBeginRecord")
    Cat4 --> TC22("TC-22 createCommitRecord")
    Cat4 --> TC23("TC-23 createAbortRecord")
    Cat4 --> TC24("TC-24 createUpdateRecord")
    Cat4 --> TC25("TC-25 createCLRRecord")
```

---

### 3.7. Security & Permissions Module (`SecurityPermission`)

```mermaid
flowchart LR
    Root(("Security & Permissions Unit Tests"))

    Cat1(["1. AuthenticationManagerTest"])
    Cat2(["2. AuthorizationManagerTest"])
    Cat3(["3. AuditManagerTest"])
    Cat4(["4. RoleManagerTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4

    Cat1 --> TC01("TC-01 authenticateUserSuccess")
    Cat1 --> TC02("TC-02 authenticateUserFailure")
    Cat1 --> TC03("TC-03 hashPassword")
    Cat1 --> TC04("TC-04 verifyPasswordHash")
    Cat1 --> TC05("TC-05 userLockoutAfterFailedAttempts")
    Cat1 --> TC06("TC-06 expiredCredentialsException")

    Cat2 --> TC07("TC-07 checkTableReadPermission")
    Cat2 --> TC08("TC-08 checkTableWritePermission")
    Cat2 --> TC09("TC-09 checkSchemaAdminPermission")
    Cat2 --> TC10("TC-10 grantRoleToUser")
    Cat2 --> TC11("TC-11 revokeRoleFromUser")
    Cat2 --> TC12("TC-12 superUserBypass")

    Cat3 --> TC13("TC-13 logSecurityEvent")
    Cat3 --> TC14("TC-14 exportAuditTrail")
    Cat3 --> TC15("TC-15 filterAuditLogsBySeverity")
    Cat3 --> TC16("TC-16 auditLogRotation")
    Cat3 --> TC17("TC-17 auditDiskQuotaFullException")
    Cat3 --> TC18("TC-18 auditEntryEncryption")

    Cat4 --> TC19("TC-19 createRole")
    Cat4 --> TC20("TC-20 dropRole")
    Cat4 --> TC21("TC-21 addPermissionToRole")
    Cat4 --> TC22("TC-22 inheritsRoleHierarchy")
```

---

### 3.8. Performance & Scalability Module (`PerformanceScalability`)

```mermaid
flowchart LR
    Root(("Performance & Scalability Unit Tests"))

    Cat1(["1. CacheManagerTest"])
    Cat2(["2. MemoryManagerTest"])
    Cat3(["3. QueryPlanCacheTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3

    Cat1 --> TC01("TC-01 putInCache")
    Cat1 --> TC02("TC-02 getFromCache")
    Cat1 --> TC03("TC-03 invalidateCacheKey")
    Cat1 --> TC04("TC-04 clearCache")
    Cat1 --> TC05("TC-05 cacheEvictionLRU")
    Cat1 --> TC06("TC-06 cacheHitRatioCalculation")
    Cat1 --> TC07("TC-07 cacheCapacityOverflow")

    Cat2 --> TC08("TC-08 allocateMemoryPool")
    Cat2 --> TC09("TC-09 releaseMemoryPool")
    Cat2 --> TC10("TC-10 reserveMemoryChunk")
    Cat2 --> TC11("TC-11 getMemoryUsage")
    Cat2 --> TC12("TC-12 memoryLimitExceededException")
    Cat2 --> TC13("TC-13 garbageCollectPool")
    Cat2 --> TC14("TC-14 memoryFragmentationCheck")

    Cat3 --> TC15("TC-15 cachePhysicalPlan")
    Cat3 --> TC16("TC-16 lookupPhysicalPlan")
    Cat3 --> TC17("TC-17 invalidatePlanOnSchemaChange")
    Cat3 --> TC18("TC-18 planCacheHitMissMetrics")
```

---

### 3.9. Monitoring Module (`Monitoring`)

```mermaid
flowchart LR
    Root(("Monitoring Unit Tests"))

    Cat1(["1. MetricsCollectorTest"])
    Cat2(["2. AlertManagerTest"])

    Root --> Cat1
    Root --> Cat2

    Cat1 --> TC01("TC-01 recordQueryLatency")
    Cat1 --> TC02("TC-02 recordBufferPoolHitRatio")
    Cat1 --> TC03("TC-03 getSystemMetrics")
    Cat1 --> TC04("TC-04 recordTransactionThroughput")
    Cat1 --> TC05("TC-05 resetMetrics")
    Cat1 --> TC06("TC-06 exportMetricsToJson")
    Cat1 --> TC07("TC-07 activeConnectionsMetric")

    Cat2 --> TC08("TC-08 checkMetricThreshold")
    Cat2 --> TC09("TC-09 triggerAlert")
    Cat2 --> TC10("TC-10 silenceAlert")
    Cat2 --> TC11("TC-11 resolveAlert")
    Cat2 --> TC12("TC-12 alertNotificationChannel")
```

---

### 3.10. Automation Module (`Automation`)

```mermaid
flowchart LR
    Root(("Automation Unit Tests"))

    Cat1(["1. AutoVacuumTest"])
    Cat2(["2. AutoIndexerTest"])

    Root --> Cat1
    Root --> Cat2

    Cat1 --> TC01("TC-01 scanDeadTuples")
    Cat1 --> TC02("TC-02 compactTablePages")
    Cat1 --> TC03("TC-03 updateCatalogStatistics")
    Cat1 --> TC04("TC-04 autoVacuumScheduleTrigger")
    Cat1 --> TC05("TC-05 autoVacuumPauseOnHighLoad")
    Cat1 --> TC06("TC-06 vacuumLockTimeoutSkip")

    Cat2 --> TC07("TC-07 analyzeQueryWorkload")
    Cat2 --> TC08("TC-08 recommendIndex")
    Cat2 --> TC09("TC-09 createAutoIndex")
    Cat2 --> TC10("TC-10 dropUnusedAutoIndex")
```

# DBMS Unit Test Scenarios & Test Coverage Architecture

This document provides a roadmap of all unit test scenarios across all DBMS system modules defined in [HighLevel.md](file:///d:/BBV/Database_Management_BBV/docs/class_diagram/HighLevel.md).

---

## 1. Summary Testcase Statistics Tables

### Table 1: Summary Statistics by Module

| STT | Module | Number of Test Classes | Total Testcases | Status |
|:---:|:---|:---:|:---:|:---:|
| 1 | Metadata | 7 | 33 | Doing |
| 2 | Query Processor | 8 | 22 | Doing |
| 3 | Database Core Server | 4 | 12 | Planned |
| 4 | Execution Engine | 5 | 14 | Planned |
| 5 | Storage Engine | 6 | 14 | Planned |
| 6 | Durability & Recovery | 3 | 8 | Planned |
| 7 | Security & Permissions | 3 | 6 | Planned |
| 8 | Performance & Scalability | 2 | 5 | Planned |
| 9 | Monitoring | 1 | 3 | Planned |
| 10 | Automation | 1 | 3 | Planned |
| **Total** | **10 Modules** | **40 Classes** | **120 Testcases** | |

---

### Table 2: Detailed Statistics by Test Class

| Module | Test Class | Testcases Count |
|:---|:---|:---:|
| Metadata | CatalogManagerTest | 8 |
| Metadata | DatabaseTest | 5 |
| Metadata | SchemaTest | 4 |
| Metadata | TableTest | 5 |
| Metadata | ColumnTest | 3 |
| Metadata | IndexTest | 3 |
| Metadata | ConstraintTest | 5 |
| Query Processor | LexerTest | 2 |
| Query Processor | TokenStreamTest | 4 |
| Query Processor | TokenTest | 2 |
| Query Processor | SQLParserTest | 3 |
| Query Processor | ASTTest | 2 |
| Query Processor | ASTNodeTest | 4 |
| Query Processor | QueryOptimizerTest | 3 |
| Query Processor | StatisticsManagerTest | 2 |
| Database Core Server | DatabaseServerTest | 3 |
| Database Core Server | SessionManagerTest | 3 |
| Database Core Server | ConnectionManagerTest | 3 |
| Database Core Server | ConfigurationManagerTest | 3 |
| Execution Engine | ExecutionPlannerTest | 2 |
| Execution Engine | ExecutionContextTest | 2 |
| Execution Engine | QueryExecutorTest | 2 |
| Execution Engine | OperatorPipelineTest | 5 |
| Execution Engine | ResultSetTest | 3 |
| Storage Engine | FileManagerTest | 2 |
| Storage Engine | PageManagerTest | 2 |
| Storage Engine | BufferPoolManagerTest | 3 |
| Storage Engine | PageReplacerTest | 1 |
| Storage Engine | LockManagerTest | 3 |
| Storage Engine | TransactionManagerTest | 3 |
| Durability & Recovery | WALManagerTest | 3 |
| Durability & Recovery | CheckpointManagerTest | 2 |
| Durability & Recovery | RecoveryManagerTest | 3 |
| Security & Permissions | AuthenticationManagerTest | 2 |
| Security & Permissions | AuthorizationManagerTest | 2 |
| Security & Permissions | AuditManagerTest | 2 |
| Performance & Scalability | CacheManagerTest | 3 |
| Performance & Scalability | MemoryManagerTest | 2 |
| Monitoring | MetricsCollectorTest | 3 |
| Automation | AutoVacuumTest | 3 |

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

    Cat1(["1. CatalogManagerTest"])
    Cat2(["2. DatabaseTest"])
    Cat3(["3. SchemaTest"])
    Cat4(["4. TableTest"])
    Cat5(["5. ColumnTest"])
    Cat6(["6. IndexTest"])
    Cat7(["7. ConstraintTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4
    Root --> Cat5
    Root --> Cat6
    Root --> Cat7

    Cat1 --> TC01("TC-01 CreateDatabase")
    TC01 --> TC01A("Database already exists")
    TC01 --> TC01B("Invalid database name")
    TC01 --> TC01C("Permission denied")

    Cat1 --> TC02("TC-02 DropDatabase")
    TC02 --> TC02A("Database not found")
    TC02 --> TC02B("Database not empty")
    TC02 --> TC02C("Permission denied")

    Cat1 --> TC03("TC-03 ListDatabases")
    Cat1 --> TC04("TC-04 ClearCatalog")

    Cat2 --> TC05("TC-05 CreateSchema")
    TC05 --> TC05A("Schema already exists")
    TC05 --> TC05B("Database offline")
    TC05 --> TC05C("Permission denied")

    Cat2 --> TC06("TC-06 RenameDatabase")
    TC06 --> TC06A("Duplicate database name")
    TC06 --> TC06B("Invalid name")

    Cat3 --> TC07("TC-07 CreateTable")
    TC07 --> TC07A("Table already exists")
    TC07 --> TC07B("Permission denied")
    TC07 --> TC07C("Schema read-only")

    Cat3 --> TC08("TC-08 RenameSchema")
    TC08 --> TC08A("Schema not found")

    Cat4 --> TC09("TC-09 AddColumn")
    TC09 --> TC09A("Column already exists")
    TC09 --> TC09B("Table locked")
    TC09 --> TC09C("Permission denied")

    Cat4 --> TC10("TC-10 RemoveColumn")
    TC10 --> TC10A("Column not found")
    TC10 --> TC10B("Referenced by constraint")

    Cat5 --> TC11("TC-11 ChangeDataType")
    TC11 --> TC11A("Unsupported conversion")
    TC11 --> TC11B("Permission denied")

    Cat5 --> TC12("TC-12 SetDefaultValue")
    TC12 --> TC12A("Invalid default value")

    Cat6 --> TC13("TC-13 AddIndex")
    TC13 --> TC13A("Duplicate index")
    TC13 --> TC13B("Invalid indexed column")

    Cat6 --> TC14("TC-14 RebuildIndex")
    TC14 --> TC14A("Disabled index")

    Cat7 --> TC15("TC-15 ValidateConstraint")

    Cat7 --> TC16("TC-16 ValidateForeignKey")
    TC16 --> TC16A("Referenced table missing")
    TC16 --> TC16B("Referenced column missing")
    TC16 --> TC16C("Parent row missing")

    Cat7 --> TC17("TC-17 EvaluateCheckConstraint")
    TC17 --> TC17A("Expression invalid")
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
    Cat5(["5. ASTTest"])
    Cat6(["6. ASTNodeTest"])
    Cat7(["7. QueryOptimizerTest"])
    Cat8(["8. StatisticsManagerTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4
    Root --> Cat5
    Root --> Cat6
    Root --> Cat7
    Root --> Cat8

    Cat1 --> TC01("TC-01: tokenizeKeyword")
    Cat1 --> TC02("TC-02: scanIdentifierAndNumber")

    Cat2 --> TC03("TC-03: Create Token Stream")
    Cat2 --> TC04("TC-04: Consume Sequential Tokens")
    Cat2 --> TC05("TC-05: LookAhead Without Advancing")
    Cat2 --> TC06("TC-06: Empty Stream Boundary")

    Cat3 --> TC07("TC-07: Create Token")
    Cat3 --> TC08("TC-08: Compare Tokens Equality")

    Cat4 --> TC09("TC-09: parseSelectQuery")
    Cat4 --> TC10("TC-10: parseWhereCondition")
    Cat4 --> TC11("TC-11: parseInvalidSyntax")

    Cat5 --> TC12("TC-12: Create AST")
    Cat5 --> TC13("TC-13: Get Root Node")

    Cat6 --> TC14("TC-14: SelectASTNode")
    Cat6 --> TC15("TC-15: BinaryOpASTNode")
    Cat6 --> TC16("TC-16: IdentifierASTNode")
    Cat6 --> TC17("TC-17: LiteralASTNode")

    Cat7 --> TC18("TC-18: generateLogicalPlan")
    Cat7 --> TC19("TC-19: optimizePhysicalPlan")
    Cat7 --> TC20("TC-20: estimateCost")

    Cat8 --> TC21("TC-21: estimateCardinality")
    Cat8 --> TC22("TC-22: estimateSelectivity")
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

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4

    Cat1 --> TC01("TC-01: startServer")
    Cat1 --> TC02("TC-02: stopServer")
    Cat1 --> TC03("TC-03: checkServerStatus")

    Cat2 --> TC04("TC-04: createSession")
    Cat2 --> TC05("TC-05: terminateSession")
    Cat2 --> TC06("TC-06: getSessionContext")

    Cat3 --> TC07("TC-07: acceptConnection")
    Cat3 --> TC08("TC-08: closeConnection")
    Cat3 --> TC09("TC-09: activeConnectionCount")

    Cat4 --> TC10("TC-10: loadConfiguration")
    Cat4 --> TC11("TC-11: getProperty")
    Cat4 --> TC12("TC-12: updateProperty")
```

---

### 3.4. Execution Engine Module (`ExecutionEngine`)

```mermaid
flowchart LR
    Root(("Execution Engine Unit Tests"))

    Cat1(["1. ExecutionPlannerTest"])
    Cat2(["2. ExecutionContextTest"])
    Cat3(["3. QueryExecutorTest"])
    Cat4(["4. OperatorPipelineTest"])
    Cat5(["5. ResultSetTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4
    Root --> Cat5

    Cat1 --> TC01("TC-01: createExecutionPlan")
    Cat1 --> TC02("TC-02: validateExecutionPlan")

    Cat2 --> TC03("TC-03: initializeContext")
    Cat2 --> TC04("TC-04: setAndGetVariable")

    Cat3 --> TC05("TC-05: executeQuery")
    Cat3 --> TC06("TC-06: executeUpdate")

    Cat4 --> TC07("TC-07: ScanOperator")
    Cat4 --> TC08("TC-08: FilterOperator")
    Cat4 --> TC09("TC-09: JoinOperator")
    Cat4 --> TC10("TC-10: AggregateOperator")
    Cat4 --> TC11("TC-11: SortOperator")

    Cat5 --> TC12("TC-12: fetchNextRow")
    Cat5 --> TC13("TC-13: getColumnValue")
    Cat5 --> TC14("TC-14: closeResultSet")
```

---

### 3.5. Storage Engine Module (`StorageEngine`)

```mermaid
flowchart LR
    Root(("Storage Engine Unit Tests"))

    Cat1(["1. FileManagerTest"])
    Cat2(["2. PageManagerTest"])
    Cat3(["3. BufferPoolManagerTest"])
    Cat4(["4. PageReplacerTest"])
    Cat5(["5. LockManagerTest"])
    Cat6(["6. TransactionManagerTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3
    Root --> Cat4
    Root --> Cat5
    Root --> Cat6

    Cat1 --> TC01("TC-01: createDataFile")
    Cat1 --> TC02("TC-02: readAndWriteBlock")

    Cat2 --> TC03("TC-03: allocatePage")
    Cat2 --> TC04("TC-04: deallocatePage")

    Cat3 --> TC05("TC-05: fetchPageFromBuffer")
    Cat3 --> TC06("TC-06: flushDirtyPage")
    Cat3 --> TC07("TC-07: unpinFrame")

    Cat4 --> TC08("TC-08: ClockPageReplacer_selectVictim")

    Cat5 --> TC09("TC-09: acquireSharedLock")
    Cat5 --> TC10("TC-10: acquireExclusiveLock")
    Cat5 --> TC11("TC-11: detectDeadlock")

    Cat6 --> TC12("TC-12: beginTransaction")
    Cat6 --> TC13("TC-13: commitTransaction")
    Cat6 --> TC14("TC-14: rollbackTransaction")
```

---

### 3.6. Durability & Recovery Data Module (`DurabilityData`)

```mermaid
flowchart LR
    Root(("Durability & Recovery Unit Tests"))

    Cat1(["1. WALManagerTest"])
    Cat2(["2. CheckpointManagerTest"])
    Cat3(["3. RecoveryManagerTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3

    Cat1 --> TC01("TC-01: appendLogRecord")
    Cat1 --> TC02("TC-02: flushLogBuffer")
    Cat1 --> TC03("TC-03: getLSN")

    Cat2 --> TC04("TC-04: createCheckpoint")
    Cat2 --> TC05("TC-05: flushDirtyPagesOnCheckpoint")

    Cat3 --> TC06("TC-06: performAnalysisPass")
    Cat3 --> TC07("TC-07: performRedoPass")
    Cat3 --> TC08("TC-08: performUndoPass")
```

---

### 3.7. Security & Permissions Module (`SecurityPermission`)

```mermaid
flowchart LR
    Root(("Security & Permissions Unit Tests"))

    Cat1(["1. AuthenticationManagerTest"])
    Cat2(["2. AuthorizationManagerTest"])
    Cat3(["3. AuditManagerTest"])

    Root --> Cat1
    Root --> Cat2
    Root --> Cat3

    Cat1 --> TC01("TC-01: authenticateUserSuccess")
    Cat1 --> TC02("TC-02: authenticateUserFailure")

    Cat2 --> TC03("TC-03: checkTableReadPermission")
    Cat2 --> TC04("TC-04: checkTableWritePermission")

    Cat3 --> TC05("TC-05: logSecurityEvent")
    Cat3 --> TC06("TC-06: exportAuditTrail")
```

---

### 3.8. Performance & Scalability Module (`PerformanceScalability`)

```mermaid
flowchart LR
    Root(("Performance & Scalability Unit Tests"))

    Cat1(["1. CacheManagerTest"])
    Cat2(["2. MemoryManagerTest"])

    Root --> Cat1
    Root --> Cat2

    Cat1 --> TC01("TC-01: putInCache")
    Cat1 --> TC02("TC-02: getFromCache")
    Cat1 --> TC03("TC-03: invalidateCacheKey")

    Cat2 --> TC04("TC-04: allocateMemoryPool")
    Cat2 --> TC05("TC-05: releaseMemoryPool")
```

---

### 3.9. Monitoring Module (`Monitoring`)

```mermaid
flowchart LR
    Root(("Monitoring Unit Tests"))

    Cat1(["1. MetricsCollectorTest"])

    Root --> Cat1

    Cat1 --> TC01("TC-01: recordQueryLatency")
    Cat1 --> TC02("TC-02: recordBufferPoolHitRatio")
    Cat1 --> TC03("TC-03: getSystemMetrics")
```

---

### 3.10. Automation Module (`Automation`)

```mermaid
flowchart LR
    Root(("Automation Unit Tests"))

    Cat1(["1. AutoVacuumTest"])

    Root --> Cat1

    Cat1 --> TC01("TC-01: scanDeadTuples")
    Cat1 --> TC02("TC-02: compactTablePages")
    Cat1 --> TC03("TC-03: updateCatalogStatistics")
```

# Metadata Unit Test Scenarios Mindmap

This mindmap represents the structural taxonomy of the Metadata unit test scenarios, showing the coverage across `CatalogManager`, `Database`, `Schema`, `Table`, `Column`, `Index`, and `Constraint` classes.

```mermaid
flowchart LR
    Root(("Metadata Unit Tests"))
    
    %% Categories
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
    
    %% CatalogManagerTest
    Cat1 --> TC01("TC-01: createDatabase")
    Cat1 --> TC02("TC-02: dropDatabase")
    Cat1 --> TC03("TC-03: listDatabases")
    Cat1 --> TC04("TC-04: clear")
    
    %% DatabaseTest
    Cat2 --> TC05("TC-05: createSchema")
    Cat2 --> TC06("TC-06: setStatus_And_rename")
    
    %% SchemaTest
    Cat3 --> TC07("TC-07: createTable")
    Cat3 --> TC08("TC-08: rename_And_listTables")
    
    %% TableTest
    Cat4 --> TC09("TC-09: addColumn")
    
    %% ColumnTest
    Cat5 --> TC10("TC-10: changeDataType_And_setDefaultValue")
    
    %% IndexTest
    Cat6 --> TC11("TC-11: addIndex")
    
    %% ConstraintTest
    Cat7 --> TC12("TC-12: validated")
    Cat7 --> TC13("TC-13: validateReference")
    Cat7 --> TC14("TC-14: evaluate")

    %% Node styling for a premium mindmap look
    style Root fill:#1A365D,stroke:#2B6CB0,stroke-width:3px,color:#FFFFFF,font-weight:bold
    
    style Cat1 fill:#2C3E50,stroke:#34495E,stroke-width:2px,color:#FFFFFF,font-weight:bold
    style Cat2 fill:#2C3E50,stroke:#34495E,stroke-width:2px,color:#FFFFFF,font-weight:bold
    style Cat3 fill:#2C3E50,stroke:#34495E,stroke-width:2px,color:#FFFFFF,font-weight:bold
    style Cat4 fill:#2C3E50,stroke:#34495E,stroke-width:2px,color:#FFFFFF,font-weight:bold
    style Cat5 fill:#2C3E50,stroke:#34495E,stroke-width:2px,color:#FFFFFF,font-weight:bold
    style Cat6 fill:#2C3E50,stroke:#34495E,stroke-width:2px,color:#FFFFFF,font-weight:bold
    style Cat7 fill:#2C3E50,stroke:#34495E,stroke-width:2px,color:#FFFFFF,font-weight:bold

    style TC01 fill:#EBF8FF,stroke:#bee3f8,stroke-width:1px,color:#2b6cb0
    style TC02 fill:#EBF8FF,stroke:#bee3f8,stroke-width:1px,color:#2b6cb0
    style TC03 fill:#EBF8FF,stroke:#bee3f8,stroke-width:1px,color:#2b6cb0
    style TC04 fill:#EBF8FF,stroke:#bee3f8,stroke-width:1px,color:#2b6cb0

    style TC05 fill:#F0FFF4,stroke:#c6f6d5,stroke-width:1px,color:#22543d
    style TC06 fill:#F0FFF4,stroke:#c6f6d5,stroke-width:1px,color:#22543d

    style TC07 fill:#EDF2F7,stroke:#e2e8f0,stroke-width:1px,color:#4a5568
    style TC08 fill:#EDF2F7,stroke:#e2e8f0,stroke-width:1px,color:#4a5568

    style TC09 fill:#FFFDF5,stroke:#fefcbf,stroke-width:1px,color:#744210

    style TC10 fill:#FFF5F5,stroke:#fed7d7,stroke-width:1px,color:#9b2c2c

    style TC11 fill:#F5F5FF,stroke:#e1e1ff,stroke-width:1px,color:#3b3b98

    style TC12 fill:#E6FFFA,stroke:#b2f5ea,stroke-width:1px,color:#234e52
    style TC13 fill:#E6FFFA,stroke:#b2f5ea,stroke-width:1px,color:#234e52
    style TC14 fill:#E6FFFA,stroke:#b2f5ea,stroke-width:1px,color:#234e52
```

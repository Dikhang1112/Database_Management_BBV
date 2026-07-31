# API Implementation Mindmaps

Tài liệu tổng hợp sơ đồ tư duy (Mindmap) ánh xạ thiết kế API cho các Subsystem thuộc hệ thống DBMS.

---

## 1. Metadata Subsystem APIs

Chi tiết tài liệu: [Metadata.md](Metadata.md)

```mermaid
flowchart LR
    Root(("Metadata Subsystem APIs"))

    %% =====================================================
    %% Subsystem Categories
    %% =====================================================
    CatFacade(["1. MetadataModule (Facade)"])
    CatCatalog(["2. CatalogManager (Catalog)"])
    CatDatabase(["3. Database Management"])
    CatSchema(["4. Schema Management"])
    CatTable(["5. Table Management"])
    CatColumn(["6. Column Management"])
    CatIndex(["7. Index Management"])

    Root --> CatFacade
    Root --> CatCatalog
    Root --> CatDatabase
    Root --> CatSchema
    Root --> CatTable
    Root --> CatColumn
    Root --> CatIndex

    %% =====================================================
    %% 1. MetadataModule Facade APIs
    %% =====================================================
    CatFacade --> F1("getInstance() ➔ GET /api/v1/metadata/get-instance")
    CatFacade --> F2("getCatalogManager() ➔ GET /api/v1/metadata/catalog-manager")
    CatFacade --> F3("getDatabase() ➔ GET /api/v1/metadata/databases/{dbName}")
    CatFacade --> F4("getTable() ➔ GET /api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}")
    CatFacade --> F5("executeDDL() ➔ POST /api/v1/metadata/ddl/execute")
    CatFacade --> F6("containsTable() ➔ GET /api/v1/metadata/tables/check")
    CatFacade --> F7("containsColumn() ➔ GET /api/v1/metadata/columns/check")

    %% =====================================================
    %% 2. CatalogManager APIs
    %% =====================================================
    CatCatalog --> C1("listDatabases() ➔ GET /api/v1/metadata/databases")
    CatCatalog --> C2("clearCatalog() ➔ DELETE /api/v1/metadata/catalog/clear")

    %% =====================================================
    %% 3. Database Operations
    %% =====================================================
    CatDatabase --> D1("createDatabase() ➔ POST /api/v1/metadata/databases")
    CatDatabase --> D2("dropDatabase() ➔ DELETE /api/v1/metadata/databases/{dbName}")

    %% =====================================================
    %% 4. Schema Operations
    %% =====================================================
    CatSchema --> S1("listSchemas() ➔ GET /api/v1/metadata/schemas/{dbName}")
    CatSchema --> S2("createSchema() ➔ POST /api/v1/metadata/schemas")
    CatSchema --> S3("dropSchema() ➔ DELETE /api/v1/metadata/schemas/{dbName}/{schemaName}")

    %% =====================================================
    %% 5. Table Operations
    %% =====================================================
    CatTable --> T1("listTables() ➔ GET /api/v1/metadata/tables/{dbName}/{schemaName}")
    CatTable --> T2("createTable() ➔ POST /api/v1/metadata/tables")
    CatTable --> T3("dropTable() ➔ DELETE /api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}")

    %% =====================================================
    %% 6. Column Operations
    %% =====================================================
    CatColumn --> CL1("listColumns() ➔ GET /api/v1/metadata/columns/{dbName}/{schemaName}/{tableName}")
    CatColumn --> CL2("addColumn() ➔ POST /api/v1/metadata/columns")
    CatColumn --> CL3("removeColumn() ➔ DELETE /api/v1/metadata/columns/{dbName}/{schemaName}/{tableName}/{columnName}")

    %% =====================================================
    %% 7. Index Operations
    %% =====================================================
    CatIndex --> I1("listIndexes() ➔ GET /api/v1/metadata/indexes/{dbName}/{schemaName}/{tableName}")
    CatIndex --> I2("addIndex() ➔ POST /api/v1/metadata/indexes")
    CatIndex --> I3("removeIndex() ➔ DELETE /api/v1/metadata/indexes/{dbName}/{schemaName}/{tableName}/{indexName}")
```

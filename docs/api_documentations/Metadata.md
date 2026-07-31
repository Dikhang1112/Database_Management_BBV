# Metadata Subsystem - REST API Documentation & Design Pattern Mapping

This document provides a comprehensive specification of the core REST APIs for the **Metadata Subsystem** of the DBMS engine. All endpoints are mapped to underlying **Design Patterns** (Facade, Singleton, Command, Memento, Observer, Strategy) and follow the standardized `ApiResponse` wrapper.

---

## 1. Standard Response Format

All REST endpoints return a unified JSON response envelope:

```json
{
  "status": 200,
  "message": "Human readable response description",
  "data": { ... },
  "timestamp": "31-07-2026 15:00:00"
}
```

* **`status`**: HTTP Status Code (`200 OK`, `201 Created`, `400 Bad Request`, `404 Not Found`, `405 Method Not Allowed`, `500 Internal Error`).
* **`message`**: Result message or exception detail.
* **`data`**: Payload data object (returns `null` on errors or DDL/deletion operations).
* **`timestamp`**: Datetime string formatted as `dd-MM-yyyy HH:mm:ss`.

---

## 2. REST API List Grouped by Management Domains & Design Patterns

### 2.1. Metadata Subsystem (Facade & Singleton Pattern)

| # | Method | REST API URL | Parameters | Status Code | Design Pattern / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **1** | `GET` | `/api/v1/metadata/get-instance` | *None* | `200 OK`, `500` | **Singleton Pattern:** Get / Initialize `MetadataModule` Singleton Instance (DCL) |
| **2** | `GET` | `/api/v1/metadata/catalog-manager` | *None* | `200 OK` | **Facade Pattern:** Query the root `CatalogManager` object via Facade |
| **3** | `POST` | `/api/v1/metadata/ddl/execute` | **Body:** DDLRequestDTO | `201 Created`, `400` | **Command Pattern:** Encapsulate and execute DDL Command Objects |

---

### 2.2. Catalog Management

| # | Method | REST API URL | Parameters | Status Code | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **4** | `POST` | `/api/v1/metadata/catalog/databases` | **Body:** `databaseName` | `201 Created`, `400` | Create a new Database in CatalogManager |
| **5** | `DELETE` | `/api/v1/metadata/catalog/databases/{databaseName}` | **Path:** `databaseName` | `200 OK`, `404`, `405` | Drop a Database from CatalogManager |
| **6** | `GET` | `/api/v1/metadata/catalog/databases` | *None* | `200 OK` | List all Databases contained in CatalogManager |

---

### 2.3. Database Management

| # | Method | REST API URL | Parameters | Status Code | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **7** | `POST` | `/api/v1/metadata/databases/{dbName}/schemas` | **Path:** `dbName`, **Body:** `schemaName` | `201 Created`, `400`, `405` | Create a new Schema in Database |
| **8** | `DELETE` | `/api/v1/metadata/databases/{dbName}/schemas/{schemaName}` | **Path:** `dbName`, `schemaName` | `200 OK`, `404` | Drop a Schema from Database |
| **9** | `GET` | `/api/v1/metadata/databases/{dbName}/schemas` | **Path:** `dbName` | `200 OK`, `404` | List all Schemas contained in Database |

---

### 2.4. Schema Management

| # | Method | REST API URL | Parameters | Status Code | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **10** | `POST` | `/api/v1/metadata/schemas/{dbName}/{schemaName}/tables` | **Path:** `dbName`, `schemaName`, **Body:** `tableName` | `201 Created`, `400`, `405` | Create a new Table in Schema |
| **11** | `DELETE` | `/api/v1/metadata/schemas/{dbName}/{schemaName}/tables/{tableName}` | **Path:** `dbName`, `schemaName`, `tableName` | `200 OK`, `404` | Drop a Table from Schema |
| **12** | `GET` | `/api/v1/metadata/schemas/{dbName}/{schemaName}/tables` | **Path:** `dbName`, `schemaName` | `200 OK`, `404` | List all Tables contained in Schema |

---

### 2.5. Table Management (Memento & Observer Pattern)

| # | Method | REST API URL | Parameters | Status Code | Design Pattern / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **13** | `POST` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/memento/snapshot` | **Path:** `dbName`, `schemaName`, `tableName` | `201 Created`, `404` | **Memento Pattern:** Create Memento Snapshot saving Column states |
| **14** | `POST` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/memento/restore` | **Path:** `dbName`, `schemaName`, `tableName`, **Body:** Memento | `201 Created`, `400` | **Memento Pattern:** Restore Column states from TableMemento object |
| **15** | `POST` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/columns` | **Path:** `dbName`, `schemaName`, `tableName`, **Body:** Column | `201 Created`, `400` | **Observer Pattern:** Add Column & publish COLUMN_ADDED event |
| **16** | `DELETE` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/columns/{columnName}` | **Path:** `dbName`, `schemaName`, `tableName`, `columnName` | `200 OK`, `404` | **Observer Pattern:** Remove Column & publish COLUMN_REMOVED event |

---

### 2.6. Column Management

| # | Method | REST API URL | Parameters | Status Code | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **17** | `PUT` | `/api/v1/metadata/columns/{dbName}/{schemaName}/{tableName}/{columnName}/rename` | **Path:** `dbName`, `schemaName`, `tableName`, `columnName`, **Body:** `newName` | `200 OK`, `400` | Rename a Column (`rename`) |
| **18** | `PUT` | `/api/v1/metadata/columns/{dbName}/{schemaName}/{tableName}/{columnName}/data-type` | **Path:** `dbName`, `schemaName`, `tableName`, `columnName`, **Body:** `newType` | `200 OK`, `400` | Change Column Data Type (`changeDataType`) |

---

### 2.7. Constraint Management

| # | Method | REST API URL | Parameters | Status Code | Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **19** | `PUT` | `/api/v1/metadata/constraints/{dbName}/{schemaName}/{tableName}/{constraintName}/enable` | **Path:** `dbName`, `schemaName`, `tableName`, `constraintName` | `200 OK`, `404` | Re-enable a disabled Constraint |
| **20** | `PUT` | `/api/v1/metadata/constraints/{dbName}/{schemaName}/{tableName}/{constraintName}/disable` | **Path:** `dbName`, `schemaName`, `tableName`, `constraintName` | `200 OK`, `404` | Disable a Constraint |

---

### 2.8. Index Management (Strategy Pattern)

| # | Method | REST API URL | Parameters | Status Code | Design Pattern / Description |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **21** | `POST` | `/api/v1/metadata/indexes/{dbName}/{schemaName}/{tableName}/{indexName}/rebuild` | **Path:** `dbName`, `schemaName`, `tableName`, `indexName` | `201 Created`, `404` | **Strategy Pattern:** Rebuild Index using `IndexRebuildStrategy` |
| **22** | `PUT` | `/api/v1/metadata/indexes/{dbName}/{schemaName}/{tableName}/{indexName}/disable` | **Path:** `dbName`, `schemaName`, `tableName`, `indexName` | `200 OK`, `404` | Disable an Index |

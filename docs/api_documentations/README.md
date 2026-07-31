# Metadata Module - REST API Documentation

Tài liệu danh sách chi tiết các REST API thuộc **Metadata Subsystem** của hệ thống DBMS, tuân thủ định dạng phản hồi chuẩn `ApiResponse`.

---

## 1. Cấu Trúc Phản Hồi Chuẩn (Standard Response Format)

Tất cả các API bên dưới đều trả về cấu trúc JSON thống nhất:

```json
{
  "status": 200,
  "message": "Thông điệp mô tả kết quả xử lý",
  "data": { ... },
  "timestamp": "31-07-2026 10:08:00"
}
```

* **`status`**: Mã HTTP Status Code (`200 OK`, `201 Created`, `400 Bad Request`, `403 Forbidden`, `404 Not Found`, `405 Method Not Allowed`, `500 Internal Error`).
* **`message`**: Thông điệp thông báo kết quả hoặc nội dung chi tiết ngoại lệ lỗi.
* **`data`**: Dữ liệu Payload (trả về `null` khi có lỗi hoặc khi gọi các thao tác khởi tạo/xóa/thực thi DDL).
* **`timestamp`**: Định dạng ngày giờ `dd-MM-yyyy HH:mm:ss`.

---

## 2. Bảng Danh Sách REST APIs theo Cấu Trúc Lớp Domain

### 2.1. Facade & Singleton APIs (`MetadataModule`)

| STT | Method | REST API URL (kèm Parameter) | Parameters | Status Code | Error (Ngoại lệ & Mã lỗi) | Mô Tả (Description) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **1** | `GET` | `/api/v1/metadata/get-instance` | *Không có* | `200 OK`, `500` | `500 Internal Error` (Khởi tạo Singleton thất bại) | Lấy / Khởi tạo MetadataModule Singleton Instance (Thread-safe DCL) |
| **2** | `GET` | `/api/v1/metadata/catalog-manager` | *Không có* | `200 OK`, `500` | `500 Internal Error` (CatalogManager chưa khởi tạo) | Lấy thông tin đối tượng CatalogManager gốc (trả về `totalDatabases`) |
| **3** | `GET` | `/api/v1/metadata/databases/{dbName}` | **Path:** `dbName` | `200 OK`, `404` | `404 Not Found` (Database not found) | Lấy thông tin chi tiết một Database theo tên |
| **4** | `GET` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}` | **Path:** `dbName`, `schemaName`, `tableName` | `200 OK`, `404` | `404 Not Found` (Table/Schema/Database not found) | Lấy thông tin Table phân cấp theo Database, Schema và Table Name |
| **5** | `POST` | `/api/v1/metadata/ddl/execute` | **Body:** `commandType`, `databaseName`, ... | `200 OK`, `400` | `400 Bad Request` (Invalid/Null DDL command) | Thực thi câu lệnh DDL Command (Tạo / Xóa Database, Schema, Table) |
| **6** | `GET` | `/api/v1/metadata/tables/check?tableName={tableName}` | **Query:** `tableName` | `200 OK`, `400` | `400 Bad Request` (Invalid identifier format) | Kiểm tra sự tồn tại của Table trong toàn bộ Catalog Metadata (`containsTable`) |
| **7** | `GET` | `/api/v1/metadata/columns/check?tableName={tableName}&columnName={columnName}` | **Query:** `tableName`, `columnName` | `200 OK`, `400` | `400 Bad Request` (Invalid identifier format) | Kiểm tra sự tồn tại của Cột thuộc Bảng chỉ định (`containsColumn`) |

---

### 2.2. Catalog Operations (`CatalogManager`)

| STT | Method | REST API URL (kèm Parameter) | Parameters | Status Code | Error (Ngoại lệ & Mã lỗi) | Mô Tả (Description) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **8** | `POST` | `/api/v1/metadata/catalog/databases` | **Body:** `databaseName` | `201`, `400`, `403` | `400 Bad Request` (Database exists), `403 Forbidden` (Permission denied) | Tạo một Database mới trong Catalog (`createDatabase`) |
| **9** | `DELETE` | `/api/v1/metadata/catalog/databases/{databaseName}` | **Path:** `databaseName` | `200`, `404`, `405` | `404 Not Found` (DB not found), `405 Method Not Allowed` (DB not empty) | Xóa một Database khỏi Catalog (`dropDatabase`) |
| **10** | `GET` | `/api/v1/metadata/catalog/databases/{databaseName}` | **Path:** `databaseName` | `200 OK`, `404` | `404 Not Found` (Database not found) | Tra cứu Database theo tên (`getDatabase`) |
| **11** | `GET` | `/api/v1/metadata/catalog/databases/check?databaseName={databaseName}` | **Query:** `databaseName` | `200 OK`, `400` | `400 Bad Request` (Invalid database identifier) | Kiểm tra sự tồn tại của Database (`containsDatabase`) |
| **12** | `GET` | `/api/v1/metadata/catalog/databases` | *Không có* | `200 OK` | *Không có* | Danh sách tất cả các Database trong Catalog (`listDatabases`) |
| **13** | `DELETE` | `/api/v1/metadata/catalog/clear` | *Không có* | `200 OK` | *Không có* | Xóa sạch toàn bộ dữ liệu lưu trữ trong Catalog (`clear`) |

---

### 2.3. Database Management (`Database`)

| STT | Method | REST API URL (kèm Parameter) | Parameters | Status Code | Error (Ngoại lệ & Mã lỗi) | Mô Tả (Description) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **14** | `POST` | `/api/v1/metadata/databases/{dbName}/schemas` | **Path:** `dbName`, **Body:** `schemaName` | `201`, `400`, `405` | `405 Method Not Allowed` (Database offline), `400 Bad Request` (Schema exists) | Tạo một Schema mới trong Database (`createSchema`) |
| **15** | `DELETE` | `/api/v1/metadata/databases/{dbName}/schemas/{schemaName}` | **Path:** `dbName`, `schemaName` | `200`, `404`, `405` | `404 Not Found` (Schema not found), `405 Method Not Allowed` (DB offline) | Xóa một Schema khỏi Database (`dropSchema`) |
| **16** | `GET` | `/api/v1/metadata/databases/{dbName}/schemas/{schemaName}` | **Path:** `dbName`, `schemaName` | `200 OK`, `404` | `404 Not Found` (Schema or Database not found) | Tra cứu Schema theo tên trong DB (`getSchema`) |
| **17** | `GET` | `/api/v1/metadata/databases/{dbName}/schemas/check?schemaName={schemaName}` | **Path:** `dbName`, **Query:** `schemaName` | `200 OK`, `404` | `404 Not Found` (Database not found) | Kiểm tra sự tồn tại của Schema (`containsSchema`) |
| **18** | `GET` | `/api/v1/metadata/databases/{dbName}/schemas` | **Path:** `dbName` | `200 OK`, `404` | `404 Not Found` (Database not found) | Danh sách tất cả các Schema thuộc Database (`listSchemas`) |
| **19** | `GET` | `/api/v1/metadata/databases/{dbName}/status` | **Path:** `dbName` | `200 OK`, `404` | `404 Not Found` (Database not found) | Truy vấn trạng thái của Database (`getStatus`) |
| **20** | `PUT` | `/api/v1/metadata/databases/{dbName}/status` | **Path:** `dbName`, **Body:** `status` | `200 OK`, `400` | `400 Bad Request` (Invalid DatabaseStatus value) | Cập nhật trạng thái `ONLINE` / `OFFLINE` cho Database (`setStatus`) |

---

### 2.4. Schema Management (`Schema`)

| STT | Method | REST API URL (kèm Parameter) | Parameters | Status Code | Error (Ngoại lệ & Mã lỗi) | Mô Tả (Description) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **21** | `POST` | `/api/v1/metadata/schemas/{dbName}/{schemaName}/tables` | **Path:** `dbName`, `schemaName`, **Body:** `tableName` | `201`, `400`, `405` | `405 Method Not Allowed` (Schema read-only), `400 Bad Request` (Table exists) | Tạo một Table mới trong Schema (`createTable`) |
| **22** | `DELETE` | `/api/v1/metadata/schemas/{dbName}/{schemaName}/tables/{tableName}` | **Path:** `dbName`, `schemaName`, `tableName` | `200`, `404`, `405` | `404 Not Found` (Table not found), `405 Method Not Allowed` (Schema read-only) | Xóa một Table khỏi Schema (`dropTable`) |
| **23** | `GET` | `/api/v1/metadata/schemas/{dbName}/{schemaName}/tables/{tableName}` | **Path:** `dbName`, `schemaName`, `tableName` | `200 OK`, `404` | `404 Not Found` (Table/Schema/DB not found) | Tra cứu thông tin Table theo tên (`getTable`) |
| **24** | `GET` | `/api/v1/metadata/schemas/{dbName}/{schemaName}/tables/check?tableName={tableName}` | **Path:** `dbName`, `schemaName`, **Query:** `tableName` | `200 OK`, `404` | `404 Not Found` (Schema or DB not found) | Kiểm tra sự tồn tại của Table trong Schema (`containsTable`) |
| **25** | `GET` | `/api/v1/metadata/schemas/{dbName}/{schemaName}/tables` | **Path:** `dbName`, `schemaName` | `200 OK`, `404` | `404 Not Found` (Schema or DB not found) | Danh sách tất cả các Table thuộc Schema (`listTables`) |
| **26** | `GET` | `/api/v1/metadata/schemas/{dbName}/{schemaName}/read-only` | **Path:** `dbName`, `schemaName` | `200 OK`, `404` | `404 Not Found` (Schema not found) | Kiểm tra chế độ Read-Only của Schema (`isReadOnly`) |
| **27** | `PUT` | `/api/v1/metadata/schemas/{dbName}/{schemaName}/read-only` | **Path:** `dbName`, `schemaName`, **Body:** `readOnly` | `200 OK`, `400` | `400 Bad Request` (Invalid boolean format) | Bật/tắt chế độ Chỉ đọc (Read-Only) của Schema (`setReadOnly`) |

---

### 2.5. Table Management (`Table`)

| STT | Method | REST API URL (kèm Parameter) | Parameters | Status Code | Error (Ngoại lệ & Mã lỗi) | Mô Tả (Description) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **28** | `POST` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/columns` | **Path:** `dbName`, `schemaName`, `tableName`, **Body:** Column | `201`, `400`, `405` | `405 Method Not Allowed` (Table locked), `400 Bad Request` (Column exists) | Thêm Cột mới vào Bảng (`addColumn`) |
| **29** | `DELETE` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/columns/{columnName}` | **Path:** `dbName`, `schemaName`, `tableName`, `columnName` | `200`, `404`, `405` | `405 Method Not Allowed` (Column referenced by constraint / Table locked) | Xóa Cột khỏi Bảng (`removeColumn`) |
| **30** | `GET` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/columns/{columnName}` | **Path:** `dbName`, `schemaName`, `tableName`, `columnName` | `200 OK`, `404` | `404 Not Found` (Column not found) | Lấy thông tin Cột theo tên (`getColumn`) |
| **31** | `GET` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/columns/check?columnName={columnName}` | **Path:** `dbName`, `schemaName`, `tableName`, **Query:** `columnName` | `200 OK`, `404` | `404 Not Found` (Table not found) | Kiểm tra tồn tại Cột (`containsColumn`) |
| **32** | `GET` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/columns` | **Path:** `dbName`, `schemaName`, `tableName` | `200 OK`, `404` | `404 Not Found` (Table not found) | Danh sách tất cả Cột của Bảng (`listColumns`) |
| **33** | `POST` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/constraints` | **Path:** `dbName`, `schemaName`, `tableName`, **Body:** Constraint | `201`, `400`, `405` | `405 Method Not Allowed` (Table locked), `400 Bad Request` (Invalid constraint) | Thêm Constraint (Ràng buộc) vào Bảng (`addConstraint`) |
| **34** | `DELETE` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/constraints/{constraintName}` | **Path:** `dbName`, `schemaName`, `tableName`, `constraintName` | `200`, `404`, `405` | `404 Not Found` (Constraint not found), `405 Method Not Allowed` (Table locked) | Xóa Constraint khỏi Bảng (`removeConstraint`) |
| **35** | `GET` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/constraints/{constraintName}` | **Path:** `dbName`, `schemaName`, `tableName`, `constraintName` | `200 OK`, `404` | `404 Not Found` (Constraint not found) | Lấy thông tin Constraint (`getConstraint`) |
| **36** | `GET` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/constraints/check?constraintName={constraintName}` | **Path:** `dbName`, `schemaName`, `tableName`, **Query:** `constraintName` | `200 OK`, `404` | `404 Not Found` (Table not found) | Kiểm tra tồn tại Constraint (`containsConstraint`) |
| **37** | `GET` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/constraints` | **Path:** `dbName`, `schemaName`, `tableName` | `200 OK`, `404` | `404 Not Found` (Table not found) | Danh sách tất cả Constraint của Bảng (`listConstraints`) |
| **38** | `POST` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/indexes` | **Path:** `dbName`, `schemaName`, `tableName`, **Body:** Index | `201`, `400`, `405` | `400 Bad Request` (Indexed column not found), `405 Method Not Allowed` (Table locked) | Thêm Index mới vào Bảng (`addIndex`) |
| **39** | `DELETE` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/indexes/{indexName}` | **Path:** `dbName`, `schemaName`, `tableName`, `indexName` | `200`, `404`, `405` | `404 Not Found` (Index not found), `405 Method Not Allowed` (Table locked) | Xóa Index khỏi Bảng (`removeIndex`) |
| **40** | `GET` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/indexes/{indexName}` | **Path:** `dbName`, `schemaName`, `tableName`, `indexName` | `200 OK`, `404` | `404 Not Found` (Index not found) | Lấy thông tin Index (`getIndex`) |
| **41** | `GET` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/indexes` | **Path:** `dbName`, `schemaName`, `tableName` | `200 OK`, `404` | `404 Not Found` (Table not found) | Danh sách tất cả Index của Bảng (`listIndexes`) |
| **42** | `PUT` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/lock` | **Path:** `dbName`, `schemaName`, `tableName`, **Body:** `locked` | `200 OK`, `400` | `400 Bad Request` (Invalid boolean format) | Khóa / Mở khóa Bảng (`setLocked`) |
| **43** | `POST` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/memento/snapshot` | **Path:** `dbName`, `schemaName`, `tableName` | `200 OK`, `404` | `404 Not Found` (Table not found) | Tạo bản chụp Memento trạng thái Bảng (`createMemento`) |
| **44** | `POST` | `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}/memento/restore` | **Path:** `dbName`, `schemaName`, `tableName`, **Body:** Memento Object | `200 OK`, `400` | `400 Bad Request` (Restore null/invalid memento) | Khôi phục trạng thái Bảng từ Memento (`restore`) |

---

### 2.6. Column Operations (`Column`)

| STT | Method | REST API URL (kèm Parameter) | Parameters | Status Code | Error (Ngoại lệ & Mã lỗi) | Mô Tả (Description) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **45** | `PUT` | `/api/v1/metadata/columns/{dbName}/{schemaName}/{tableName}/{columnName}/rename` | **Path:** `dbName`, `schemaName`, `tableName`, `columnName`, **Body:** `newName` | `200 OK`, `400` | `400 Bad Request` (Invalid new identifier format) | Đổi tên Cột (`rename`) |
| **46** | `PUT` | `/api/v1/metadata/columns/{dbName}/{schemaName}/{tableName}/{columnName}/data-type` | **Path:** `dbName`, `schemaName`, `tableName`, `columnName`, **Body:** `newType` | `200 OK`, `400` | `400 Bad Request` (Unsupported data type conversion) | Thay đổi kiểu dữ liệu Cột (`changeDataType`) |
| **47** | `PUT` | `/api/v1/metadata/columns/{dbName}/{schemaName}/{tableName}/{columnName}/nullable` | **Path:** `dbName`, `schemaName`, `tableName`, `columnName`, **Body:** `nullable` | `200 OK`, `400` | `400 Bad Request` (Invalid boolean format) | Thiết lập thuộc tính Nullable cho Cột (`setNullable`) |
| **48** | `PUT` | `/api/v1/metadata/columns/{dbName}/{schemaName}/{tableName}/{columnName}/default-value` | **Path:** `dbName`, `schemaName`, `tableName`, `columnName`, **Body:** `defaultValue` | `200 OK`, `400` | `400 Bad Request` (Invalid default value for column type) | Thiết lập giá trị mặc định cho Cột (`setDefaultValue`) |

---

### 2.7. Constraint & Index Management (`Constraint` / `Index`)

| STT | Method | REST API URL (kèm Parameter) | Parameters | Status Code | Error (Ngoại lệ & Mã lỗi) | Mô Tả (Description) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **49** | `PUT` | `/api/v1/metadata/constraints/{dbName}/{schemaName}/{tableName}/{constraintName}/enable` | **Path:** `dbName`, `schemaName`, `tableName`, `constraintName` | `200 OK`, `404` | `404 Not Found` (Constraint not found) | Kích hoạt Ràng buộc Constraint (`enable`) |
| **50** | `PUT` | `/api/v1/metadata/constraints/{dbName}/{schemaName}/{tableName}/{constraintName}/disable` | **Path:** `dbName`, `schemaName`, `tableName`, `constraintName` | `200 OK`, `404` | `404 Not Found` (Constraint not found) | Tắt Ràng buộc Constraint (`disable`) |
| **51** | `POST` | `/api/v1/metadata/indexes/{dbName}/{schemaName}/{tableName}/{indexName}/rebuild` | **Path:** `dbName`, `schemaName`, `tableName`, `indexName` | `200 OK`, `405` | `405 Method Not Allowed` (Index is corrupted) | Tái cấu trúc lại Index (`rebuild`) |
| **52** | `PUT` | `/api/v1/metadata/indexes/{dbName}/{schemaName}/{tableName}/{indexName}/disable` | **Path:** `dbName`, `schemaName`, `tableName`, `indexName` | `200 OK`, `404` | `404 Not Found` (Index not found) | Vô hiệu hóa Index (`disable`) |

# Metadata Module - REST API Documentation

Tài liệu quy định chi tiết cấu trúc REST API cho **Metadata Subsystem** thuộc hệ thống DBMS. Tất cả phản hồi API tuân thủ chuẩn cấu trúc DTO `ApiResponse` gồm các trường: `status`, `message`, `data`, và `timestamp`.

---

## 1. Cấu Trúc Phản Hồi Chuẩn (Standard Response Format)

Mọi phản hồi REST API từ Metadata Module đều áp dụng cấu trúc JSON chuẩn bên dưới:

```json
{
  "status": 200,
  "message": "Thông điệp phản hồi từ hệ thống",
  "data": { ... },
  "timestamp": "31-07-2026 10:08:00"
}
```

> **Lưu ý:**
> - Đối với các phương thức chỉ dùng để thực thi hoặc khởi tạo như `getInstance()`, `executeDDL()`, trường `data` sẽ trả về `null` và chỉ thông báo kết quả qua trường `message`.
> - Định dạng thời gian `timestamp` luôn là `dd-MM-yyyy HH:mm:ss`.

---

## 2. Chi Tiết Danh Sách REST APIs

### 2.1. Lấy / Khởi tạo Singleton Instance (`getInstance`)
* **HTTP Method:** `GET`
* **URL:** `/api/v1/metadata/get-instance`
* **Mô tả:** Lấy tham chiếu đến `MetadataModule` Singleton Instance (khởi tạo thread-safe bằng Double-Checked Locking nếu chưa tồn tại).
* **Request Header / Query:** Không có.
* **Phản hồi Thành Công (200 OK):**
```json
{
  "status": 200,
  "message": "Thành công (200 OK): Khởi tạo/Lấy MetadataModule Singleton Instance thành công",
  "data": null,
  "timestamp": "31-07-2026 10:08:00"
}
```

---

### 2.2. Lấy Thông Tin Catalog Manager (`getCatalogManager`)
* **HTTP Method:** `GET`
* **URL:** `/api/v1/metadata/catalog-manager`
* **Mô tả:** Truy vấn đối tượng `CatalogManager` gốc giữ toàn bộ danh sách Database trong hệ thống.
* **Phản hồi Thành Công (200 OK):**
```json
{
  "status": 200,
  "message": "Thành công (200 OK): Lấy thông tin CatalogManager thành công",
  "data": {
    "elementName": "CatalogManager",
    "elementType": "CatalogManager",
    "totalDatabases": 2
  },
  "timestamp": "31-07-2026 10:08:00"
}
```

---

### 2.3. Lấy Thông Tin Database theo Tên (`getDatabase`)
* **HTTP Method:** `GET`
* **URL:** `/api/v1/metadata/databases/{dbName}`
* **Tham số Path:** `dbName` (String) - Tên Database cần lấy thông tin.
* **Phản hồi Thành Công (200 OK):**
```json
{
  "status": 200,
  "message": "Thành công (200 OK): Lấy thông tin Database 'sales_db' thành công",
  "data": {
    "databaseName": "sales_db",
    "schemaCount": 1,
    "schemas": ["public"]
  },
  "timestamp": "31-07-2026 10:08:00"
}
```
* **Phản hồi Lỗi (404 Not Found):**
```json
{
  "status": 404,
  "message": "Lỗi (404 Not Found): Database 'unknown_db' không tồn tại trong Catalog",
  "data": null,
  "timestamp": "31-07-2026 10:08:00"
}
```

---

### 2.4. Lấy Thông Tin Bảng theo Phân Cấp (`getTable`)
* **HTTP Method:** `GET`
* **URL:** `/api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}`
* **Tham số Path:**
  * `dbName` (String): Tên Database.
  * `schemaName` (String): Tên Schema.
  * `tableName` (String): Tên Table.
* **Phản hồi Thành Công (200 OK):**
```json
{
  "status": 200,
  "message": "Thành công (200 OK): Lấy thông tin Bảng 'users' thành công",
  "data": {
    "tableName": "users",
    "schemaName": "public",
    "databaseName": "sales_db",
    "columns": [
      {
        "columnName": "id",
        "dataType": "INTEGER",
        "isPrimaryKey": true
      },
      {
        "columnName": "username",
        "dataType": "VARCHAR",
        "isPrimaryKey": false
      }
    ]
  },
  "timestamp": "31-07-2026 10:08:00"
}
```

---

### 2.5. Thực Thi Lệnh DDL (`executeDDL`)
* **HTTP Method:** `POST`
* **URL:** `/api/v1/metadata/ddl/execute`
* **Request Body (JSON):**
```json
{
  "commandType": "CREATE_DATABASE",
  "databaseName": "analytics_db"
}
```
* **Phản hồi Thành Công (200 OK):**
```json
{
  "status": 200,
  "message": "Thành công (200 OK): Thực thi lệnh DDL 'CREATE_DATABASE' thành công",
  "data": null,
  "timestamp": "31-07-2026 10:08:00"
}
```

---

### 2.6. Kiểm Tra Sự Tồn Tại Của Bảng (`containsTable`)
* **HTTP Method:** `GET`
* **URL:** `/api/v1/metadata/tables/check?tableName={tableName}`
* **Tham số Query:** `tableName` (String) - Tên bảng cần tra cứu.
* **Phản hồi Thành Công (200 OK):**
```json
{
  "status": 200,
  "message": "Thành công (200 OK): Kiểm tra tồn tại bảng thành công",
  "data": {
    "tableName": "users",
    "exists": true
  },
  "timestamp": "31-07-2026 10:08:00"
}
```

---

### 2.7. Kiểm Tra Sự Tồn Tại Của Cột (`containsColumn`)
* **HTTP Method:** `GET`
* **URL:** `/api/v1/metadata/columns/check?tableName={tableName}&columnName={columnName}`
* **Tham số Query:**
  * `tableName` (String): Tên bảng.
  * `columnName` (String): Tên cột cần kiểm tra.
* **Phản hồi Thành Công (200 OK):**
```json
{
  "status": 200,
  "message": "Thành công (200 OK): Kiểm tra tồn tại cột thành công",
  "data": {
    "tableName": "users",
    "columnName": "username",
    "exists": true
  },
  "timestamp": "31-07-2026 10:08:00"
}
```

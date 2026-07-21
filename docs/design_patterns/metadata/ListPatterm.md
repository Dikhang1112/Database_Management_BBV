# Danh Sách Các Design Pattern Quan Trọng Nhất Trong Metadata Module (Core & Operational)

Tài liệu này tập trung vào **13 Design Pattern quan trọng nhất** (thuộc Group 1 - Cốt lõi và Group 2 - Tính năng nâng cao) trong module `metadata`, được sắp xếp theo cấp bậc đối tượng từ **Lớn (Root Catalog)** ➔ **Trung Bình (Database, Schema, Table)** ➔ **Nhỏ (Column, Constraint, Index)**.

---

## 🏛️ 1. Cấp Độ CatalogManager & MetadataModule (Root Catalog Level)

### 1.1. Singleton Pattern
* **Pattern**: Singleton Pattern
* **Class/Interface áp dụng**: CatalogManager
* **Method**: `getInstance()`
* **Lí do sử dụng**: Đảm bảo toàn bộ hệ thống DBMS chỉ khởi tạo và truy cập duy nhất một bản thể `CatalogManager` để quản lý tập trung cây cơ sở dữ liệu (`databases` map), tránh việc khởi tạo trùng lặp gây xung đột dữ liệu metadata trong bộ nhớ.

### 1.2. Facade Pattern
* **Pattern**: Facade Pattern
* **Class/Interface áp dụng**: MetadataModule
* **Method**: `getTable(databaseName, schemaName, tableName)`, `executeDDL()`
* **Lí do sử dụng**: Cung cấp một điểm giao tiếp duy nhất (High-level API) đơn giản hóa truy cập Metadata cho các module bên ngoài (Query Processor, Execution Engine), che giấu sự phối hợp phức tạp giữa CatalogManager, Database, Schema và Table.

### 1.3. Composite Pattern
* **Pattern**: Composite Pattern
* **Class/Interface áp dụng**: MetadataElement (được implement bởi CatalogManager, Database, Schema, Table, Column)
* **Method**: `getElementName()`
* **Lí do sử dụng**: Xây dựng cấu trúc phân cấp dạng cây đồng nhất cho Catalog. Cho phép gọi các thao tác chung thống nhất trên cả phần tử nút chứa (Catalog/Database/Schema) lẫn phần tử nút lá (Table/Column).

---

## 🗄️ 2. Cấp Độ Database (Database Level)

### 2.1. State Pattern
* **Pattern**: State Pattern
* **Class/Interface áp dụng**: DatabaseStatus, Database
* **Method**: `setStatus(status)`, `createSchema(schemaName)`
* **Lí do sử dụng**: Thay đổi linh hoạt hành vi của phương thức `createSchema()` dựa trên trạng thái làm việc hiện tại của Database. Nếu trạng thái là `OFFLINE`, hệ thống lập tức chặn thao tác và ném lỗi `IllegalStateException`.

---

## 📂 3. Cấp Độ Schema (Schema Level)

### 3.1. Factory Method Pattern
* **Pattern**: Factory Method Pattern
* **Class/Interface áp dụng**: Schema
* **Method**: `createTable(tableName)`, `createView(viewName, sql)`, `createSequence(sequenceName)`, `createTrigger(triggerName)`, `createProcedure(procedureName)`, `createFunction(functionName)`
* **Lí do sử dụng**: Lớp Schema đóng vai trò Creator cung cấp các phương thức khởi tạo đối tượng con, giúp đóng gói logic khởi tạo và lưu trữ các thành phần trong Schema.

### 3.2. Command Pattern
* **Pattern**: Command Pattern
* **Class/Interface áp dụng**: DDLCommand (CreateTableCommand)
* **Method**: `execute()`, `undo()`
* **Lí do sử dụng**: Đóng gói các câu lệnh thay đổi DDL thành đối tượng riêng biệt hỗ trợ thực thi `execute()` và hoàn tác `undo()`, phục vụ tính năng Rollback Transaction DDL hoặc Schema Migration.

---

## 📋 4. Cấp Độ Table (Table Level)

### 4.1. Prototype Pattern
* **Pattern**: Prototype Pattern
* **Class/Interface áp dụng**: Table
* **Method**: `clone()`
* **Lí do sử dụng**: Thực hiện sao chép sâu (deep-copy) toàn bộ cấu trúc Cột, Ràng buộc và Chỉ mục của bảng hiện tại để hỗ trợ câu lệnh DDL `CREATE TABLE ... LIKE ...` mà không cần parse lại SQL từ đầu.

### 4.2. Memento Pattern
* **Pattern**: Memento Pattern
* **Class/Interface áp dụng**: TableMemento, Table
* **Method**: `createMemento()`, `restore(memento)`
* **Lí do sử dụng**: Lưu giữ bản chụp snapshot cấu trúc của Table trước khi thực hiện câu lệnh `ALTER TABLE`. Nếu quá trình chỉnh sửa gặp sự cố, hệ thống khôi phục lại trạng thái cũ từ TableMemento.

### 4.3. Observer Pattern (Subject)
* **Pattern**: Observer Pattern
* **Class/Interface áp dụng**: Table, MetadataChangeListener
* **Method**: `registerListener(listener)`, `notifyListeners(eventType, targetName)`
* **Lí do sử dụng**: Table tự động gửi thông báo cho tất cả các đối tượng Subscriber đăng ký lắng nghe (View, Trigger) bất cứ khi nào cấu trúc cột thay đổi (`addColumn`, `removeColumn`).

---

## 🏛️ 5. Cấp Độ Column (Column Level)

### 5.1. Builder Pattern
* **Pattern**: Builder Pattern
* **Class/Interface áp dụng**: ColumnBuilder
* **Method**: `setType(dataType)`, `setNullable(nullable)`, `setDefaultValue(value)`, `build()`
* **Lí do sử dụng**: Giúp xây dựng đối tượng Column có nhiều tham số tùy chọn một cách sạch sẽ, rõ ràng thông qua giao diện Fluent API.

---

## 🔒 6. Cấp Độ Constraint (Constraint Level)

### 6.1. Factory Method Pattern (Constraint)
* **Pattern**: Factory Method Pattern
* **Class/Interface áp dụng**: ConstraintFactory
* **Method**: `createConstraint(type, name, args)`
* **Lí do sử dụng**: Đóng gói logic khởi tạo các lớp con cụ thể của Constraint (PrimaryKeyConstraint, ForeignKeyConstraint, UniqueConstraint, CheckConstraint) dựa vào tham số loại truyền vào.

### 6.2. Template Method Pattern
* **Pattern**: Template Method Pattern
* **Class/Interface áp dụng**: Constraint
* **Method**: `validate()`, `preValidate()`, `doValidate()`, `postValidate()`
* **Lí do sử dụng**: Lớp trừu tượng Constraint định nghĩa sẵn khung quy trình kiểm tra cố định trong `validate()`, các lớp con chỉ cần override lại bước kiểm tra chi tiết trong `doValidate()`.

### 6.3. Chain of Responsibility Pattern
* **Pattern**: Chain of Responsibility Pattern
* **Class/Interface áp dụng**: ConstraintValidationChain
* **Method**: `addConstraint(constraint)`, `validateAll()`
* **Lí do sử dụng**: Quản lý chuỗi kiểm tra thẩm định dữ liệu qua từng Constraint (PK ➔ FK ➔ Unique ➔ Check). Nếu một mắt xích thất bại, chuỗi dừng lại và báo lỗi.

---

## ⚡ 7. Cấp Độ Index (Index Level)

### 7.1. Strategy Pattern
* **Pattern**: Strategy Pattern
* **Class/Interface áp dụng**: Index, IndexRebuildStrategy
* **Method**: `setRebuildStrategy(strategy)`, `rebuild()`
* **Lí do sử dụng**: Tách biệt thuật toán rebuild chỉ mục ra khỏi lớp Index, cho phép lựa chọn và thay đổi các chiến lược rebuild (BTree, Hash, Concurrent) linh hoạt tại runtime.

---

## 👁️ 8. Cấp Độ View & Trigger (Schema Observers Level)

### 8.1. Observer Pattern (Subscriber)
* **Pattern**: Observer Pattern
* **Class/Interface áp dụng**: View, Trigger, MetadataChangeListener
* **Method**: `onMetadataChanged(eventType, targetName)`, `compile()`, `refresh()`
* **Lí do sử dụng**: Đóng vai trò các Subscriber đăng ký nhận thông báo sự kiện từ Table, khi có thay đổi cấu trúc bảng thì tự động kích hoạt phương thức `compile()` hoặc `refresh()` để cập nhật định nghĩa.

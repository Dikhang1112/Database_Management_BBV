# Implemented Design Patterns Matrix


## Implemented Design Patterns Matrix in Table

| # | Design Pattern | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Singleton** | `CatalogManager`<br>`MetadataModule` | `getInstance()` | Đảm bảo duy nhất 1 Quản lý Catalog và 1 điểm truy cập Facade chính cho toàn bộ hệ thống DBMS trong RAM. |
| 2 | **Facade** | `MetadataModule` | `getTable(databaseName, schemaName, tableName)`<br>`executeDDL(command)` | Cung cấp giao diện API cấp cao đơn giản hóa việc tương tác phức tạp giữa CatalogManager, Database, Schema và Table. |
| 3 | **Composite** | `MetadataElement` (Interface)<br>`CatalogManager`, `Database`, `Schema`, `Table`, `Column` | `getElementName()` | Xây dựng cấu trúc cây phân cấp quản lý đồng nhất cho các thành phần Metadata trong hệ thống. |
| 4 | **State** | `Database`<br>`DatabaseStatus` | `setStatus(status)`<br>`createSchema(schemaName)` | Quản lý và chặn thao tác thay đổi cấu trúc khi Database ở trạng thái `OFFLINE` hoặc `READ_ONLY`. |
| 5 | **Factory Method** | `Schema`<br>`ConstraintFactory` | `createTable(tableName)`<br>`createPrimaryKey(name)` | Đóng gói logic khởi tạo các đối tượng con (`Table`, `Constraint`) một cách linh hoạt. |
| 6 | **Command** | `DDLCommand` (Interface)<br>`CreateDatabaseCommand`<br>`DropDatabaseCommand`<br>`RenameDatabaseCommand`<br>`CreateSchemaCommand`<br>`DropSchemaCommand`<br>`RenameSchemaCommand`<br>`CreateTableCommand`<br>`DropTableCommand`<br>`RenameTableCommand`<br>`CreateColumnCommand`<br>`DropColumnCommand`<br>`RenameColumnCommand` | `execute()`<br>`undo()` | Đóng gói các thao tác DDL thành các đối tượng lệnh có khả năng Thực thi (`execute`) và Hoàn tác (`undo` / Rollback). |
| 7 | **Prototype** | `Table`<br>`Column` | `clone()` | Nhân bản nhanh cấu trúc bảng hoặc cột hiện tại thành đối tượng độc lập mà không cần khởi tạo lại từ đầu. |
| 8 | **Memento** | `TableMemento`<br>`Table` | `createMemento()`<br>`restore(memento)` | Chụp ảnh trạng thái (Snapshot) danh sách các cột của Table và hỗ trợ khôi phục về trạng thái trước đó. |
| 9 | **Observer** | `MetadataChangeListener` (Interface)<br>`Table` | `registerListener(...)`<br>`notifyListeners(...)`<br>`onMetadataChanged(...)` | `Table` phát thông báo sự kiện thay đổi cấu trúc cho các Observer lắng nghe tự động cập nhật. |
| 10 | **Builder** | `ColumnBuilder` | `setName(...)`<br>`setType(...)`<br>`setNullable(...)`<br>`setDefaultValue(...)`<br>`build()` | Khởi tạo đối tượng `Column` có nhiều tham số tùy chọn bằng giao diện Fluent API. |
| 11 | **Template Method** | `Constraint` (Abstract Class) | `validate()` | Định nghĩa thuật toán khung kiểm tra trạng thái `enabled` trước khi tiến hành thẩm định chi tiết. |
| 12 | **Chain of Responsibility** | `ConstraintValidationChain` | `addConstraint(...)`<br>`removeConstraint(...)`<br>`validateAll()` | Quản lý chuỗi thẩm định ràng buộc dữ liệu nối tiếp (Fail-Fast: PK ➔ FK ➔ Check). |
| 13 | **Strategy** | `IndexRebuildStrategy` (Interface)<br>`Index` | `setRebuildStrategy(strategy)`<br>`rebuild()` | Cho phép gán và thực thi linh hoạt chiến lược rebuild thuật toán cho đối tượng `Index`. |

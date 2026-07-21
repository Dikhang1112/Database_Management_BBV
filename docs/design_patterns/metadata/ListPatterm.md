# Danh Sách 23 Design Pattern Trong Metadata Module

Tài liệu này chi tiết hóa cách áp dụng **23 GoF Design Patterns** thuần túy trong nội bộ module `metadata` (bao gồm các class và interface: [MetadataModule](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/MetadataModule.java), [CatalogManager](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CatalogManager.java), [Database](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Database.java), [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java), [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java), [Column](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Column.java), [Constraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Constraint.java), [Index](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Index.java), [View](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/View.java), [Sequence](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Sequence.java), [Trigger](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Trigger.java), [StoredProcedure](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/StoredProcedure.java), [Function](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Function.java), [DataType](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/enums/DataType.java)).

---

## 🟢 I. Creational Design Patterns (Mẫu Khởi Tạo - 5 Patterns)

### 1. Singleton Pattern
* **Class / Interface nội bộ**: [CatalogManager](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CatalogManager.java), [MetadataModule](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/MetadataModule.java)
* **Bài toán nội bộ Metadata**: Quản lý truy cập tập trung vào duy nhất 1 instance `CatalogManager` duy nhất trong module `metadata` để duy trì cây cơ sở dữ liệu (`Map<String, Database> databases`).
* **Chi tiết triển khai**: [MetadataModule](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/MetadataModule.java) lấy instance duy nhất thông qua `CatalogManager.getInstance()`.

### 2. Factory Method Pattern
* **Class / Interface nội bộ**: [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java), `ConstraintFactory`
* **Bài toán nội bộ Metadata**: Lớp [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java) đóng vai trò Creator với các phương thức khởi tạo đối tượng con (`createTable`, `createView`, `createSequence`, `createTrigger`, `createProcedure`, `createFunction`) và khởi tạo các loại [Constraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Constraint.java) ([PrimaryKeyConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/PrimaryKeyConstraint.java), [ForeignKeyConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/ForeignKeyConstraint.java), [UniqueConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/UniqueConstraint.java), [CheckConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CheckConstraint.java)).
* **Chi tiết triển khai**: `Schema.createTable(tableName)` đóng gói việc khởi tạo `new Table(tableName)` và lưu vào `tables` map của Schema.

### 3. Abstract Factory Pattern
* **Class / Interface nội bộ**: `MetadataObjectFactory` (interface nội bộ module), `StandardMetadataFactory`
* **Bài toán nội bộ Metadata**: Cung cấp giao diện tạo các đối tượng Metadata tương thích theo nhóm trong module (tạo bộ tương thích gồm Table, Column, Index, Constraint).
* **Chi tiết triển khai**: `MetadataObjectFactory` định nghĩa `createTable()`, `createColumn()`, `createIndex()`.

### 4. Builder Pattern
* **Class / Interface nội bộ**: `ColumnBuilder`, `TableBuilder`
* **Bài toán nội bộ Metadata**: Khởi tạo cấu trúc [Column](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Column.java) hoặc [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java) phức tạp với nhiều tham số nội bộ (`columnName`, `dataType`, `nullable`, `defaultValue`, `autoIncrement`).
* **Chi tiết triển khai**: `new ColumnBuilder("id").setType(DataType.INT).setNullable(false).build()`.

### 5. Prototype Pattern
* **Class / Interface nội bộ**: [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java), [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java)
* **Bài toán nội bộ Metadata**: Nhân bản (clone) đối tượng [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java) hoặc [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java) trong bộ nhớ để tạo bản sao cấu trúc dữ liệu mà không cần khởi tạo lại từ đầu.
* **Chi tiết triển khai**: [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java) implement phương thức `clone()` thực hiện deep-copy các đối tượng [Column](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Column.java), [Index](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Index.java), [Constraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Constraint.java).

---

## 🔵 II. Structural Design Patterns (Mẫu Cấu Trúc - 7 Patterns)

### 6. Composite Pattern
* **Class / Interface nội bộ**: Interface `MetadataElement`, [CatalogManager](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CatalogManager.java), [Database](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Database.java), [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java), [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java), [Column](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Column.java)
* **Bài toán nội bộ Metadata**: Tổ chức phân cấp quản lý cây Metadata (`CatalogManager` ➔ `Database` ➔ `Schema` ➔ `Table` ➔ `Column`). Cho phép gọi các thao tác chung (như `getMetadataName()`, `accept()`) thống nhất trên cả Composite node (Database/Schema) và Leaf node (Table/Column).
* **Chi tiết triển khai**: Tất cả class thuộc cây Metadata đều implement interface `MetadataElement`.

### 7. Decorator Pattern
* **Class / Interface nội bộ**: `TableDecorator`, `ReadOnlyTableDecorator`
* **Bài toán nội bộ Metadata**: Bọc đối tượng [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java) gốc bằng các decorator nội bộ để kiểm soát và bổ sung tính năng (ví dụ: chặn chỉnh sửa với `ReadOnlyTableDecorator`).
* **Chi tiết triển khai**: `ReadOnlyTableDecorator` ghi đè `addColumn()` và throw `IllegalStateException("Table is read-only")`.

### 8. Adapter Pattern
* **Class / Interface nội bộ**: `MetadataDefinitionAdapter`, [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java), [Column](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Column.java)
* **Bài toán nội bộ Metadata**: Chuyển đổi định dạng dữ liệu cấu hình chuỗi (String/JSON definition) trong nội bộ metadata thành cấu trúc đối tượng `Table` và `Column`.
* **Chi tiết triển khai**: `MetadataDefinitionAdapter` nhận chuỗi cấu hình và gọi các API thích hợp trên `Table`.

### 9. Facade Pattern
* **Class / Interface nội bộ**: [MetadataModule](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/MetadataModule.java)
* **Bài toán nội bộ Metadata**: [MetadataModule](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/MetadataModule.java) đóng vai trò là điểm giao tiếp đại diện duy nhất (Facade) cung cấp các phương thức làm việc gọn nhẹ (`getDatabase()`, `getTable()`, `createTable()`) ẩn đi sự phối hợp giữa [CatalogManager](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CatalogManager.java), [Database](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Database.java), và [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java).

### 10. Proxy Pattern
* **Class / Interface nội bộ**: `ProtectedSchemaProxy`, [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java)
* **Bài toán nội bộ Metadata**: Kiểm soát quyền tác động lên các Schema bảo mật trong nội bộ Metadata (ví dụ: chặn tạo/xóa bảng trong `secure_schema` hoặc schema ở chế độ `readOnly`).
* **Chi tiết triển khai**: `ProtectedSchemaProxy` kiểm tra cờ `readOnly` hoặc tên schema trước khi ủy quyền cho [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java) thực sự.

### 11. Flyweight Pattern
* **Class / Interface áp dụng**: [DataType](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/enums/DataType.java), `DataTypeFlyweightFactory`
* **Bài toán nội bộ Metadata**: Tái sử dụng các giá trị kiểu dữ liệu trong enum [DataType](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/enums/DataType.java) giữa tất cả các đối tượng [Column](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Column.java) thuộc các bảng khác nhau để tối ưu hóa bộ nhớ.

### 12. Bridge Pattern
* **Class / Interface nội bộ**: [Index](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Index.java) (Abstraction) & `IndexOperationImpl` (Implementor: `BTreeIndexOperation`, `HashIndexOperation`)
* **Bài toán nội bộ Metadata**: Tách rời định nghĩa đối tượng [Index](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Index.java) khỏi thuật toán thao tác chỉ mục nội bộ ([IndexType](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/enums/IndexType.java)).
* **Chi tiết triển khai**: [Index](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Index.java) gọi phương thức `rebuild()` thông qua interface `IndexOperationImpl`.

---

## 🟡 III. Behavioral Design Patterns (Mẫu Hành Vi - 11 Patterns)

### 13. Chain of Responsibility Pattern
* **Class / Interface nội bộ**: [Constraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Constraint.java) và các lớp con ([PrimaryKeyConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/PrimaryKeyConstraint.java), [ForeignKeyConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/ForeignKeyConstraint.java), [UniqueConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/UniqueConstraint.java), [CheckConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CheckConstraint.java))
* **Bài toán nội bộ Metadata**: [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java) chuyển tiếp yêu cầu kiểm tra danh sách `constraints` theo chuỗi nối tiếp nhau thông qua hàm `validate()`.

### 14. Command Pattern
* **Class / Interface nội bộ**: `MetadataOperationCommand` (`CreateTableCommand`, `DropTableCommand`, `AddColumnCommand`)
* **Bài toán nội bộ Metadata**: Đóng gói các thao tác thay đổi Metadata trong [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java) hoặc [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java) thành từng đối tượng lệnh có phương thức `execute()` và `undo()`.

### 15. Interpreter Pattern
* **Class / Interface nội bộ**: [CheckConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CheckConstraint.java) (`ConstraintExpressionInterpreter`)
* **Bài toán nội bộ Metadata**: Phân tích biểu thức điều kiện của [CheckConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CheckConstraint.java) trong nội bộ hàm `evaluate()`.

### 16. Iterator Pattern
* **Class / Interface nội bộ**: `MetadataTreeIterator`
* **Bài toán nội bộ Metadata**: Cung cấp bộ duyệt tuần tự qua cây cấu trúc Metadata ([CatalogManager](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CatalogManager.java) ➔ [Database](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Database.java) ➔ [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java) ➔ [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java)) mà không làm lộ các cấu trúc dữ liệu `Map` / `List` nội bộ.

### 17. Mediator Pattern
* **Class / Interface nội bộ**: `MetadataMediator`
* **Bài toán nội bộ Metadata**: Làm trung gian điều phối giao tiếp giữa [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java), [View](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/View.java), [Index](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Index.java), và [ForeignKeyConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/ForeignKeyConstraint.java). Khi Table xóa hoặc đổi tên, `MetadataMediator` tự động thông báo tới View và Constraint liên quan.

### 18. Memento Pattern
* **Class / Interface nội bộ**: `TableMemento`, [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java)
* **Bài toán nội bộ Metadata**: Lưu trạng thái snapshot của [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java) (danh sách cột, ràng buộc) vào `TableMemento` trước khi thực hiện chỉnh sửa, cho phép `restore()` lại nếu cần.

### 19. Observer Pattern
* **Class / Interface nội bộ**: `MetadataObserver`, [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java) (Subject), [View](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/View.java) (Observer), [Trigger](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Trigger.java) (Observer)
* **Bài toán nội bộ Metadata**: Khi [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java) thay đổi cấu trúc (`addColumn`, `removeColumn`), [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java) tự động phát sự kiện tới các [View](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/View.java) và [Trigger](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Trigger.java) đăng ký lắng nghe để tự động `compile()`.

### 20. State Pattern
* **Class / Interface nội bộ**: [DatabaseStatus](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/enums/DatabaseStatus.java), `DatabaseState` (`OnlineDatabaseState`, `OfflineDatabaseState`), [Database](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Database.java)
* **Bài toán nội bộ Metadata**: Thao tác `createSchema()` trong [Database](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Database.java) ủy quyền cho trạng thái [DatabaseStatus](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/enums/DatabaseStatus.java) hiện tại. Nếu `OFFLINE`, ném ra `IllegalStateException("Database is offline")`.

### 21. Strategy Pattern
* **Class / Interface nội bộ**: `IndexRebuildStrategy`, [Index](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Index.java)
* **Bài toán nội bộ Metadata**: Lớp [Index](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Index.java) cho phép thay đổi linh hoạt thuật toán rebuild trong phương thức `rebuild()` bằng cách truyền các triển khai `IndexRebuildStrategy` khác nhau.

### 22. Template Method Pattern
* **Class / Interface nội bộ**: [Constraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Constraint.java), [ForeignKeyConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/ForeignKeyConstraint.java), [CheckConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CheckConstraint.java)
* **Bài toán nội bộ Metadata**: Lớp trừu tượng [Constraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Constraint.java) định nghĩa phương thức mẫu `validate()` gọi các bước cố định, các lớp con override bước cụ thể (như `validateReference()` trong [ForeignKeyConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/ForeignKeyConstraint.java) hoặc `evaluate()` trong [CheckConstraint](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CheckConstraint.java)).

### 23. Visitor Pattern
* **Class / Interface nội bộ**: `MetadataVisitor`, [CatalogManager](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/CatalogManager.java), [Database](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Database.java), [Schema](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Schema.java), [Table](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Table.java), [Column](file:///d:/BBV/Database_Management_BBV/DBMS/src/main/java/metadata/Column.java)
* **Bài toán nội bộ Metadata**: Cho phép duyệt và thực hiện các thao tác xử lý mới (như `MetadataExportVisitor`, `MetadataValidationVisitor`) trên toàn bộ cấu trúc các đối tượng Metadata mà không sửa đổi mã nguồn các lớp cơ bản.

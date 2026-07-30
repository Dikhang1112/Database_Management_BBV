# Execution Engine - Implemented Design Patterns Matrix

Tài liệu bảng ma trận đối chiếu tất cả các Design Pattern, Class, Interface, Method và công dụng thực tế trong module `execution_engine`, đồng nhất 100% với Sơ đồ lớp (Class Diagrams) trong `docs/class_diagram/execution_engine/`.

---

## 1. Core Design Patterns Matrix (By Category: Creational ➔ Structural ➔ Behavioral)

Bảng ma trận các Design Pattern cốt lõi áp dụng trong kiến trúc tổng thể module Execution Engine:

| # | Nhóm Pattern | Design Pattern | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|:---|
| 1 | **Creational** | **Factory Method** | `OperatorFactory` | `createOperator(physicalPlanNode)` | Đóng gói logic biến đổi từ các nút kế hoạch vật lý (`PhysicalPlanNode`) thành cây toán tử thực thi (`ExecutionPlanNode`). |
| 2 | **Structural** | **Facade** | `ExecutionEngine` | `execute(physicalPlan)` | Cung cấp giao diện API cấp cao duy nhất cho caller bên ngoài thực thi toàn bộ cây kế hoạch và nhận `QueryResult`. |
| 3 | **Structural** | **Composite** | `ExecutionPlanNode` (Abstract Class)<br>`ScanOperator`, `FilterOperator`, `ProjectOperator`, `JoinOperator`, `AggregateOperator`, `SortOperator` | `open()`, `next()`, `close()` | Định nghĩa cấu trúc cây toán tử thực thi đệ quy kiểu Volcano, cho phép xử lý đồng nhất cả nút lá lẫn nút cha. |
| 4 | **Structural** | **Decorator** | `ExecutionOperatorDecorator` (Abstract Class)<br>`LoggingOperatorDecorator`<br>`ProfilingOperatorDecorator` | `open()`, `next()`, `close()` | Bổ sung động các tính năng ghi log, đo đạc hiệu năng (profiling), đếm số tuple cho toán tử thực thi mà không sửa code cũ. |
| 5 | **Behavioral** | **Strategy** | `JoinStrategy` (`NestedLoopJoinStrategy`, `HashJoinStrategy`, `MergeJoinStrategy`)<br>`ScanStrategy` (`SequentialScanStrategy`, `IndexScanStrategy`) | `execute()`, `scan()` | Đóng gói các giải thuật Join và giải thuật quét bảng thành các đối tượng chiến lược có thể thay đổi linh hoạt lúc runtime. |
| 6 | **Behavioral** | **Iterator** | `TupleIterator` | `hasNext()`, `next()` | Triển khai mô hình Volcano Iterator Model cho phép các toán tử kéo (pull) từng dòng bản ghi demand-driven. |
| 7 | **Behavioral** | **Template Method** | `ScanOperator` | `open()`, `next()`, `close()`, `fetchTuple()` | Định nghĩa thuật toán khung cho chu kỳ đọc bảng, nhường phần rút trích bản ghi chi tiết (`fetchTuple`) cho lớp con / strategy. |
| 8 | **Behavioral** | **State** | `ExecutionState` (Enum: `CREATED`, `OPEN`, `RUNNING`, `FINISHED`, `CLOSED`) | `setState()`, `getState()` | Quản lý và kiểm soát vòng đời chuyển đổi trạng thái thực thi của từng toán tử, đảm bảo an toàn luồng và tránh rò rỉ bộ nhớ. |
| 9 | **Behavioral** | **Observer** | `ExecutionListener` (Interface)<br>`StatisticsCollector` | `onExecutionStarted()`, `onTupleProcessed()`, `onExecutionFinished()` | Tách rời logic lắng nghe sự kiện thực thi và thu thập thống kê số lượng tuple/thời gian chạy khỏi luồng xử lý chính. |

---

## 2. Detailed Subsystem Patterns & Class Breakdown

### 2.1. Execution Engine Pipeline & Operator Factory

Bảng chi tiết các lớp thuộc phân đoạn Điều phối Thực thi và Khởi tạo Toán tử:

| # | Pattern / Role | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Facade** | `ExecutionEngine` | `execute(physicalPlan)` | Entry-point Facade điều phối khởi tạo, thực thi và trả về kết quả truy vấn. |
| 2 | **Factory Method** | `OperatorFactory` | `createOperator()` | Ánh xạ và chuyển đổi từng nút trong `PhysicalPlan` sang `ExecutionPlanNode` tương ứng. |
| 3 | **Input Plan** | `PhysicalPlan` | `getRoot()` | Đóng gói cây kế hoạch thực thi vật lý từ Query Optimizer. |
| 4 | **Input Node** | `PhysicalPlanNode` | `getPhysicalOperatorType()` | Nút đại diện cho loại toán tử vật lý. |
| 5 | **Output Result** | `QueryResult` | `getTuples()`, `getExecutionTime()` | Đóng gói dữ liệu bản ghi kết quả cùng các thông số đo đạc thu được. |

---

### 2.2. Volcano Execution Operators & Composite Hierarchy

Bảng chi tiết các lớp thuộc phân đoạn Cây Toán tử Thực thi (Composite Pattern & Volcano Model):

| # | Pattern / Role | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Composite Base** | `ExecutionPlanNode` | `open()`, `next()`, `close()` | Lớp trừu tượng cơ sở cho mọi toán tử thực thi Volcano. |
| 2 | **Template / Leaf** | `ScanOperator` | `open()`, `next()`, `close()`, `fetchTuple()` | Toán tử đọc dữ liệu từ đĩa/bộ đệm storage engine. |
| 3 | **Composite Node** | `FilterOperator` | `open()`, `next()`, `close()` | Toán tử lọc các dòng dữ liệu thỏa mãn điều kiện `WHERE`. |
| 4 | **Composite Node** | `ProjectOperator` | `open()`, `next()`, `close()` | Toán tử chiếu loại bỏ các cột không cần thiết (`SELECT`). |
| 5 | **Composite Node** | `JoinOperator` | `open()`, `next()`, `close()` | Toán tử thực hiện kết nối dữ liệu giữa 2 bảng. |
| 6 | **Composite Node** | `AggregateOperator` | `open()`, `next()`, `close()` | Toán tử gom nhóm và tính toán hàm tổng hợp (`GROUP BY`, `SUM`, `COUNT`). |
| 7 | **Composite Node** | `SortOperator` | `open()`, `next()`, `close()` | Toán tử sắp xếp dữ liệu (`ORDER BY`). |
| 8 | **Iterator** | `TupleIterator` | `hasNext()`, `next()` | Bộ duyệt dữ liệu dòng bản ghi theo luồng Volcano pull. |

---

### 2.3. Join & Scan Strategy Implementations

Bảng chi tiết các lớp thuộc phân đoạn Chiến lược Nối Bảng và Quét Dữ liệu (Strategy Pattern):

| # | Pattern / Role | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Strategy Interf** | `JoinStrategy` | `execute()` | Giao diện thuật toán nối bảng. |
| 2 | **Strategy Impl** | `NestedLoopJoinStrategy` | `execute()` | Thuật toán Join vòng lặp lồng nhau đơn giản (dành cho tập dữ liệu nhỏ). |
| 3 | **Strategy Impl** | `HashJoinStrategy` | `execute()` | Thuật toán Hash Join hiệu năng cao (Build hash table & Probe). |
| 4 | **Strategy Impl** | `MergeJoinStrategy` | `execute()` | Thuật toán Sort-Merge Join (dành cho dữ liệu đã được sắp xếp trước). |
| 5 | **Strategy Interf** | `ScanStrategy` | `scan()` | Giao diện thuật toán quét bảng dữ liệu. |
| 6 | **Strategy Impl** | `SequentialScanStrategy` | `scan()` | Thuật toán quét tuần tự toàn bộ bảng (Full Table Scan). |
| 7 | **Strategy Impl** | `IndexScanStrategy` | `scan()` | Thuật toán quét dựa trên cây chỉ mục B+Tree (Index Scan). |

---

### 2.4. Operator Decorator, State & Observer Monitoring

Bảng chi tiết các lớp thuộc phân đoạn Trang trí Toán tử, Quản lý Trạng thái và Lắng nghe Sự kiện:

| # | Pattern / Role | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Decorator Base** | `ExecutionOperatorDecorator` | `open()`, `next()`, `close()` | Lớp trừu tượng trang trí cho `ExecutionPlanNode`. |
| 2 | **Decorator Impl** | `LoggingOperatorDecorator` | `next()` | Trang trí thêm tính năng ghi thông vết log cho từng dòng tuple xử lý. |
| 3 | **Decorator Impl** | `ProfilingOperatorDecorator` | `next()` | Trang trí thêm tính năng đo chính xác thời gian thực thi (nanoseconds) của toán tử. |
| 4 | **State Enum** | `ExecutionState` | N/A | Enum lưu trạng thái vòng đời toán tử (`CREATED`, `OPEN`, `RUNNING`, `FINISHED`, `CLOSED`). |
| 5 | **Observer Interf** | `ExecutionListener` | `onExecutionStarted()`, `onTupleProcessed()`, `onExecutionFinished()` | Giao diện đăng ký lắng nghe sự kiện thực thi. |
| 6 | **Observer Impl** | `StatisticsCollector` | `onExecutionFinished()` | Thu thập và tổng hợp chỉ số thống kê runtime của truy vấn. |

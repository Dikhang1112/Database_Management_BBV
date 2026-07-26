# Query Processor - Implemented Design Patterns Matrix

Tài liệu bảng ma trận đối chiếu tất cả các Design Pattern, Class, Interface, Method và công dụng thực tế trong module `query_processor`, đồng nhất 100% với Sơ đồ lớp (Class Diagrams) trong `docs/class_diagram/query_processor/`.

---

## 1. Core Design Patterns Matrix

Bảng ma trận các Design Pattern cốt lõi áp dụng trong kiến trúc tổng thể module Query Processor:

| # | Design Pattern | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Facade** | `QueryProcessor` | `compile(sqlText)` | Cung cấp giao diện tập trung duy nhất cho toàn bộ chuỗi biên dịch và tối ưu hóa câu lệnh SQL (Lexer ➔ Parser ➔ AST ➔ Semantic ➔ Optimizer ➔ PlanGenerator). |
| 2 | **Chain of Responsibility** | `CompilerStage` (Interface)<br>`Lexer`<br>`SQLParser`<br>`ASTBuilder` | `process(...)` | Nối chuỗi các công đoạn xử lý SQL tuần tự độc lập (Tokenize ➔ Parse Tree ➔ AST Builder). |
| 3 | **Composite** | `AST`<br>`ASTNode` (Composite Node) | `accept(visitor)` | Biểu diễn cấu trúc phân cấp cây cú pháp trừu tượng AST đồng nhất. |
| 4 | **Visitor** | `ASTVisitor` (Interface)<br>`SemanticAnalyzer`<br>`QueryRewriter` | `visit(node)`<br>`analyze(ast)`<br>`rewrite(ast)` | Thao tác duyệt cây AST để kiểm tra ngữ nghĩa và tối ưu hóa logic mà không làm thay đổi cấu trúc nút cây `ASTNode`. |
| 5 | **Strategy** | `QueryOptimizer` (Strategy Context)<br>`OptimizationRule` (Interface)<br>`CostEstimator`<br>`PredicatePushdownOptimizer`<br>`ProjectionPushdownOptimizer`<br>`ConstantFoldingOptimizer` | `optimize(plan)`<br>`setOptimizationRule(rule)`<br>`estimate(plan)` | Đóng gói linh hoạt các thuật toán tối ưu hóa dựa trên chi phí (CBO) và các quy tắc biến đổi kế hoạch truy vấn độc lập. |
| 6 | **Builder** | `LogicalPlanBuilder`<br>`PhysicalPlanBuilder` | `build(ast)`<br>`build(logicalPlan)` | Xây dựng từng bước cây kế hoạch logic (`LogicalPlan`) và kế hoạch vật lý (`PhysicalPlan`) từ cây AST và kế hoạch tối ưu. |
| 7 | **Factory Method** | `LogicalOperatorFactory`<br>`PhysicalOperatorFactory` | `createOperator(...)` | Đóng gói logic khởi tạo các đối tượng toán tử logic/vật lý chuyên biệt (`Operator Nodes`). |

---

## 2. Detailed Subsystem Patterns & Class Breakdown

### 2.1. Semantic Analysis & Name Resolution

Bảng chi tiết các lớp thuộc phân đoạn Phân tích ngữ nghĩa (Semantic Analysis) và Phân giải tên (Name Resolution):

| # | Pattern / Role | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Visitor** | `SemanticAnalyzer` | `analyze(ast)`, `visit(node)` | Điều phối quy trình duyệt cây AST thẩm định ngữ nghĩa toàn diện. |
| 2 | **Visitor Interface** | `ASTVisitor` | `visit(node)` | Định nghĩa giao diện duyệt chuẩn cho tất cả các nút cây AST. |
| 3 | **SRP Helper** | `NameResolver` | `resolve(ast)` | Phân giải các đối tượng định danh trong SQL (Database, Schema, Table, Column, Alias). |
| 4 | **SRP Helper** | `TableResolver` | `resolveTable(node)` | Kiểm tra sự tồn tại của bảng dữ liệu trong Catalog Metadata. |
| 5 | **SRP Helper** | `ColumnResolver` | `resolveColumn(node)` | Phân giải thông tin cột và phát hiện xung đột/mơ hồ tên cột. |
| 6 | **SRP Helper** | `AliasResolver` | `resolveAlias(node)` | Phân giải và định danh các tên bí danh (Table Alias, Column Alias). |
| 7 | **SRP Helper** | `TypeChecker` | `validate(ast)` | Thẩm định tính hợp lệ và sự tương thích kiểu dữ liệu toàn hệ thống. |
| 8 | **SRP Helper** | `ExpressionTypeChecker` | `checkExpression(node)` | Kiểm tra kiểu dữ liệu trong các biểu thức đại số, so sánh và logic. |
| 9 | **SRP Helper** | `FunctionTypeChecker` | `checkFunction(node)` | Thẩm định tham số đầu vào và kiểu trả về của các hàm SQL. |
| 10 | **SRP Helper** | `AggregateValidator` | `validate(ast)` | Thẩm định tính hợp lệ của các hàm gom nhóm (SUM, COUNT, AVG, MIN, MAX). |
| 11 | **SRP Helper** | `GroupByValidator` | `validate(ast)` | Kiểm tra quy tắc ngữ nghĩa và điều kiện ràng buộc của mệnh đề `GROUP BY`. |
| 12 | **SRP Helper** | `OrderByValidator` | `validate(ast)` | Kiểm tra danh sách cột và biểu thức sắp xếp trong mệnh đề `ORDER BY`. |

---

### 2.2. Query Optimization Engine

Bảng chi tiết các lớp thuộc Bộ tối ưu hóa truy vấn (Query Optimizer Engine):

| # | Pattern / Role | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Strategy Context** | `QueryOptimizer` | `optimize(plan)`, `setOptimizationRule(rule)` | Điều phối quy trình tối ưu hóa kế hoạch dựa trên chi phí CBO. |
| 2 | **Strategy Interface** | `OptimizationRule` | `optimize(plan)` | Định nghĩa giao diện chung cho các thuật toán và quy tắc tối ưu hóa. |
| 3 | **Strategy Impl** | `PredicatePushdownOptimizer` | `optimize(plan)` | Chiến lược đẩy điều kiện lọc xuống gần nguồn dữ liệu (Scan) để giảm dữ liệu trung gian. |
| 4 | **Strategy Impl** | `ProjectionPushdownOptimizer` | `optimize(plan)` | Chiến lược loại bỏ các cột không sử dụng ngay từ tầng truy xuất đầu tiên. |
| 5 | **Strategy Impl** | `ConstantFoldingOptimizer` | `optimize(plan)` | Chiến lược tính toán trước các biểu thức hằng số trong thời gian biên dịch. |
| 6 | **SRP Helper** | `QueryRewriter` | `rewrite(plan)` | Đóng gói quy trình biến đổi và viết lại truy vấn bảo toàn ngữ nghĩa SQL. |
| 7 | **SRP Helper** | `JoinOptimizer` | `optimize(plan)` | Quản lý và điều phối các thuật toán tối ưu hóa phép nối Join. |
| 8 | **SRP Helper** | `JoinOrderOptimizer` | `optimize(plan)` | Tính toán và lựa chọn thứ tự thực hiện phép nối Join tối ưu chi phí. |
| 9 | **SRP Helper** | `JoinMethodSelector` | `selectJoinMethod(plan)` | Lựa chọn thuật toán Join phù hợp (Nested Loop Join, Hash Join, Merge Join). |
| 10 | **Strategy / Cost** | `CostEstimator` | `estimate(plan)` | Đánh giá tổng chi phí tài nguyên (CPU & I/O) cho ứng viên kế hoạch thực thi. |
| 11 | **SRP Helper** | `CardinalityEstimator` | `estimate(plan)` | Ước lượng kích thước dữ liệu và số lượng dòng kết quả trung gian. |
| 12 | **SRP Helper** | `StatisticsManager` | `estimateCardinality()`, `estimateSelectivity()` | Tra cứu số liệu thống kê dữ liệu (Cardinality, Selectivity) từ Storage Engine. |
| 13 | **SRP Helper** | `PlanEnumerator` | `enumerate(plan)` | Duyệt và tìm kiếm không gian các ứng viên kế hoạch thực thi khả thi. |
| 14 | **SRP Helper** | `AccessPathSelector` | `select(plan)` | Lựa chọn đường dẫn truy xuất dữ liệu tối ưu (Table Scan, Index Scan, Index Only Scan). |

---

### 2.3. Plan Generation & Building

Bảng chi tiết các lớp thuộc Phân đoạn Sinh kế hoạch thực thi (Plan Generation & Building):

| # | Pattern / Role | Class / Interface | Method | Công dụng (Purpose) |
|:---:|:---|:---|:---|:---|
| 1 | **Pipeline Helper** | `PlanGenerator` | `createLogicalPlan(ast)`, `createPhysicalPlan(logicalPlan)` | Điều phối quy trình sinh kế hoạch logic và vật lý qua Builder & Factory. |
| 2 | **Builder** | `LogicalPlanBuilder` | `build(ast)` | Xây dựng từng bước cây kế hoạch logic từ cây cấu trúc AST. |
| 3 | **Builder** | `PhysicalPlanBuilder` | `build(logicalPlan)` | Chuyển đổi và xây dựng kế hoạch thực thi vật lý từ kế hoạch logic. |
| 4 | **Factory Method** | `LogicalOperatorFactory` | `createOperator(node)` | Khởi tạo các toán tử logic (LogicalScan, LogicalFilter, LogicalJoin, LogicalAggregate, LogicalSort). |
| 5 | **Factory Method** | `PhysicalOperatorFactory` | `createOperator(node)` | Khởi tạo các toán tử thực thi vật lý tương ứng chiến lược đã chọn. |
| 6 | **SRP Helper** | `PlanValidator` | `validate(logicalPlan)` | Thẩm định tính hợp lệ và toàn vẹn của kế hoạch trước khi chuyển giao thực thi. |
| 7 | **SRP Helper** | `PlanNormalizer` | `normalize(logicalPlan)` | Chuẩn hóa dạng cây kế hoạch về dạng chuẩn trước khi tạo kế hoạch vật lý. |
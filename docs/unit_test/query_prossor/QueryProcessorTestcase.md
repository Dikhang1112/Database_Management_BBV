# Query Processor Subsystem - Semantic Analysis Unit Test Scenarios

This document defines all unit test scenarios for the `Semantic Analysis` module of the `Query Processor` subsystem (`SemanticAnalyzer`, `NameResolver`, `TypeChecker`, `GroupByValidator`, `OrderByValidator`, `ASTVisitor`, `ASTNode`, and `AST`), covering both positive (happy path) and negative (edge cases and exceptions) scenarios as mapped out in [SemanticAnalysis.md](file:///d:/BBV/Database_Management_BBV/docs/class_diagram/query_processor/detail/SemanticAnalysis.md) and aligned with the Level-3 mock source code implementation.

Each test scenario follows this standard format:
- **Test method:** JUnit method name.
- **Sequence diagram:** Corresponding sequence diagram ID.
- **Input:** Input objects or parameters.
- **Why:** Detailed explanation of the test scenario and rationale for why this test case is necessary.
- **Expected output:** Assertions or expected exceptions.

---

## 1. SemanticAnalyzerTest

### TC-01. Analyze Full AST (Happy Path)
- **Test method:** `analyze_ShouldExecuteFourStepValidationInOrder_WhenValidASTProvided`
- **Sequence diagram:** `TC-01`
- **Input:** Valid `AST` instance containing query representation
- **Why:** **Giải thích & Lý do:** Kiểm thử quy trình phân tích ngữ nghĩa 4 bước tiêu chuẩn theo đúng thứ tự bắt buộc: `NameResolver` -> `TypeChecker` -> `GroupByValidator` -> `OrderByValidator`. Việc phân giải tên định danh phải diễn ra trước khi kiểm tra kiểu dữ liệu và thẩm định các mệnh đề gom nhóm, đảm bảo tính toàn vẹn và nhất quán của cây AST trước khi chuyển sang tầng Planner.
- **Expected output:**
  - `nameResolver.resolve(ast)` được gọi thứ 1.
  - `typeChecker.validate(ast)` được gọi thứ 2.
  - `groupByValidator.validate(ast)` được gọi thứ 3.
  - `orderByValidator.validate(ast)` được gọi thứ 4.
  - Phân tích hoàn tất thành công không ném ra ngoại lệ.

### TC-01A. Analyze Null AST
- **Test method:** `analyze_ShouldHandleNullASTGracefully_WhenASTIsNull`
- **Sequence diagram:** `TC-01A`
- **Input:** `ast = null`
- **Why:** **Giải thích & Lý do:** Kiểm thử khả năng phòng thủ (Defensive Programming) của `SemanticAnalyzer`. Truyền đối tượng AST bị `null` không được làm sập ứng dụng với ngoại lệ `NullPointerException`, mà phải được xử lý an toàn hoặc báo lỗi tham số không hợp lệ.
- **Expected output:**
  - Xử lý an toàn hoặc ném `IllegalArgumentException` ("AST cannot be null").

### TC-01B. Visit AST Node (Visitor Pattern)
- **Test method:** `visit_ShouldInvokeAcceptOnASTNode_WhenValidNodeVisited`
- **Sequence diagram:** `TC-01B`
- **Input:** Mock `ASTNode` instance
- **Why:** **Giải thích & Lý do:** Xác minh việc triển khai đúng mẫu thiết kế Visitor Pattern (Double-dispatch mechanism). `SemanticAnalyzer` đóng vai trò là `ASTVisitor`, khi duyệt nút phải ủy quyền lại cho `node.accept(this)` để xử lý theo đúng kiểu dữ liệu cụ thể của từng nút.
- **Expected output:**
  - Phương thức `node.accept(semanticAnalyzer)` được gọi đúng 1 lần.

### TC-01C. Visit Null AST Node
- **Test method:** `visit_ShouldDoNothing_WhenASTNodeIsNull`
- **Sequence diagram:** `TC-01C`
- **Input:** `node = null`
- **Why:** **Giải thích & Lý do:** Khi đệ quy duyệt cây AST, một số nút con (như `whereCondition` hoặc `groupByList`) có thể nhận giá trị `null`. Test case này đảm bảo bộ duyệt bỏ qua các nút `null` một cách an toàn mà không bị crash hệ thống.
- **Expected output:**
  - Bỏ qua an toàn, không ném ngoại lệ.

### TC-01D. Semantic Analysis Error Propagation
- **Test method:** `analyze_ShouldPropagateException_WhenAnyStepValidationFails`
- **Sequence diagram:** `TC-01D`
- **Input:** `AST` chứa tên bảng/cột không tồn tại
- **Why:** **Giải thích & Lý do:** Kiểm thử cơ chế ngắt sớm (Fail-fast behavior). Nếu bất kỳ bước nào trong chuỗi 4 bước (ví dụ: `NameResolver`) phát hiện lỗi ngữ nghĩa, quá trình phân tích phải dừng ngay lập tức, ném ngoại lệ và không thực thi các bước thẩm định phía sau để tiết kiệm tài nguyên.
- **Expected output:**
  - Ném `SemanticException` (hoặc `IllegalArgumentException`) từ bước bị lỗi.
  - Các bước thẩm định phía sau trong chuỗi bị hủy bỏ.

---

## 2. NameResolverTest

### TC-02. Resolve Table Identifier (Happy Path)
- **Test method:** `resolveTable_ShouldReturnTrue_WhenTableExistsInMetadata`
- **Sequence diagram:** `TC-02`
- **Input:** `ASTNode` chứa tên bảng `"users"`, mock `MetadataModule` trả về `containsTable("users") = true`
- **Why:** **Giải thích & Lý do:** Xác minh tính đúng đắn khi phân giải tên bảng với Catalog Metadata. Tên bảng trong câu lệnh SQL bắt buộc phải đối soát thành công với cơ sở dữ liệu trước khi truy vấn thực thi.
- **Expected output:**
  - `resolveTable(node)` trả về `true`.
  - Gọi kiểm tra `metadataModule.containsTable("users")`.

### TC-02A. Resolve Table Identifier - Table Not Found
- **Test method:** `resolveTable_ShouldReturnFalse_WhenTableDoesNotExistInMetadata`
- **Sequence diagram:** `TC-02A`
- **Input:** `ASTNode` chứa tên bảng không tồn tại `"missing_table"`, mock `MetadataModule` trả về `false`
- **Why:** **Giải thích & Lý do:** Phát hiện sớm các câu lệnh SQL truy vấn vào bảng không tồn tại trong hệ thống, ngăn ngừa lỗi runtime ở tầng lưu trữ dữ liệu.
- **Expected output:**
  - `resolveTable(node)` trả về `false` (hoặc ném `TableNotFoundException`).

### TC-02B. Resolve Column Identifier (Happy Path)
- **Test method:** `resolveColumn_ShouldReturnTrue_WhenColumnExistsInTable`
- **Sequence diagram:** `TC-02B`
- **Input:** `ASTNode` chứa tên cột `"email"` thuộc bảng `"users"`
- **Why:** **Giải thích & Lý do:** Đảm bảo tất cả các cột được tham chiếu trong danh sách SELECT, WHERE, GROUP BY, ORDER BY đều tồn tại trong sơ đồ (schema) của bảng tương ứng.
- **Expected output:**
  - `resolveColumn(node)` trả về `true`.
  - `metadataModule.containsColumn("users", "email")` trả về `true`.

### TC-02C. Resolve Column Identifier - Column Not Found
- **Test method:** `resolveColumn_ShouldReturnFalse_WhenColumnDoesNotExist`
- **Sequence diagram:** `TC-02C`
- **Input:** `ASTNode` chứa tên cột không tồn tại `"unknown_col"`
- **Why:** **Giải thích & Lý do:** Ngăn chặn việc thực thi truy vấn chứa tên cột bị viết sai chính tả hoặc không có trong bảng dữ liệu.
- **Expected output:**
  - `resolveColumn(node)` trả về `false` (hoặc ném `ColumnNotFoundException`).

### TC-02D. Resolve Table Alias (Happy Path)
- **Test method:** `resolveAlias_ShouldReturnTrue_WhenAliasIsValid`
- **Sequence diagram:** `TC-02D`
- **Input:** `ASTNode` định nghĩa bí danh `u` cho bảng `"users"`
- **Why:** **Giải thích & Lý do:** Kiểm thử tính năng phân giải bí danh (Table Alias), cho phép các tên cột được định danh dạng `u.id` ánh xạ chính xác về `users.id` trong phạm vi truy vấn.
- **Expected output:**
  - `resolveAlias(node)` trả về `true`.
  - Bí danh `u` được đăng ký thành công vào phạm vi phân giải (resolution scope).

### TC-02E. Resolve Duplicate Alias Collision
- **Test method:** `resolveAlias_ShouldReturnFalse_WhenAliasIsDuplicatedInSameScope`
- **Sequence diagram:** `TC-02E`
- **Input:** Hai nút bảng cùng định nghĩa một bí danh `t` trong cùng phạm vi truy vấn (Query Scope)
- **Why:** **Giải thích & Lý do:** Tránh sự nhập nhằng (Ambiguity) khi truy vấn đặt trùng bí danh cho hai bảng khác nhau trong lệnh JOIN.
- **Expected output:**
  - `resolveAlias(node)` trả về `false` (hoặc ném `DuplicateAliasException`).

### TC-02F. Resolve Full AST Identifiers
- **Test method:** `resolve_ShouldProcessAllIdentifiersInAST_WhenASTIsProvided`
- **Sequence diagram:** `TC-02F`
- **Input:** Cây `AST` hoàn chỉnh chứa nhiều bảng, cột và bí danh
- **Why:** **Giải thích & Lý do:** Kiểm thử khả năng duyệt và phân giải đồng loạt tất cả định danh trong toàn bộ cây AST của câu lệnh SQL phức tạp.
- **Expected output:**
  - Phương thức `resolve(ast)` hoàn thành và tất cả định danh đều được đánh dấu đã phân giải thành công.

---

## 3. TypeCheckerTest

### TC-03. Validate AST Types (Happy Path)
- **Test method:** `validate_ShouldCheckAllExpressions_WhenASTIsProvided`
- **Sequence diagram:** `TC-03`
- **Input:** Cây `AST` chứa các biểu thức hợp lệ
- **Why:** **Giải thích & Lý do:** Đảm bảo toàn bộ các biểu thức tính toán trên cây AST đều trải qua quá trình thẩm định kiểu dữ liệu.
- **Expected output:**
  - Phương thức `validate(ast)` thực thi thành công không phát sinh lỗi kiểu.

### TC-03A. Check Expression Types - Compatible Operands
- **Test method:** `checkExpression_ShouldReturnTrue_WhenOperandTypesAreCompatible`
- **Sequence diagram:** `TC-03A`
- **Input:** `ASTNode` đại diện cho biểu thức so sánh `age > 18` (`INT` > `INT`)
- **Why:** **Giải thích & Lý do:** Cho phép các phép toán số học và so sánh giữa các kiểu dữ liệu tương thích với nhau (như `INT` với `INT`, hoặc `FLOAT` với `INT`).
- **Expected output:**
  - `checkExpression(node)` trả về `true`.

### TC-03B. Check Expression Types - Incompatible Type Mismatch
- **Test method:** `checkExpression_ShouldReturnFalse_WhenOperandTypesAreIncompatible`
- **Sequence diagram:** `TC-03B`
- **Input:** `ASTNode` đại diện cho biểu thức `age = 'John'` (`INT` = `VARCHAR`)
- **Why:** **Giải thích & Lý do:** Ngăn chặn các phép so sánh vô nghĩa giữa hai kiểu dữ liệu không thể ép kiểu tự động (như Số nguyên và Chuỗi ký tự), tránh lỗi crash hoặc sai kết quả khi thực thi truy vấn.
- **Expected output:**
  - `checkExpression(node)` trả về `false` (hoặc ném `TypeMismatchException`).

---

## 4. GroupByValidatorTest

### TC-04. Validate GROUP BY Clause (Happy Path)
- **Test method:** `validate_ShouldPass_WhenAllNonAggregatedSelectColumnsAreInGroupBy`
- **Sequence diagram:** `TC-04`
- **Input:** `AST` đại diện cho truy vấn `SELECT department FROM employees GROUP BY department`
- **Why:** **Giải thích & Lý do:** Đảm bảo tất cả các cột xuất hiện trong SELECT list đều phải có mặt đầy đủ trong mệnh đề GROUP BY.
- **Expected output:**
  - Phương thức `validate(ast)` hoàn tất thành công.

### TC-04A. Validate GROUP BY Clause - Missing Column
- **Test method:** `validate_ShouldThrowException_WhenColumnMissingFromGroupBy`
- **Sequence diagram:** `TC-04A`
- **Input:** `AST` đại diện cho truy vấn `SELECT department, name FROM employees GROUP BY department`
- **Why:** **Giải thích & Lý do:** Bắt lỗi vi phạm quy tắc GROUP BY SQL: Cột `name` là cột thường nhưng không có trong GROUP BY sẽ gây ra kết quả không xác định (non-deterministic).
- **Expected output:**
  - Ném `SemanticException` ("Column 'name' must appear in GROUP BY clause").

### TC-04B. Validate GROUP BY Clause - Empty GROUP BY with Unaggregated Column
- **Test method:** `validate_ShouldThrowException_WhenQueryHasUnaggregatedColumnsWithoutGroupBy`
- **Sequence diagram:** `TC-04B`
- **Input:** `AST` đại diện cho truy vấn `SELECT name FROM users`
- **Why:** **Giải thích & Lý do:** Kiểm tra và đảm bảo tính hợp lệ của mệnh đề GROUP BY khi có tham chiếu đến cột dữ liệu.
- **Expected output:**
  - Ném `SemanticException` ("Expression in SELECT list not in GROUP BY").

---

## 5. OrderByValidatorTest

### TC-05. Validate ORDER BY Clause (Happy Path)
- **Test method:** `validate_ShouldPass_WhenOrderByColumnsAreValid`
- **Sequence diagram:** `TC-05`
- **Input:** `AST` đại diện cho truy vấn `SELECT id, name FROM users ORDER BY name ASC`
- **Why:** **Giải thích & Lý do:** Xác nhận tính hợp lệ khi sắp xếp dữ liệu theo các cột hợp lệ có quyền truy cập.
- **Expected output:**
  - Phương thức `validate(ast)` hoàn tất thành công.

### TC-05A. Validate ORDER BY Clause - Ambiguous Sort Key
- **Test method:** `validate_ShouldThrowException_WhenOrderByColumnIsAmbiguous`
- **Sequence diagram:** `TC-05A`
- **Input:** `AST` của truy vấn JOIN hai bảng đều có cột `created_at`, và mệnh đề ORDER BY dùng `created_at` không chỉ định tên bảng
- **Why:** **Giải thích & Lý do:** Tránh sự nhập nhằng tiêu chuẩn sắp xếp khi tên cột trùng nhau ở nhiều bảng trong truy vấn kết hợp (JOIN).
- **Expected output:**
  - Ném `SemanticException` ("Ambiguous column reference 'created_at' in ORDER BY").

### TC-05B. Validate ORDER BY Clause - DISTINCT Query Sort Key Missing from Projection
- **Test method:** `validate_ShouldThrowException_WhenDistinctQuerySortColumnNotInSelectList`
- **Sequence diagram:** `TC-05B`
- **Input:** `AST` đại diện cho truy vấn `SELECT DISTINCT name FROM users ORDER BY age`
- **Why:** **Giải thích & Lý do:** Tuân thủ chuẩn ANSI SQL: Khi dùng `SELECT DISTINCT`, cột trong `ORDER BY` bắt buộc phải nằm trong danh sách các cột được chọn (`SELECT list`), vì các dòng trùng lặp đã bị loại bỏ trước khi sắp xếp.
- **Expected output:**
  - Ném `SemanticException` ("ORDER BY items must appear in select list if SELECT DISTINCT is specified").

---

## 6. ASTVisitorAndNodeTest

### TC-06. AST Node Accept Visitor (Happy Path)
- **Test method:** `accept_ShouldInvokeVisitOnVisitor_WhenAcceptCalled`
- **Sequence diagram:** `TC-06`
- **Input:** Lớp triển khai `ASTNode`, mock `ASTVisitor`
- **Why:** **Giải thích & Lý do:** Xác minh cơ chế Double-dispatch của mẫu thiết kế Visitor Pattern: Nút AST phải gọi lại đúng phương thức `visitor.visit(this)` để xử lý theo đúng kiểu nút.
- **Expected output:**
  - Phương thức `visitor.visit(node)` được thực thi.

### TC-06A. AST Node Accept Null Visitor
- **Test method:** `accept_ShouldHandleNullVisitor_WhenVisitorIsNull`
- **Sequence diagram:** `TC-06A`
- **Input:** `ASTNode`, `visitor = null`
- **Why:** **Giải thích & Lý do:** Đảm bảo tính an toàn phòng thủ khi gọi phương thức `accept` với tham số `visitor` bị `null`.
- **Expected output:**
  - Xử lý an toàn không gây crash ứng dụng.

### TC-06B. AST Root Traversal
- **Test method:** `getRoot_ShouldReturnAndSetRootNode_WhenASTConstructed`
- **Sequence diagram:** `TC-06B`
- **Input:** Đối tượng `AST`, nút `rootNode`
- **Why:** **Giải thích & Lý do:** Kiểm thử khả năng thiết lập và truy xuất nút gốc (Root node) của cấu trúc cây biểu diễn cú pháp AST.
- **Expected output:**
  - `ast.getRoot()` trả về đúng `rootNode` đã thiết lập.

---

## 7. QueryOptimizerTest

### TC-07. Execute Full Optimization Pipeline (Happy Path)
- **Test method:** `optimize_ShouldExecuteOptimizationPipeline_WhenLogicalPlanIsValid`
- **Sequence diagram:** `TC-07`
- **Input:** Instance `LogicalPlan` hợp lệ
- **Why:** **Giải thích & Lý do:** Kiểm thử quy trình tối ưu hóa câu lệnh SQL toàn diện thông qua pipeline `QueryOptimizer`: thực thi qua tuần tự các công đoạn Viết lại câu lệnh (`QueryRewriter`), Tối ưu hóa phép nối (`JoinOptimizer`), Ước lượng chi phí (`CostEstimator`), và Tạo kế hoạch vật lý (`PlanEnumerator`).
- **Expected output:**
  - Trả về đối tượng `PhysicalPlan` đã được tối ưu hoàn chỉnh.
  - Mỗi thành phần phụ thuộc trong pipeline được thực thi đúng thứ tự.

### TC-07A. Query Rewrite Failure Handling
- **Test method:** `optimize_ShouldStopPipeline_WhenQueryRewriteFails`
- **Sequence diagram:** `TC-07A`
- **Input:** `LogicalPlan` hợp lệ, mock `QueryRewriter` ném ra ngoại lệ `QueryRewriteException` (hoặc `RuntimeException`)
- **Why:** **Giải thích & Lý do:** Kiểm thử cơ chế ngắt sớm (Fail-fast). Khi công đoạn biến đổi/viết lại truy vấn gặp lỗi, pipeline phải dừng lập tức và lan truyền ngoại lệ, không được tiếp tục chuyển sang giai đoạn tối ưu phép nối hay tính toán chi phí.
- **Expected output:**
  - Ném ngoại lệ `QueryRewriteException` (hoặc `RuntimeException`).
  - Các bước `JoinOptimizer`, `CostEstimator`, `PlanEnumerator` không được nạp hoặc thực thi.

### TC-07B. Join Optimization Failure Handling
- **Test method:** `optimize_ShouldStopPipeline_WhenJoinOptimizationFails`
- **Sequence diagram:** `TC-07B`
- **Input:** `LogicalPlan` hợp lệ, mock `JoinOptimizer` ném ngoại lệ khi tối ưu phép nối
- **Why:** **Giải thích & Lý do:** Kiểm thử tính toàn vẹn của pipeline khi giai đoạn tối ưu hóa thứ tự và phương thức JOIN thất bại. Toàn bộ quy trình tối ưu phải hủy bỏ để tránh tạo ra kế hoạch thực thi không an toàn.
- **Expected output:**
  - Lan truyền ngoại lệ từ `JoinOptimizer`.
  - Hủy bỏ các bước phía sau (`CostEstimator`, `PlanEnumerator`).

### TC-07C. Cost Estimation Failure Handling
- **Test method:** `optimize_ShouldStopPipeline_WhenCostEstimationFails`
- **Sequence diagram:** `TC-07C`
- **Input:** `LogicalPlan` hợp lệ, mock `CostEstimator` ném ngoại lệ trong quá trình tính toán chi phí tài nguyên
- **Why:** **Giải thích & Lý do:** Đảm bảo hệ thống không tạo kế hoạch vật lý khi việc ước lượng chi phí CBO bị thất bại hoặc không thể truy xuất số liệu thống kê nghiêm trọng.
- **Expected output:**
  - Lan truyền ngoại lệ từ `CostEstimator`.
  - Phương thức `PlanEnumerator.enumerate` không được gọi.

### TC-07D. Null Logical Plan Handling
- **Test method:** `optimize_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull`
- **Sequence diagram:** `TC-07D`
- **Input:** `logicalPlan = null`
- **Why:** **Giải thích & Lý do:** Kiểm thử tính an toàn phòng thủ (Defensive Programming) của `QueryOptimizer` khi nhận tham số `LogicalPlan` bị `null`.
- **Expected output:**
  - Ném ngoại lệ `IllegalArgumentException` ("Logical plan cannot be null").

### TC-07E. Empty Logical Plan Handling
- **Test method:** `optimize_ShouldReturnPhysicalPlan_WhenLogicalPlanIsEmpty`
- **Sequence diagram:** `TC-07E`
- **Input:** `LogicalPlan` rỗng (không chứa nút toán tử nào)
- **Why:** **Giải thích & Lý do:** Kiểm thử khả năng xử lý biên khi cây kế hoạch logic rỗng, đảm bảo hệ thống vẫn tạo ra một `PhysicalPlan` rỗng tương ứng mà không bị crash.
- **Expected output:**
  - Trả về đối tượng `PhysicalPlan` hợp lệ (rỗng).

### TC-07F. Verify Dependency Invocation Count
- **Test method:** `optimize_ShouldInvokeEachDependencyExactlyOnce_WhenOptimizationSucceeds`
- **Sequence diagram:** `TC-07F`
- **Input:** `LogicalPlan` hợp lệ và các mock dependencies (`QueryRewriter`, `JoinOptimizer`, `CostEstimator`, `PlanEnumerator`)
- **Why:** **Giải thích & Lý do:** Xác minh cơ chế điều phối (Orchestration): đảm bảo mỗi thành phần phụ thuộc trong pipeline chỉ được kích hoạt đúng 1 lần duy nhất trên mỗi lượt tối ưu hóa.
- **Expected output:**
  - Gọi `QueryRewriter.rewrite(...)` đúng 1 lần.
  - Gọi `JoinOptimizer.optimize(...)` đúng 1 lần.
  - Gọi `CostEstimator.estimate(...)` đúng 1 lần.
  - Gọi `PlanEnumerator.enumerate(...)` đúng 1 lần.

### TC-07G. Replace Optimization Strategy Rule
- **Test method:** `setOptimizationRule_ShouldReplaceOptimizationStrategy_WhenNewRuleProvided`
- **Sequence diagram:** `TC-07G`
- **Input:** Đối tượng `OptimizationRule` mới
- **Why:** **Giải thích & Lý do:** Kiểm thử tính năng tráo đổi chiến lược tối ưu động (Strategy Pattern). Cho phép người dùng hoặc hệ thống cập nhật luật tối ưu mới vào `QueryOptimizer` tại runtime.
- **Expected output:**
  - Chiến lược `OptimizationRule` mới được lưu trữ và áp dụng thành công cho các lượt tối ưu tiếp theo.

---

## 8. QueryRewriterTest

### TC-08. Execute Full Rewrite Rules (Happy Path)
- **Test method:** `rewrite_ShouldApplyAllRewriteRules_WhenLogicalPlanIsValid`
- **Sequence diagram:** `TC-08`
- **Input:** `LogicalPlan` hợp lệ chứa các biểu thức điều kiện, cột truy xuất và hằng số
- **Why:** **Giải thích & Lý do:** Xác minh bộ viết lại `QueryRewriter` áp dụng đầy đủ các quy tắc biến đổi đại số quan hệ: Đẩy điều kiện lọc (`predicatePushdown`), Thu gọn danh sách cột (`projectionPushdown`), và Tính toán trước hằng số (`constantFolding`).
- **Expected output:**
  - Trả về `LogicalPlan` đã được chuẩn hóa và biến đổi tối ưu.

### TC-08A. Predicate Pushdown Rule
- **Test method:** `predicatePushdown_ShouldMovePredicatesCloserToScan_WhenFilterExists`
- **Sequence diagram:** `TC-08A`
- **Input:** `LogicalPlan` chứa nút `Filter` nằm trên nút `Join` hoặc `Project`
- **Why:** **Giải thích & Lý do:** Kiểm thử chiến lược đẩy mệnh đề lọc (`WHERE`) xuống gần toán tử quét dữ liệu (`Scan`) nhất có thể, giúp giảm khối lượng bản ghi trung gian trước khi thực hiện phép nối hoặc gom nhóm.
- **Expected output:**
  - Nút `Filter` được di chuyển xuống bên dưới nút `Join` hoặc nằm ngay trên nút `Scan`.

### TC-08B. Projection Pushdown Rule
- **Test method:** `projectionPushdown_ShouldRemoveUnusedColumns_WhenProjectionContainsExtraColumns`
- **Sequence diagram:** `TC-08B`
- **Input:** `LogicalPlan` chứa các cột không được tham chiếu trong kết quả cuối cùng hoặc các bước tính toán phía trên
- **Why:** **Giải thích & Lý do:** Loại bỏ sớm các cột dữ liệu dư thừa ngay từ tầng quét đĩa, giúp tiết kiệm bộ nhớ RAM và băng thông I/O.
- **Expected output:**
  - Danh sách cột trong toán tử `Scan`/`Project` bị cắt giảm, chỉ giữ lại các cột thực sự cần thiết.

### TC-08C. Constant Folding Rule
- **Test method:** `constantFolding_ShouldSimplifyConstantExpressions_WhenExpressionIsConstant`
- **Sequence diagram:** `TC-08C`
- **Input:** `LogicalPlan` chứa biểu thức hằng số như `1 + 1` hoặc `YEAR(2026)`
- **Why:** **Giải thích & Lý do:** Đơn giản hóa các biểu thức hằng số ngay trong thời gian biên dịch (Compile-time evaluation) thay vì tính toán lặp đi lặp lại trên từng dòng dữ liệu trong thời gian thực thi (Runtime execution).
- **Expected output:**
  - Biểu thức `1 + 1` được tính sẵn thành giá trị hằng số `2` trên cây `LogicalPlan`.

### TC-08D. Skip Already Optimized Plan
- **Test method:** `rewrite_ShouldSkipRewrite_WhenLogicalPlanAlreadyOptimized`
- **Sequence diagram:** `TC-08D`
- **Input:** `LogicalPlan` đã qua biến đổi hoặc đã ở dạng chuẩn tối ưu
- **Why:** **Giải thích & Lý do:** Tránh lãng phí tài nguyên CPU khi thực hiện lại các phép biến đổi không cần thiết trên một cây kế hoạch đã được tối ưu từ trước.
- **Expected output:**
  - Trả về nguyên mẫu `LogicalPlan` ban đầu mà không làm thay đổi cấu trúc cây.

### TC-08E. Null Logical Plan Handling in Rewriter
- **Test method:** `rewrite_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull`
- **Sequence diagram:** `TC-08E`
- **Input:** `logicalPlan = null`
- **Why:** **Giải thích & Lý do:** Đảm bảo an toàn phòng thủ cho `QueryRewriter` khi nhận tham số vào bị `null`.
- **Expected output:**
  - Ném ngoại lệ `IllegalArgumentException` ("Logical plan cannot be null").

### TC-08F. Fail-Fast Rewrite Pipeline
- **Test method:** `rewrite_ShouldStopRemainingRewriteRules_WhenPredicatePushdownFails`
- **Sequence diagram:** `TC-08F`
- **Input:** `LogicalPlan` lỗi làm cho công đoạn `predicatePushdown` ném ra ngoại lệ
- **Why:** **Giải thích & Lý do:** Xác minh tính chất Fail-fast: Nếu một luật biến đổi bị lỗi, quá trình biến đổi phải dừng ngay, không được chạy tiếp `projectionPushdown` hay `constantFolding`.
- **Expected output:**
  - Ngoại lệ được ném ra ngay lập tức.
  - Các hàm `projectionPushdown` và `constantFolding` bị hủy bỏ.

---

## 9. JoinOptimizerTest

### TC-09. Execute Join Optimization Pipeline (Happy Path)
- **Test method:** `optimize_ShouldOptimizeJoinPipeline_WhenLogicalPlanContainsJoin`
- **Sequence diagram:** `TC-09`
- **Input:** `LogicalPlan` chứa các toán tử nối bảng (`JOIN`)
- **Why:** **Giải thích & Lý do:** Xác minh quy trình tối ưu phép nối: tính toán thứ tự nối các bảng tối ưu chi phí (`optimizeJoinOrder`) và lựa chọn thuật toán nối phù hợp (`selectJoinMethod`).
- **Expected output:**
  - Trả về `LogicalPlan` chứa cấu trúc cây JOIN và thuật toán JOIN được ấn định tối ưu.

### TC-09A. Join Order Optimization
- **Test method:** `optimizeJoinOrder_ShouldReorderJoinSequence_WhenMultipleTablesExist`
- **Sequence diagram:** `TC-09A`
- **Input:** `LogicalPlan` thực hiện JOIN 3 bảng `A`, `B`, `C` (trong đó bảng `A` rất lớn, bảng `B` và `C` rất nhỏ)
- **Why:** **Giải thích & Lý do:** Kiểm thử thuật toán hoán vị và tìm thứ tự JOIN sao cho nối các bảng nhỏ trước để thu hẹp kích thước tập dữ liệu trung gian trước khi nối với bảng lớn.
- **Expected output:**
  - Thứ tự JOIN được sắp xếp lại thành `(B JOIN C) JOIN A`.

### TC-09B. Select Hash Join Method
- **Test method:** `selectJoinMethod_ShouldChooseHashJoin_WhenHashJoinIsOptimal`
- **Sequence diagram:** `TC-09B`
- **Input:** `LogicalPlan` có phép nối bằng (`Equi-Join`) trên tập dữ liệu kích thước trung bình/lớn không có sẵn chỉ mục
- **Why:** **Giải thích & Lý do:** Kiểm thử khả năng lựa chọn thuật toán `Hash Join` khi chi phí dựng bảng Hash nhỏ hơn chi phí quét lặp nhiều lần (Nested Loop).
- **Expected output:**
  - Toán tử JOIN được chỉ định phương thức thực thi vật lý là `Hash Join`.

### TC-09C. Select Nested Loop Join Method
- **Test method:** `selectJoinMethod_ShouldChooseNestedLoopJoin_WhenInputTablesAreSmall`
- **Sequence diagram:** `TC-09C`
- **Input:** `LogicalPlan` chứa phép nối với bảng đầu vào có số lượng dòng rất nhỏ (< 100 dòng)
- **Why:** **Giải thích & Lý do:** Lựa chọn thuật toán `Nested Loop Join` cho các bảng nhỏ vì chi phí khởi tạo bảng Hash hay Sắp xếp (Merge Sort) lớn hơn việc chạy vòng lặp trực tiếp.
- **Expected output:**
  - Toán tử JOIN được chỉ định phương thức thực thi là `Nested Loop Join`.

### TC-09D. Handle Plan Without Join
- **Test method:** `optimize_ShouldReturnOriginalPlan_WhenLogicalPlanContainsNoJoin`
- **Sequence diagram:** `TC-09D`
- **Input:** `LogicalPlan` chỉ truy vấn trên 1 bảng đơn (không chứa mệnh đề `JOIN`)
- **Why:** **Giải thích & Lý do:** Đảm bảo `JoinOptimizer` hoạt động an toàn và trả về nguyên trạng cây kế hoạch khi câu lệnh SQL không chứa phép nối bảng nào.
- **Expected output:**
  - Trả về đúng cây `LogicalPlan` ban đầu mà không bị sửa đổi.

### TC-09E. Null Logical Plan Handling in JoinOptimizer
- **Test method:** `optimize_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull`
- **Sequence diagram:** `TC-09E`
- **Input:** `logicalPlan = null`
- **Why:** **Giải thích & Lý do:** Đảm bảo kiểm tra an toàn tham số đầu vào của `JoinOptimizer`.
- **Expected output:**
  - Ném ngoại lệ `IllegalArgumentException` ("Logical plan cannot be null").

---

## 10. CostEstimatorTest

### TC-10. Calculate Execution Cost (Happy Path)
- **Test method:** `estimate_ShouldCalculateExecutionCost_WhenLogicalPlanIsValid`
- **Sequence diagram:** `TC-10`
- **Input:** `LogicalPlan` hợp lệ
- **Why:** **Giải thích & Lý do:** Kiểm thử khả năng tổng hợp chi phí tài nguyên (CPU Cost + I/O Cost) dựa trên số dòng ước lượng và kích thước dữ liệu của cây `LogicalPlan`.
- **Expected output:**
  - Trả về giá trị chi phí dạng `double` lớn hơn `0.0`.

### TC-10A. Estimate Row Count (Cardinality)
- **Test method:** `estimateCardinality_ShouldEstimateRowCount_WhenStatisticsAvailable`
- **Sequence diagram:** `TC-10A`
- **Input:** `LogicalPlan` chứa nút `Scan` hoặc `Filter` và thông tin thống kê số dòng bảng
- **Why:** **Giải thích & Lý do:** Kiểm thử tính toán số lượng dòng dữ liệu dự kiến (Cardinality) đầu ra của một toán tử logic.
- **Expected output:**
  - Trả về ước lượng số dòng dạng `double` (hoặc `long`) tương ứng với độ chọn lọc của dữ liệu.

### TC-10B. Estimate Predicate Selectivity
- **Test method:** `estimateSelectivity_ShouldEstimatePredicateSelectivity_WhenFilterExists`
- **Sequence diagram:** `TC-10B`
- **Input:** `LogicalPlan` chứa điều kiện lọc `age > 30`
- **Why:** **Giải thích & Lý do:** Tính toán tỷ lệ phần trăm dữ liệu thỏa mãn điều kiện lọc (`Selectivity` từ `0.0` đến `1.0`), phục vụ cho việc tính Cardinality phía trên.
- **Expected output:**
  - Trả về giá trị độ chọn lọc `Selectivity` trong khoảng `[0.0, 1.0]`.

### TC-10C. Verify Interaction with StatisticsManager
- **Test method:** `estimate_ShouldInvokeStatisticsManager_WhenCostCalculationStarts`
- **Sequence diagram:** `TC-10C`
- **Input:** `LogicalPlan` hợp lệ, mock `StatisticsManager`
- **Why:** **Giải thích & Lý do:** Xác minh `CostEstimator` tương tác và lấy các chỉ số dữ liệu thống kê từ `StatisticsManager` khi thực hiện tính toán chi phí.
- **Expected output:**
  - Phương thức `statisticsManager.estimateCardinality()` hoặc `estimateSelectivity()` được kích hoạt.

### TC-10D. Fallback Default Statistics Handling
- **Test method:** `estimate_ShouldUseDefaultStatistics_WhenMetadataStatisticsUnavailable`
- **Sequence diagram:** `TC-10D`
- **Input:** `LogicalPlan` cho bảng mới tạo chưa có số liệu thống kê trong Metadata
- **Why:** **Giải thích & Lý do:** Đảm bảo bộ ước lượng chi phí vẫn hoạt động an toàn bằng cách dùng chỉ số mặc định (Default heuristics) khi bảng chưa được phân tích dữ liệu thống kê (`ANALYZE`).
- **Expected output:**
  - Trả về chi phí ước lượng dựa trên tham số mặc định mà không bị ném ngoại lệ.

### TC-10E. Null Logical Plan Handling in CostEstimator
- **Test method:** `estimate_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull`
- **Sequence diagram:** `TC-10E`
- **Input:** `logicalPlan = null`
- **Why:** **Giải thích & Lý do:** Thẩm định an toàn tham số đầu vào bị `null` của `CostEstimator`.
- **Expected output:**
  - Ném ngoại lệ `IllegalArgumentException` ("Logical plan cannot be null").

---

## 11. StatisticsManagerTest

### TC-11. Estimate Table Cardinality from Metadata
- **Test method:** `estimateCardinality_ShouldReturnEstimatedRowCount_WhenTableStatisticsExist`
- **Sequence diagram:** `TC-11`
- **Input:** Tên bảng đã được thu thập thống kê trong Metadata (ví dụ: 10,000 dòng)
- **Why:** **Giải thích & Lý do:** Thao tác tra cứu số lượng bản ghi thực tế của bảng dữ liệu lưu trong Catalog Metadata.
- **Expected output:**
  - Trả về đúng số lượng dòng đã lưu (ví dụ: `10000.0`).

### TC-11A. Estimate Column Predicate Selectivity
- **Test method:** `estimateSelectivity_ShouldReturnEstimatedFilterRatio_WhenColumnStatisticsExist`
- **Sequence diagram:** `TC-11A`
- **Input:** Tên cột có thông tin Histogram hoặc số giá trị duy nhất (Distinct Values - NDV)
- **Why:** **Giải thích & Lý do:** Tính toán độ chọn lọc của cột dựa trên số lượng giá trị phân biệt `1 / NDV` hoặc biểu đồ tần suất Histogram.
- **Expected output:**
  - Trả về tỷ lệ độ chọn lọc chính xác (ví dụ: `0.05`).

### TC-11B. Default Table Cardinality on Missing Statistics
- **Test method:** `estimateCardinality_ShouldReturnDefaultValue_WhenTableStatisticsMissing`
- **Sequence diagram:** `TC-11B`
- **Input:** Truy vấn bảng chưa từng được thống kê
- **Why:** **Giải thích & Lý do:** Cung cấp giá trị ước lượng dòng mặc định an toàn khi thông tin thống kê bảng không tồn tại.
- **Expected output:**
  - Trả về giá trị số dòng mặc định của hệ thống (ví dụ: `1000.0`).

### TC-11C. Default Selectivity on Missing Column Statistics
- **Test method:** `estimateSelectivity_ShouldReturnDefaultValue_WhenColumnStatisticsMissing`
- **Sequence diagram:** `TC-11C`
- **Input:** Điều kiện lọc trên cột chưa được thống kê
- **Why:** **Giải thích & Lý do:** Trả về giá trị độ chọn lọc mặc định (ví dụ: `0.1` cho phép so sánh =, `0.33` cho phép so sánh range >/<) khi thiếu số liệu cột.
- **Expected output:**
  - Trả về giá trị độ chọn lọc mặc định quy định bởi hệ thống.

---

## 12. PlanEnumeratorTest

### TC-12. Generate Physical Plan (Happy Path)
- **Test method:** `enumerate_ShouldGeneratePhysicalPlan_WhenLogicalPlanIsValid`
- **Sequence diagram:** `TC-12`
- **Input:** `LogicalPlan` đã qua tối ưu hóa
- **Why:** **Giải thích & Lý do:** Kiểm thử giai đoạn chuyển đổi toàn bộ cây kế hoạch logic `LogicalPlan` thành cây kế hoạch thực thi vật lý `PhysicalPlan` sẵn sàng cho `ExecutionEngine`.
- **Expected output:**
  - Trả về đối tượng `PhysicalPlan` hợp lệ chứa các toán tử thực thi đĩa/bộ nhớ cụ thể.

### TC-12A. Select Best Access Path
- **Test method:** `selectAccessPath_ShouldChooseBestAccessPath_WhenMultipleCandidatesExist`
- **Sequence diagram:** `TC-12A`
- **Input:** `LogicalPlan` có nhiều đường truy xuất dữ liệu khả thi (Table Scan vs Index Scan)
- **Why:** **Giải thích & Lý do:** Thẩm định khả năng so sánh và chọn đường truy xuất dữ liệu có chi phí thấp nhất trong danh sách ứng viên.
- **Expected output:**
  - Trả về toán tử truy xuất vật lý có chi phí tối ưu nhất.

### TC-12B. Generate Sequential Scan When No Index
- **Test method:** `enumerate_ShouldGenerateSequentialScan_WhenNoIndexAvailable`
- **Sequence diagram:** `TC-12B`
- **Input:** `LogicalPlan` truy vấn bảng không tạo chỉ mục (`Index`)
- **Why:** **Giải thích & Lý do:** Khi không có sẵn chỉ mục phù hợp, hệ thống bắt buộc phải chọn phương pháp duyệt toàn bộ bảng (`Sequential Scan`).
- **Expected output:**
  - Nút truy xuất dữ liệu vật lý là `PhysicalSeqScanNode`.

### TC-12C. Generate Index Scan When Index Exists
- **Test method:** `enumerate_ShouldGenerateIndexScan_WhenMatchingIndexExists`
- **Sequence diagram:** `TC-12C`
- **Input:** `LogicalPlan` chứa điều kiện lọc trên cột đã được đánh chỉ mục `B+ Tree`
- **Why:** **Giải thích & Lý do:** Tận dụng chỉ mục có sẵn để tạo toán tử tìm kiếm nhanh qua chỉ mục (`Index Scan`) thay vì duyệt tuần tự toàn bộ bảng.
- **Expected output:**
  - Nút truy xuất dữ liệu vật lý là `PhysicalIndexScanNode`.

### TC-12D. Null Logical Plan Handling in PlanEnumerator
- **Test method:** `enumerate_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull`
- **Sequence diagram:** `TC-12D`
- **Input:** `logicalPlan = null`
- **Why:** **Giải thích & Lý do:** Đảm bảo an toàn phòng thủ cho `PlanEnumerator`.
- **Expected output:**
  - Ném ngoại lệ `IllegalArgumentException` ("Logical plan cannot be null").

---

## 13. QueryOptimizerInteractionTest

### TC-13. QueryRewriter Execution Order
- **Test method:** `optimize_ShouldInvokeQueryRewriterFirst_WhenOptimizationPipelineStarts`
- **Sequence diagram:** `TC-13`
- **Input:** `LogicalPlan` hợp lệ và mock pipeline stages
- **Why:** **Giải thích & Lý do:** Kiểm tra thứ tự khởi tạo pipeline: `QueryRewriter` phải là thành phần đầu tiên được gọi để biến đổi đại số trước khi tính toán các phương án nối bảng.
- **Expected output:**
  - `queryRewriter.rewrite(...)` được gọi đầu tiên trước tất cả các giai đoạn khác.

### TC-13A. JoinOptimizer Execution Order After QueryRewriter
- **Test method:** `optimize_ShouldInvokeJoinOptimizerAfterQueryRewriter_WhenRewriteCompletes`
- **Sequence diagram:** `TC-13A`
- **Input:** Pipeline với `QueryRewriter` hoàn thành thành công
- **Why:** **Giải thích & Lý do:** Đảm bảo `JoinOptimizer` chỉ được gọi ngay sau khi công đoạn biến đổi/viết lại cây kế hoạch logic `QueryRewriter` kết thúc.
- **Expected output:**
  - `joinOptimizer.optimize(...)` được gọi ngay sau `queryRewriter.rewrite(...)`.

### TC-13B. CostEstimator Execution Order After JoinOptimizer
- **Test method:** `optimize_ShouldInvokeCostEstimatorAfterJoinOptimizer_WhenJoinOptimizationCompletes`
- **Sequence diagram:** `TC-13B`
- **Input:** Pipeline với `JoinOptimizer` hoàn thành thành công
- **Why:** **Giải thích & Lý do:** Xác minh `CostEstimator` được thực thi sau khi cấu trúc cây và thứ tự JOIN đã được tối ưu.
- **Expected output:**
  - `costEstimator.estimate(...)` được gọi sau `joinOptimizer.optimize(...)`.

### TC-13C. PlanEnumerator Execution Order Last
- **Test method:** `optimize_ShouldInvokePlanEnumeratorLast_WhenOptimizationPipelineCompletes`
- **Sequence diagram:** `TC-13C`
- **Input:** Tất cả các bước tối ưu logic và chi phí đã hoàn thành
- **Why:** **Giải thích & Lý do:** `PlanEnumerator` phải là bước cuối cùng trong pipeline để biến đổi `LogicalPlan` hoàn chỉnh thành `PhysicalPlan`.
- **Expected output:**
  - `planEnumerator.enumerate(...)` được gọi cuối cùng trong chuỗi.

### TC-13D. Pipeline Fail-Fast Behavior Verification
- **Test method:** `optimize_ShouldStopRemainingStages_WhenAnyOptimizationStageFails`
- **Sequence diagram:** `TC-13D`
- **Input:** Thất bại tại một bước bất kỳ trong pipeline (ví dụ: `JoinOptimizer`)
- **Why:** **Giải thích & Lý do:** Xác minh nguyên lý ngắt chuỗi: một bước lỗi sẽ làm dừng toàn bộ các bước còn lại phía sau.
- **Expected output:**
  - Các bước sau bước bị lỗi không được gọi bất kỳ phương thức nào.

### TC-13E. CostEstimator and StatisticsManager Collaboration
- **Test method:** `estimate_ShouldInvokeStatisticsManager_WhenCostEstimatorCalculatesCost`
- **Sequence diagram:** `TC-13E`
- **Input:** `CostEstimator` nhận `LogicalPlan` cần tính chi phí
- **Why:** **Giải thích & Lý do:** Kiểm thử sự tương tác phối hợp giữa `CostEstimator` và `StatisticsManager` để lấy tham số đầu vào cho công thức tính toán CBO.
- **Expected output:**
  - `StatisticsManager` được kích hoạt và trả về dữ liệu cho `CostEstimator`.

### TC-13F. Verify Single Execution per Optimization Lifecycle
- **Test method:** `optimize_ShouldInvokeEachDependencyExactlyOnce_WhenOptimizationSucceeds`
- **Sequence diagram:** `TC-13F`
- **Input:** Một vòng đời tối ưu hóa thành công hoàn chỉnh
- **Why:** **Giải thích & Lý do:** Đảm bảo không xảy ra hiện tượng gọi lặp lại (duplicate call) đối với bất kỳ thành phần nào trong pipeline trong cùng một lượt tối ưu.
- **Expected output:**
  - Tất cả các mock dependency (`QueryRewriter`, `JoinOptimizer`, `CostEstimator`, `PlanEnumerator`) đều ghi nhận đúng 1 lượt gọi (`verify(..., times(1))`).


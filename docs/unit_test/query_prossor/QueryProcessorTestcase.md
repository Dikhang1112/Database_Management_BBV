# Query Processor Subsystem - Semantic Analysis Unit Test Scenarios

This document defines all unit test scenarios for the `Semantic Analysis` module of the `Query Processor` subsystem (`SemanticAnalyzer`, `NameResolver`, `TypeChecker`, `AggregateValidator`, `GroupByValidator`, `OrderByValidator`, `ASTVisitor`, `ASTNode`, and `AST`), covering both positive (happy path) and negative (edge cases and exceptions) scenarios as mapped out in [SemanticAnalysis.md](file:///d:/BBV/Database_Management_BBV/docs/class_diagram/query_processor/detail/SemanticAnalysis.md) and aligned with the Level-3 mock source code implementation.

Each test scenario follows this standard format:
- **Test method:** JUnit method name.
- **Sequence diagram:** Corresponding sequence diagram ID.
- **Input:** Input objects or parameters.
- **Why:** Detailed explanation of the test scenario and rationale for why this test case is necessary.
- **Expected output:** Assertions or expected exceptions.

---

## 1. SemanticAnalyzerTest

### TC-01. Analyze Full AST (Happy Path)
- **Test method:** `analyze_ShouldExecuteFiveStepValidationInOrder_WhenValidASTProvided`
- **Sequence diagram:** `TC-01`
- **Input:** Valid `AST` instance containing query representation
- **Why:** **Giải thích & Lý do:** Kiểm thử quy trình phân tích ngữ nghĩa 5 bước tiêu chuẩn theo đúng thứ tự bắt buộc: `NameResolver` -> `TypeChecker` -> `AggregateValidator` -> `GroupByValidator` -> `OrderByValidator`. Việc phân giải tên định danh phải diễn ra trước khi kiểm tra kiểu dữ liệu và thẩm định các mệnh đề gom nhóm, đảm bảo tính toàn vẹn và nhất quán của cây AST trước khi chuyển sang tầng Planner.
- **Expected output:**
  - `nameResolver.resolve(ast)` được gọi thứ 1.
  - `typeChecker.validate(ast)` được gọi thứ 2.
  - `aggregateValidator.validate(ast)` được gọi thứ 3.
  - `groupByValidator.validate(ast)` được gọi thứ 4.
  - `orderByValidator.validate(ast)` được gọi thứ 5.
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
- **Why:** **Giải thích & Lý do:** Kiểm thử cơ chế ngắt sớm (Fail-fast behavior). Nếu bất kỳ bước nào trong chuỗi 5 bước (ví dụ: `NameResolver`) phát hiện lỗi ngữ nghĩa, quá trình phân tích phải dừng ngay lập tức, ném ngoại lệ và không thực thi các bước thẩm định phía sau để tiết kiệm tài nguyên.
- **Expected output:**
  - Ném `SemanticException` (hoặc `IllegalArgumentException`) từ bước bị lỗi.
  - Các bước thẩm định phía sau trong chuỗi bị hủy bỏ.

---

## 2. NameResolverTest

### TC-02. Resolve Table Identifier (Happy Path)
- **Test method:** `resolveTable_ShouldReturnTrue_WhenTableExistsInMetadata`
- **Sequence diagram:** `TC-02`
- **Input:** `ASTNode` chứa tên bảng `"users"`, mock `MetadataModule` trả về `tableExists("users") = true`
- **Why:** **Giải thích & Lý do:** Xác minh tính đúng đắn khi phân giải tên bảng với Catalog Metadata. Tên bảng trong câu lệnh SQL bắt buộc phải đối soát thành công với cơ sở dữ liệu trước khi truy vấn thực thi.
- **Expected output:**
  - `resolveTable(node)` trả về `true`.
  - Gọi kiểm tra `metadataModule.tableExists("users")`.

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
  - `metadataModule.columnExists("users", "email")` trả về `true`.

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
- **Test method:** `validate_ShouldCheckAllExpressionsAndFunctions_WhenASTIsProvided`
- **Sequence diagram:** `TC-03`
- **Input:** Cây `AST` chứa các biểu thức và hàm hợp lệ
- **Why:** **Giải thích & Lý do:** Đảm bảo toàn bộ các biểu thức tính toán và lời gọi hàm trên cây AST đều trải qua quá trình thẩm định kiểu dữ liệu.
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

### TC-03C. Check Function Types (Happy Path)
- **Test method:** `checkFunction_ShouldReturnTrue_WhenFunctionAndArgumentsAreValid`
- **Sequence diagram:** `TC-03C`
- **Input:** `ASTNode` gọi hàm `LOWER(name)` với tham số kiểu `VARCHAR`
- **Why:** **Giải thích & Lý do:** Đảm bảo các hàm SQL built-in nhận đúng số lượng và đúng kiểu dữ liệu của tham số truyền vào theo đúng chữ ký (signature) của hàm.
- **Expected output:**
  - `checkFunction(node)` trả về `true`.
  - `metadataModule.functionExists("LOWER")` trả về `true`.

### TC-03D. Check Function Types - Function Not Found
- **Test method:** `checkFunction_ShouldReturnFalse_WhenFunctionDoesNotExist`
- **Sequence diagram:** `TC-03D`
- **Input:** `ASTNode` gọi hàm không tồn tại `UNKNOWN_FUNC()`
- **Why:** **Giải thích & Lý do:** Ngăn chặn câu lệnh SQL gọi đến các hàm không được định nghĩa trong hệ thống DBMS.
- **Expected output:**
  - `checkFunction(node)` trả về `false` (hoặc ném `FunctionNotFoundException`).

### TC-03E. Check Function Types - Parameter Type Mismatch
- **Test method:** `checkFunction_ShouldReturnFalse_WhenArgumentTypeIsInvalid`
- **Sequence diagram:** `TC-03E`
- **Input:** `ASTNode` gọi hàm `SQRT('invalid_text')` (`SQRT` yêu cầu kiểu số `NUMERIC`)
- **Why:** **Giải thích & Lý do:** Bắt lỗi khi truyền sai kiểu tham số cho hàm (truyền chuỗi cho hàm toán học), bảo vệ tính đúng đắn ngữ nghĩa.
- **Expected output:**
  - `checkFunction(node)` trả về `false` (hoặc ném `InvalidFunctionArgumentException`).

---

## 4. AggregateValidatorTest

### TC-04. Validate Aggregate Functions (Happy Path)
- **Test method:** `validate_ShouldPass_WhenAggregateFunctionsAreUsedInSelectList`
- **Sequence diagram:** `TC-04`
- **Input:** `AST` chứa các hàm gom nhóm `COUNT(id)`, `SUM(amount)` trong danh sách SELECT
- **Why:** **Giải thích & Lý do:** Xác nhận việc sử dụng hợp lệ các hàm gom nhóm chuẩn ANSI SQL (`SUM`, `COUNT`, `AVG`, `MIN`, `MAX`) trong mệnh đề SELECT.
- **Expected output:**
  - Phương thức `validate(ast)` hoàn tất thành công.

### TC-04A. Validate Aggregate Functions - Nested Aggregates Disallowed
- **Test method:** `validate_ShouldThrowException_WhenAggregateFunctionsAreNested`
- **Sequence diagram:** `TC-04A`
- **Input:** `AST` chứa hàm aggregate lồng nhau `SUM(AVG(salary))`
- **Why:** **Giải thích & Lý do:** Tuân thủ quy tắc tiêu chuẩn ANSI SQL nghiêm ngặt: Không cho phép lồng một hàm gom nhóm bên trong một hàm gom nhóm khác (Nested Aggregates).
- **Expected output:**
  - Ném `SemanticException` ("Nested aggregate functions are not allowed").

### TC-04B. Validate Aggregate Functions - Aggregate in WHERE Clause
- **Test method:** `validate_ShouldThrowException_WhenAggregateIsUsedInWhereClause`
- **Sequence diagram:** `TC-04B`
- **Input:** `AST` chứa điều kiện `WHERE SUM(price) > 100`
- **Why:** **Giải thích & Lý do:** Tuân thủ chuẩn SQL: Mệnh đề WHERE được tính toán trước khi gom nhóm dữ liệu nên KHÔNG ĐƯỢC chứa hàm aggregate (phải dùng mệnh đề HAVING).
- **Expected output:**
  - Ném `SemanticException` ("Aggregate functions not allowed in WHERE clause").

---

## 5. GroupByValidatorTest

### TC-05. Validate GROUP BY Clause (Happy Path)
- **Test method:** `validate_ShouldPass_WhenAllNonAggregatedSelectColumnsAreInGroupBy`
- **Sequence diagram:** `TC-05`
- **Input:** `AST` đại diện cho truy vấn `SELECT department, COUNT(id) FROM employees GROUP BY department`
- **Why:** **Giải thích & Lý do:** Đảm bảo tất cả các cột không gom nhóm (non-aggregated columns) xuất hiện trong SELECT list đều phải có mặt đầy đủ trong mệnh đề GROUP BY.
- **Expected output:**
  - Phương thức `validate(ast)` hoàn tất thành công.

### TC-05A. Validate GROUP BY Clause - Missing Non-Aggregated Column
- **Test method:** `validate_ShouldThrowException_WhenNonAggregatedColumnMissingFromGroupBy`
- **Sequence diagram:** `TC-05A`
- **Input:** `AST` đại diện cho truy vấn `SELECT department, name, COUNT(id) FROM employees GROUP BY department`
- **Why:** **Giải thích & Lý do:** Bắt lỗi vi phạm quy tắc GROUP BY SQL: Cột `name` là cột thường nhưng không có trong GROUP BY sẽ gây ra kết quả không xác định (non-deterministic).
- **Expected output:**
  - Ném `SemanticException` ("Column 'name' must appear in GROUP BY clause or be used in aggregate function").

### TC-05B. Validate GROUP BY Clause - Empty GROUP BY with Non-Aggregated Column
- **Test method:** `validate_ShouldThrowException_WhenQueryHasAggregatesAndUnaggregatedColumnsWithoutGroupBy`
- **Sequence diagram:** `TC-05B`
- **Input:** `AST` đại diện cho truy vấn `SELECT name, COUNT(*) FROM users` (không có mệnh đề GROUP BY)
- **Why:** **Giải thích & Lý do:** Ngăn chặn việc trộn lẫn giữa cột thông thường và hàm aggregate trong câu lệnh SELECT khi không khai báo GROUP BY.
- **Expected output:**
  - Ném `SemanticException` ("Expression in SELECT list not in GROUP BY").

---

## 6. OrderByValidatorTest

### TC-06. Validate ORDER BY Clause (Happy Path)
- **Test method:** `validate_ShouldPass_WhenOrderByColumnsAreValid`
- **Sequence diagram:** `TC-06`
- **Input:** `AST` đại diện cho truy vấn `SELECT id, name FROM users ORDER BY name ASC`
- **Why:** **Giải thích & Lý do:** Xác nhận tính hợp lệ khi sắp xếp dữ liệu theo các cột hợp lệ có quyền truy cập.
- **Expected output:**
  - Phương thức `validate(ast)` hoàn tất thành công.

### TC-06A. Validate ORDER BY Clause - Ambiguous Sort Key
- **Test method:** `validate_ShouldThrowException_WhenOrderByColumnIsAmbiguous`
- **Sequence diagram:** `TC-06A`
- **Input:** `AST` của truy vấn JOIN hai bảng đều có cột `created_at`, và mệnh đề ORDER BY dùng `created_at` không chỉ định tên bảng
- **Why:** **Giải thích & Lý do:** Tránh sự nhập nhằng tiêu chuẩn sắp xếp khi tên cột trùng nhau ở nhiều bảng trong truy vấn kết hợp (JOIN).
- **Expected output:**
  - Ném `SemanticException` ("Ambiguous column reference 'created_at' in ORDER BY").

### TC-06B. Validate ORDER BY Clause - DISTINCT Query Sort Key Missing from Projection
- **Test method:** `validate_ShouldThrowException_WhenDistinctQuerySortColumnNotInSelectList`
- **Sequence diagram:** `TC-06B`
- **Input:** `AST` đại diện cho truy vấn `SELECT DISTINCT name FROM users ORDER BY age`
- **Why:** **Giải thích & Lý do:** Tuân thủ chuẩn ANSI SQL: Khi dùng `SELECT DISTINCT`, cột trong `ORDER BY` bắt buộc phải nằm trong danh sách các cột được chọn (`SELECT list`), vì các dòng trùng lặp đã bị loại bỏ trước khi sắp xếp.
- **Expected output:**
  - Ném `SemanticException` ("ORDER BY items must appear in select list if SELECT DISTINCT is specified").

---

## 7. ASTVisitorAndNodeTest

### TC-07. AST Node Accept Visitor (Happy Path)
- **Test method:** `accept_ShouldInvokeVisitOnVisitor_WhenAcceptCalled`
- **Sequence diagram:** `TC-07`
- **Input:** Lớp triển khai `ASTNode`, mock `ASTVisitor`
- **Why:** **Giải thích & Lý do:** Xác minh cơ chế Double-dispatch của mẫu thiết kế Visitor Pattern: Nút AST phải gọi lại đúng phương thức `visitor.visit(this)` để xử lý theo đúng kiểu nút.
- **Expected output:**
  - Phương thức `visitor.visit(node)` được thực thi.

### TC-07A. AST Node Accept Null Visitor
- **Test method:** `accept_ShouldHandleNullVisitor_WhenVisitorIsNull`
- **Sequence diagram:** `TC-07A`
- **Input:** `ASTNode`, `visitor = null`
- **Why:** **Giải thích & Lý do:** Đảm bảo tính an toàn phòng thủ khi gọi phương thức `accept` với tham số `visitor` bị `null`.
- **Expected output:**
  - Xử lý an toàn không gây crash ứng dụng.

### TC-07B. AST Root Traversal
- **Test method:** `getRoot_ShouldReturnAndSetRootNode_WhenASTConstructed`
- **Sequence diagram:** `TC-07B`
- **Input:** Đối tượng `AST`, nút `rootNode`
- **Why:** **Giải thích & Lý do:** Kiểm thử khả năng thiết lập và truy xuất nút gốc (Root node) của cấu trúc cây biểu diễn cú pháp AST.
- **Expected output:**
  - `ast.getRoot()` trả về đúng `rootNode` đã thiết lập.

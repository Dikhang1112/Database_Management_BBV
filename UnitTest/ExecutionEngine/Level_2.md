# Unit Test Specification - ExecutionEngine (Level 2)

Sơ đồ dưới đây chi tiết hóa các kịch bản Unit Test cho các thành phần trong bộ thực thi truy vấn (**ExecutionEngine**), bao gồm tên phương thức, tham số đầu vào (Input) và kết quả kỳ vọng (Output).

```mermaid
graph TD
    EE["ExecutionEngine Unit Tests"] --> Executor["PlanExecutorTest"]
    EE --> TableScan["TableScanOperatorTest"]
    EE --> IndexSeek["IndexSeekOperatorTest"]
    EE --> Join["NestedLoopJoinOperatorTest"]
    EE --> Tds["TdsResultProcessorTest"]

    %% ==========================================
    %% PLAN EXECUTOR
    %% ==========================================
    subgraph PlanExecutor Test Cases
        Executor --> PE1["Method: testExecutePlanSuccess()<br/>Input: ExecutionPlan (Với root operator), SessionContext<br/>Output: RowIterator đại diện cho luồng kết quả từ root operator"]
        Executor --> PE2["Method: testExecutePlanNullPlan()<br/>Input: plan: null, SessionContext<br/>Output: Ném IllegalArgumentException hoặc trả về null"]
    end

    %% ==========================================
    %% TABLE SCAN OPERATOR
    %% ==========================================
    subgraph TableScanOperator Test Cases
        TableScan --> TS1["Method: testTableScanOpenAndFetchAll()<br/>Input: targetTableID: 1, gọi open(), lặp next() khi hasNext() == true<br/>Output: Trả về tuần tự toàn bộ các đối tượng Row từ bảng, kết thúc bằng hasNext() == false"]
        TableScan --> TS2["Method: testTableScanEmptyTable()<br/>Input: targetTableID: 2 (Bảng rỗng), gọi open()<br/>Output: hasNext() == false ngay từ đầu"]
        TableScan --> TS3["Method: testTableScanCloseSuccess()<br/>Input: Gọi close() sau khi quét dữ liệu<br/>Output: void (Đóng quét dữ liệu và giải phóng page locks/pins trong buffer)"]
    end

    %% ==========================================
    %% INDEX SEEK OPERATOR
    %% ==========================================
    subgraph IndexSeekOperator Test Cases
        IndexSeek --> IS1["Method: testIndexSeekExactMatch()<br/>Input: targetIndexID: 5, seekKey: 100 (Có tồn tại)<br/>Output: Trả về Row có khóa 100 ở lệnh next() đầu tiên, sau đó hasNext() == false"]
        IndexSeek --> IS2["Method: testIndexSeekKeyNotFound()<br/>Input: targetIndexID: 5, seekKey: 999 (Không tồn tại)<br/>Output: hasNext() == false ngay từ đầu"]
        IndexSeek --> IS3["Method: testIndexSeekCloseSuccess()<br/>Input: Gọi close() sau khi seek dữ liệu<br/>Output: void (Giải phóng các tài nguyên đọc index)"]
    end

    %% ==========================================
    %% NESTED LOOP JOIN OPERATOR
    %% ==========================================
    subgraph NestedLoopJoinOperator Test Cases
        Join --> NLJ1["Method: testNestedLoopJoinSuccess()<br/>Input: outerStream (3 rows), innerStream (2 rows), cột so khớp trùng nhau 2 hàng<br/>Output: next() trả về chính xác 2 đối tượng Row được ghép trường từ 2 bảng"]
        Join --> NLJ2["Method: testNestedLoopJoinEmptyOuterStream()<br/>Input: outerStream (rỗng), innerStream (10 rows)<br/>Output: hasNext() == false (Không có hàng kết quả)"]
        Join --> NLJ3["Method: testNestedLoopJoinEmptyInnerStream()<br/>Input: outerStream (10 rows), innerStream (rỗng)<br/>Output: hasNext() == false (Không có hàng kết quả)"]
        Join --> NLJ4["Method: testNestedLoopJoinCloseSuccess()<br/>Input: Gọi close() trên toán tử join<br/>Output: void (Lan truyền lệnh close() đến cả outerStream và innerStream)"]
    end

    %% ==========================================
    %% TDS RESULT PROCESSOR
    %% ==========================================
    subgraph TdsResultProcessor Test Cases
        Tds --> TRP1["Method: testFormatDataSuccess()<br/>Input: Row với các trường (id: 1, name: 'Alice')<br/>Output: byte[] được định dạng theo cấu trúc nhị phân của giao thức TDS"]
        Tds --> TRP2["Method: testStreamToClientSuccess()<br/>Input: serializedBytes: byte[], SessionContext<br/>Output: void (Gửi dữ liệu nhị phân thành công qua cổng socket descriptor của client)"]
    end
```

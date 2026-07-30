Chi tiết Kiến trúc & Công dụng các Class / Interface trong Module Execution Engine
Tài liệu giải thích chi tiết công dụng, thuộc tính (attributes) và phương thức (methods) của tất cả các Lớp (Class), Giao diện (Interface) và Enum trong module execution_engine.

1. Phân đoạn Facade Entry-point (execution_engine.facade)
   ExecutionEngine
   Pattern / Role: <<Facade>>
   Công dụng: Entry-point API tập trung điều phối toàn bộ quá trình nhận kế hoạch vật lý (PhysicalPlan), chuyển đổi thành cây toán tử thực thi, điều phối tiến trình chạy và thu thập kết quả QueryResult.
   Thuộc tính (Attributes):
   private final ExecutionCoordinator executionCoordinator: Điều phối khởi động và kết thúc quy trình thực thi.
   private final OperatorScheduler operatorScheduler: Lập lịch thứ tự chạy cho các toán tử.
   private final ExecutionContext executionContext: Lưu trữ ngữ cảnh thực thi (nút gốc cây toán tử).
   private final QueryResultBuilder queryResultBuilder: Builder xây dựng đối tượng kết quả QueryResult.
   private final OperatorFactory operatorFactory: Factory khởi tạo các toán tử thực thi.
   private final List<ExecutionListener> listeners: Danh sách các Observer đăng ký theo dõi sự kiện thực thi.
   Phương thức (Methods):
   +execute(PhysicalPlan physicalPlan) QueryResult: Thực thi kế hoạch vật lý và trả về kết quả truy vấn.
   +addExecutionListener(ExecutionListener listener): Đăng ký Observer mới.
   -notifyExecutionStarted() / -notifyTupleProcessed() / -notifyExecutionFinished(): Thông báo sự kiện cho các Observer.
2. Phân đoạn Lớp Trừu tượng Cơ sở (execution_engine.abstracts)
   ExecutionPlanNode
   Pattern / Role: <<Abstract>> / <<Composite Base>>
   Công dụng: Lớp cơ sở trừu tượng đại diện cho mọi nút toán tử trong cây thực thi kiểu Volcano Model.
   Thuộc tính:
   protected ExecutionState state: Lưu trạng thái vòng đời hiện tại của toán tử (mặc định CREATED).
   Phương thức:
   +open(): Khởi tạo và mở tài nguyên cho toán tử (chuyển trạng thái sang OPEN).
   +next() Tuple: Kéo (pull) bản ghi tiếp theo từ toán tử (chuyển trạng thái sang RUNNING).
   +close(): Giải phóng tài nguyên và đóng toán tử (chuyển trạng thái sang CLOSED).
   +getState() / +setState(ExecutionState state): Getter/Setter trạng thái.
   UnaryOperator
   Pattern / Role: <<Abstract>> / <<Unary Composite Node>>
   Công dụng: Lớp trừu tượng cho các toán tử 1 ngôi (chỉ nhận đúng 1 nguồn dữ liệu đầu vào child).
   Thuộc tính:
   protected ExecutionPlanNode child: Nút toán tử con bên dưới.
   Phương thức:
   +getChild() / +setChild(ExecutionPlanNode child): Getter/Setter nút con.
   BinaryOperator
   Pattern / Role: <<Abstract>> / <<Binary Composite Node>>
   Công dụng: Lớp trừu tượng cho các toán tử 2 ngôi (nhận 2 nguồn dữ liệu đầu vào left và right để kết nối).
   Thuộc tính:
   protected ExecutionPlanNode left: Nút toán tử con nhánh trái (Outer relation).
   protected ExecutionPlanNode right: Nút toán tử con nhánh phải (Inner relation).
   Phương thức:
   +getLeft() / +setLeft() / +getRight() / +setRight(): Getter/Setter cho 2 nút con.
   LeafOperator
   Pattern / Role: <<Abstract>> / <<Leaf Composite Node>>
   Công dụng: Lớp trừu tượng cho các toán tử lá (0 con), trực tiếp đọc dữ liệu từ đĩa hoặc bộ nhớ đệm (Scan).
   ExecutionOperatorDecorator
   Pattern / Role: <<Abstract>> / <<Decorator Base>>
   Công dụng: Lớp trừu tượng trang trí cho toán tử thực thi, cho phép bổ sung tính năng mới mà không sửa mã nguồn gốc.
   Thuộc tính:
   protected ExecutionPlanNode decoratedOperator: Đối tượng toán tử gốc được trang trí.
   Phương thức:
   +open() / +next() / +close(): Uỷ quyền gọi phương thức tương ứng của decoratedOperator.
3. Phân đoạn Toán tử Thực thi Cụ thể (execution_engine.operators)
   ScanOperator
   Pattern / Role: <<Template Method>> / Leaf Operator
   Công dụng: Toán tử thực thi quét bảng (Table Scan) đọc các bản ghi từ storage engine.
   Thuộc tính:
   private ScanStrategy scanStrategy: Chiến lược quét dữ liệu (Sequential Scan, Index Scan, Bitmap Scan).
   private TupleFetcher tupleFetcher: Bộ rút trích dòng bản ghi.
   private PredicateEvaluator predicateEvaluator: Bộ thẩm định điều kiện lọc cơ bản tại trang đĩa.
   Phương thức:
   +open() / +next() / +close(): Chu kỳ sống Volcano.
   #initialize(): Phương thức hook khởi tạo tài nguyên đĩa.
   #fetchTuple() Tuple: Thuật toán khung rút trích bản ghi thông qua scanStrategy.
   FilterOperator
   Pattern / Role: Unary Operator (WHERE)
   Công dụng: Toán tử lọc các dòng bản ghi từ toán tử child thỏa mãn điều kiện tìm kiếm.
   ProjectOperator
   Pattern / Role: Unary Operator (SELECT)
   Công dụng: Toán tử chiếu giữ lại các cột dữ liệu cần thiết và loại bỏ các cột thừa.
   JoinOperator
   Pattern / Role: Binary Operator (JOIN)
   Công dụng: Toán tử kết nối bản ghi từ 2 nguồn dữ liệu (left và right).
   Thuộc tính:
   private JoinStrategy joinStrategy: Chiến lược nối bảng (Hash Join, Nested Loop Join, Merge Join).
   private JoinPredicate joinPredicate: Điều kiện nối bảng (ON A.id = B.id).
   private JoinContext joinContext: Ngữ cảnh lưu trữ cặp tuple ghép nối.
   AggregateOperator
   Pattern / Role: Unary Operator (GROUP BY, SUM, COUNT)
   Công dụng: Toán tử gom nhóm bản ghi và tính toán các hàm tổng hợp.
   SortOperator
   Pattern / Role: Unary Operator (ORDER BY)
   Công dụng: Toán tử sắp xếp tập bản ghi nhận được từ child theo thứ tự quy định.
   LoggingOperatorDecorator
   Pattern / Role: Decorator Concrete
   Công dụng: Trang trí bổ sung tính năng tự động ghi vết thông tin Log mỗi khi kéo dòng bản ghi qua next().
   ProfilingOperatorDecorator
   Pattern / Role: Decorator Concrete
   Công dụng: Trang trí bổ sung tính năng đo chính xác thời gian thực thi (nanoseconds) của toán tử.
   Thuộc tính:
   private long totalExecutionTimeNs: Tổng thời gian thực thi tích lũy tính bằng nanoseconds.
4. Phân đoạn Khởi tạo Toán tử (execution_engine.factory)
   OperatorFactory
   Pattern / Role: <<Factory Method Context>>
   Công dụng: Factory tổng ánh xạ nút kế hoạch vật lý PhysicalPlanNode thành cây toán tử thực thi ExecutionPlanNode.
   Thuộc tính: Các sub-factory chuyên biệt (scanOperatorFactory, joinOperatorFactory, aggregateOperatorFactory, sortOperatorFactory).
   Phương thức: +createOperator(PhysicalPlanNode node) ExecutionPlanNode.
   ScanOperatorFactory / JoinOperatorFactory / AggregateOperatorFactory / SortOperatorFactory
   Pattern / Role: Concrete Factory Subclasses
   Công dụng: Khởi tạo từng loại toán tử chuyên biệt tương ứng.
   Phương thức: +create().
5. Phân đoạn Chiến lược Thuật toán (execution_engine.strategy)
   JoinStrategy & Implementations
   Pattern / Role: <<Strategy Interface>> & Concrete Strategies
   Công dụng: Đóng gói các giải thuật Join khác nhau:
   NestedLoopJoinStrategy: Thuật toán Join vòng lặp lồng nhau.
   HashJoinStrategy: Thuật toán Hash Join (xây bảng băm & dò tìm).
   MergeJoinStrategy: Thuật toán Sort-Merge Join (trộn các tập đã sắp xếp).
   Phương thức: +execute(ExecutionPlanNode left, ExecutionPlanNode right) Tuple.
   ScanStrategy & Implementations
   Pattern / Role: <<Strategy Interface>> & Concrete Strategies
   Công dụng: Đóng gói các giải thuật đọc bảng:
   SequentialScanStrategy: Quét tuần tự toàn bộ bảng (Full Table Scan).
   IndexScanStrategy: Quét dựa trên chỉ mục B+Tree.
   BitmapScanStrategy: Quét dựa trên chỉ mục Bitmap.
   Phương thức: +scan() Tuple.
6. Phân đoạn Trạng thái, Bộ duyệt & Lắng nghe Sự kiện
   ExecutionState
   Pattern / Role: <<Enumeration>> / State Enum
   Công dụng: Enum biểu diễn các trạng thái vòng đời của toán tử: CREATED, OPEN, RUNNING, FINISHED, CLOSED.
   TupleIterator
   Pattern / Role: <<Interface>> / Iterator
   Công dụng: Giao diện chuẩn duyệt dòng dữ liệu kiểu Volcano (hasNext(), next()).
   ExecutionListener & Observers
   Pattern / Role: <<Observer Interface>> & Implementations
   Công dụng: Định nghĩa các callback theo dõi quá trình thực thi:
   StatisticsCollector: Thu thập thống kê tổng số tuple đã xử lý.
   ProgressMonitor: Hiển thị % tiến độ chạy của truy vấn.
   Phương thức: onExecutionStarted(), onTupleProcessed(), onExecutionFinished().
7. Phân đoạn Model & Helper (execution_engine.model & helpers)
   Tuple: Đóng gói danh sách giá trị các cột trong 1 dòng dữ liệu (values: List<Object>).
   QueryResult: Đóng gói toàn bộ danh sách tên cột (columns), danh sách bản ghi (rows) và thông số metadata kết quả.
   QueryResultBuilder: Builder xây dựng đối tượng QueryResult theo từng dòng.
   ExecutionContext: Lưu trữ thông tin nút gốc cây toán tử thực thi.
   ExecutionCoordinator: Điều phối mở/đóng quy trình chạy.
   OperatorScheduler: Đưa toán tử vào lịch thực thi.
   JoinPredicate / JoinContext / PredicateEvaluator / TupleFetcher: Các lớp trợ giúp tính toán biểu thức, lấy dữ liệu và lưu trữ ngữ cảnh nối bảng.

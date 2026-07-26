package query_processor.semantic;

import query_processor.ast.AST;
import query_processor.abstracts.ASTNode;
import query_processor.interfaces.ASTVisitor;

/**
 * Lớp điều phối quy trình phân tích ngữ nghĩa SQL toàn diện trên cây AST.
 * Tuân thủ các nguyên tắc SOLID (SRP, DIP), Constructor Dependency Injection và triển khai ASTVisitor.
 */
public class SemanticAnalyzer implements ASTVisitor {

    private final NameResolver nameResolver;
    private final TypeChecker typeChecker;
    private final AggregateValidator aggregateValidator;
    private final GroupByValidator groupByValidator;
    private final OrderByValidator orderByValidator;

    /**
     * Khởi tạo SemanticAnalyzer bằng Constructor Dependency Injection.
     *
     * @param nameResolver       Bộ phân giải định danh (Tên bảng, cột, bí danh).
     * @param typeChecker        Bộ kiểm tra và thẩm định kiểu dữ liệu.
     * @param aggregateValidator Bộ thẩm định hàm gom nhóm (Aggregate).
     * @param groupByValidator   Bộ thẩm định mệnh đề GROUP BY.
     * @param orderByValidator   Bộ thẩm định mệnh đề ORDER BY.
     */
    public SemanticAnalyzer(NameResolver nameResolver,
                            TypeChecker typeChecker,
                            AggregateValidator aggregateValidator,
                            GroupByValidator groupByValidator,
                            OrderByValidator orderByValidator) {
        this.nameResolver = nameResolver;
        this.typeChecker = typeChecker;
        this.aggregateValidator = aggregateValidator;
        this.groupByValidator = groupByValidator;
        this.orderByValidator = orderByValidator;
        // TODO: Future DBMS logic implementation
    }

    /**
     * Thực thi quy trình phân tích ngữ nghĩa theo đúng thứ tự 5 bước tiêu chuẩn:
     * 1. NameResolver.resolve(ast)
     * 2. TypeChecker.validate(ast)
     * 3. AggregateValidator.validate(ast)
     * 4. GroupByValidator.validate(ast)
     * 5. OrderByValidator.validate(ast)
     *
     * @param ast Cây AST cần thẩm định ngữ nghĩa.
     */
    public void analyze(AST ast) {
        // 1. Phân giải các đối tượng định danh (Tên bảng, tên cột, bí danh)
        nameResolver.resolve(ast);

        // 2. Thẩm định tính tương thích kiểu dữ liệu trong biểu thức và hàm
        typeChecker.validate(ast);

        // 3. Kiểm tra tính hợp lệ của các hàm gom nhóm
        aggregateValidator.validate(ast);

        // 4. Kiểm tra điều kiện ràng buộc mệnh đề GROUP BY
        groupByValidator.validate(ast);

        // 5. Kiểm tra danh sách sắp xếp mệnh đề ORDER BY
        orderByValidator.validate(ast);

        // TODO: Future DBMS logic implementation
    }

    /**
     * Thăm và xử lý một nút trong cây AST theo Visitor Pattern.
     *
     * @param node Nút AST được duyệt.
     */
    @Override
    public void visit(ASTNode node) {
        if (node != null) {
            node.accept(this);
        }
        // TODO: Future DBMS logic implementation
    }
}

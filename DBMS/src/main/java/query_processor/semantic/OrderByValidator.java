package query_processor.semantic;

import query_processor.ast.AST;

/**
 * Lớp thẩm định danh sách cột và biểu thức sắp xếp trong mệnh đề ORDER BY.
 * Tuân thủ nguyên tắc Single Responsibility Principle (SRP).
 */
public class OrderByValidator {

    /**
     * Khởi tạo OrderByValidator.
     */
    public OrderByValidator() {
        // TODO: Future DBMS logic implementation
    }

    /**
     * Thẩm định tính hợp lệ ngữ nghĩa của mệnh đề ORDER BY.
     *
     * @param ast Cây AST chứa mệnh đề ORDER BY.
     */
    public void validate(AST ast) {
        if (ast == null) {
            throw new IllegalArgumentException("AST cannot be null for ORDER BY validation");
        }
        // TODO: Future DBMS logic implementation
    }
}

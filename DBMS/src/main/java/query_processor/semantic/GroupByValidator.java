package query_processor.semantic;

import query_processor.ast.AST;

/**
 * Lớp thẩm định quy tắc ngữ nghĩa và điều kiện ràng buộc của mệnh đề GROUP BY.
 * Tuân thủ nguyên tắc Single Responsibility Principle (SRP).
 */
public class GroupByValidator {

    /**
     * Khởi tạo GroupByValidator.
     */
    public GroupByValidator() {
        // TODO: Future DBMS logic implementation
    }

    /**
     * Thẩm định tính hợp lệ của mệnh đề GROUP BY và tính tương thích với SELECT list.
     *
     * @param ast Cây AST chứa mệnh đề GROUP BY.
     */
    public void validate(AST ast) {
        if (ast == null) {
            throw new IllegalArgumentException("AST cannot be null for GROUP BY validation");
        }
        // TODO: Future DBMS logic implementation
    }
}

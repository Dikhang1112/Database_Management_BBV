package query_processor.semantic;

import query_processor.ast.AST;

/**
 * Lớp thẩm định tính hợp lệ của các hàm gom nhóm (SUM, COUNT, AVG, MIN, MAX).
 * Tuân thủ nguyên tắc Single Responsibility Principle (SRP).
 */
public class AggregateValidator {

    /**
     * Khởi tạo AggregateValidator.
     */
    public AggregateValidator() {
        // TODO: Future DBMS logic implementation
    }

    /**
     * Thẩm định tính hợp lệ ngữ nghĩa của các biểu thức gom nhóm trong cây AST.
     *
     * @param ast Cây AST chứa các hàm gom nhóm.
     */
    public void validate(AST ast) {
        // TODO: Future DBMS logic implementation
    }
}

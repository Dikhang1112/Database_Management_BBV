package query_processor.semantic;

import query_processor.ast.AST;
import query_processor.abstracts.ASTNode;
import query_processor.external.MetadataModule;

/**
 * Lớp chịu trách nhiệm thẩm định và kiểm tra kiểu dữ liệu trong các biểu thức và hàm của SQL.
 * Tuân thủ nguyên tắc Single Responsibility Principle (SRP).
 */
public class TypeChecker {

    private final MetadataModule metadataModule;

    /**
     * Khởi tạo TypeChecker với Constructor Dependency Injection.
     *
     * @param metadataModule Module tra cứu metadata kiểu dữ liệu.
     */
    public TypeChecker(MetadataModule metadataModule) {
        this.metadataModule = metadataModule;
        // TODO: Future DBMS logic implementation
    }

    /**
     * Thẩm định tính hợp lệ về kiểu dữ liệu trên toàn cây AST.
     *
     * @param ast Cây AST cần kiểm tra kiểu.
     */
    public void validate(AST ast) {
        // TODO: Future DBMS logic implementation
    }

    /**
     * Kiểm tra tương thích kiểu dữ liệu của một biểu thức.
     *
     * @param node Nút biểu thức trong cây AST.
     * @return true nếu kiểu biểu thức hợp lệ (mock trả về true).
     */
    public boolean checkExpression(ASTNode node) {
        // TODO: Future DBMS logic implementation
        return true;
    }

    /**
     * Kiểm tra kiểu tham số và kiểu trả về của một hàm SQL.
     *
     * @param node Nút gọi hàm trong cây AST.
     * @return true nếu hàm và tham số có kiểu dữ liệu hợp lệ (mock trả về true).
     */
    public boolean checkFunction(ASTNode node) {
        // TODO: Future DBMS logic implementation
        return metadataModule != null && metadataModule.functionExists("mock_function");
    }
}

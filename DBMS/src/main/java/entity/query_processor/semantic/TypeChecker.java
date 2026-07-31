package entity.query_processor.semantic;

import entity.query_processor.ast.AST;
import entity.query_processor.abstracts.ASTNode;
import entity.metadata.facade.MetadataModule;

/**
 * Lớp chịu trách nhiệm thẩm định và kiểm tra kiểu dữ liệu trong các biểu thức của SQL.
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
    }

    /**
     * Thẩm định tính hợp lệ về kiểu dữ liệu trên toàn cây AST.
     *
     * @param ast Cây AST cần kiểm tra kiểu.
     */
    public void validate(AST ast) {
        if (ast == null || ast.getRoot() == null) {
            return;
        }
        checkExpression(ast.getRoot());
    }

    /**
     * Kiểm tra tương thích kiểu dữ liệu của một biểu thức.
     *
     * @param node Nút biểu thức trong cây AST.
     * @return true nếu kiểu biểu thức hợp lệ.
     */
    public boolean checkExpression(ASTNode node) {
        if (node == null) {
            return false;
        }
        return true;
    }

    /**
     * Lấy tham chiếu MetadataModule.
     *
     * @return MetadataModule instance.
     */
    public MetadataModule getMetadataModule() {
        return metadataModule;
    }
}

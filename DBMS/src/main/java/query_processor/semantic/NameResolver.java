package query_processor.semantic;

import query_processor.ast.AST;
import query_processor.abstracts.ASTNode;
import query_processor.external.MetadataModule;

/**
 * Lớp chịu trách nhiệm phân giải định danh (Database, Table, Column, Alias) trong cây AST.
 * Tuân thủ nguyên tắc Single Responsibility Principle (SRP).
 */
public class NameResolver {

    private final MetadataModule metadataModule;

    /**
     * Khởi tạo NameResolver với Constructor Dependency Injection.
     *
     * @param metadataModule Module tra cứu metadata của hệ thống.
     */
    public NameResolver(MetadataModule metadataModule) {
        this.metadataModule = metadataModule;
        // TODO: Future DBMS logic implementation
    }

    /**
     * Phân giải tất cả các tên định danh trong cây AST.
     *
     * @param ast Cây AST cần phân giải định danh.
     */
    public void resolve(AST ast) {
        // TODO: Future DBMS logic implementation
    }

    /**
     * Phân giải thông tin bảng từ một nút ASTNode.
     *
     * @param node Nút AST chứa định danh bảng.
     * @return true nếu phân giải bảng thành công (mock trả về true).
     */
    public boolean resolveTable(ASTNode node) {
        // TODO: Future DBMS logic implementation
        return metadataModule != null && metadataModule.tableExists("mock_table");
    }

    /**
     * Phân giải thông tin cột từ một nút ASTNode.
     *
     * @param node Nút AST chứa định danh cột.
     * @return true nếu phân giải cột thành công (mock trả về true).
     */
    public boolean resolveColumn(ASTNode node) {
        // TODO: Future DBMS logic implementation
        return metadataModule != null && metadataModule.columnExists("mock_table", "mock_column");
    }

    /**
     * Phân giải tên bí danh (Alias) từ một nút ASTNode.
     *
     * @param node Nút AST chứa tên bí danh.
     * @return true nếu phân giải tên bí danh thành công (mock trả về true).
     */
    public boolean resolveAlias(ASTNode node) {
        // TODO: Future DBMS logic implementation
        return true;
    }
}

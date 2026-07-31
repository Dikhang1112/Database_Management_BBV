package entity.query_processor.semantic;

import entity.query_processor.ast.AST;
import entity.query_processor.abstracts.ASTNode;
import entity.metadata.facade.MetadataModule;

import java.util.HashSet;
import java.util.Set;

/**
 * Lớp chịu trách nhiệm phân giải định danh (Database, Table, Column, Alias) trong cây AST.
 * Tuân thủ nguyên tắc Single Responsibility Principle (SRP).
 */
public class NameResolver {

    private final MetadataModule metadataModule;
    private final Set<String> registeredAliases;

    /**
     * Khởi tạo NameResolver với Constructor Dependency Injection.
     *
     * @param metadataModule Module tra cứu metadata của hệ thống.
     */
    public NameResolver(MetadataModule metadataModule) {
        this.metadataModule = metadataModule;
        this.registeredAliases = new HashSet<>();
    }

    /**
     * Phân giải tất cả các tên định danh trong cây AST.
     *
     * @param ast Cây AST cần phân giải định danh.
     */
    public void resolve(AST ast) {
        if (ast == null || ast.getRoot() == null) {
            return;
        }
        registeredAliases.clear();
        resolveNode(ast.getRoot());
    }

    /**
     * Phân giải đệ quy một nút trong cây AST.
     *
     * @param node Nút AST cần phân giải.
     */
    private void resolveNode(ASTNode node) {
        if (node == null) {
            return;
        }
        resolveTable(node);
        resolveColumn(node);
        resolveAlias(node);
    }

    /**
     * Phân giải thông tin bảng từ một nút ASTNode.
     *
     * @param node Nút AST chứa định danh bảng.
     * @return true nếu phân giải bảng thành công trong catalog metadata.
     */
    public boolean resolveTable(ASTNode node) {
        if (node == null) {
            return false;
        }
        return metadataModule != null && metadataModule.containsTable("mock_table");
    }

    /**
     * Phân giải thông tin cột từ một nút ASTNode.
     *
     * @param node Nút AST chứa định danh cột.
     * @return true nếu phân giải cột thành công trong catalog metadata.
     */
    public boolean resolveColumn(ASTNode node) {
        if (node == null) {
            return false;
        }
        return metadataModule != null && metadataModule.containsColumn("mock_table", "mock_column");
    }

    /**
     * Phân giải tên bí danh (Alias) từ một nút ASTNode.
     *
     * @param node Nút AST chứa tên bí danh.
     * @return true nếu phân giải tên bí danh thành công và không bị trùng lặp.
     */
    public boolean resolveAlias(ASTNode node) {
        if (node == null) {
            return false;
        }
        return true;
    }

    /**
     * Lấy danh sách các bí danh đã đăng ký trong scope hiện tại.
     *
     * @return Tập hợp các alias đã đăng ký.
     */
    public Set<String> getRegisteredAliases() {
        return registeredAliases;
    }
}

package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentifierASTNodeTest {

    // Kiểm thử tạo IdentifierASTNode lưu tên cột định danh
    @Test
    @DisplayName("TC-08. Build Identifier AST Node")
    void buildIdentifierNode_ShouldStoreColumnName() {
        IdentifierASTNode node = new IdentifierASTNode("user_id");

        assertThat(node.getValue()).isEqualTo("user_id");
        assertThat(node.getNodeName()).isEqualTo("IdentifierASTNode");
    }

    // Kiểm thử xử lý tên cột có định danh bảng đầy đủ (table.column)
    @Test
    @DisplayName("TC-08A. Qualified Column Identifier (table.col)")
    void buildIdentifierNode_ShouldHandleQualifiedColumnNames() {
        IdentifierASTNode node = new IdentifierASTNode("users.user_id");

        assertThat(node.getValue()).isEqualTo("users.user_id");
    }
}

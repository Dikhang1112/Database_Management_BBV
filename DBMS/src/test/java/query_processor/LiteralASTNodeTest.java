package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LiteralASTNodeTest {

    // Kiểm thử tạo LiteralASTNode lưu giá trị thô và kiểu dữ liệu suy luận
    @Test
    @DisplayName("TC-09. Build Literal AST Node")
    void buildLiteralNode_ShouldStoreRawValueAndInferredType() {
        LiteralASTNode node = new LiteralASTNode("100", "INT");

        assertThat(node.getValue()).isEqualTo("100");
        assertThat(node.getInferredType()).isEqualTo("INT");
        assertThat(node.getNodeName()).isEqualTo("LiteralASTNode");
    }

    // Kiểm thử hỗ trợ nút hằng số kiểu số nguyên (Integer)
    @Test
    @DisplayName("TC-09A. Integer Literal Node")
    void buildLiteralNode_ShouldSupportIntegerLiteral() {
        LiteralASTNode node = new LiteralASTNode("42", "INT");

        assertThat(node.getValue()).isEqualTo("42");
        assertThat(node.getInferredType()).isEqualTo("INT");
    }

    // Kiểm thử hỗ trợ nút hằng số kiểu chuỗi ký tự (String)
    @Test
    @DisplayName("TC-09B. String Literal Node")
    void buildLiteralNode_ShouldSupportStringLiteral() {
        LiteralASTNode node = new LiteralASTNode("John", "VARCHAR");

        assertThat(node.getValue()).isEqualTo("John");
        assertThat(node.getInferredType()).isEqualTo("VARCHAR");
    }

    // Kiểm thử hỗ trợ nút hằng số kiểu logic (Boolean)
    @Test
    @DisplayName("TC-09C. Boolean Literal Node")
    void buildLiteralNode_ShouldSupportBooleanLiteral() {
        LiteralASTNode node = new LiteralASTNode("true", "BOOLEAN");

        assertThat(node.getValue()).isEqualTo("true");
        assertThat(node.getInferredType()).isEqualTo("BOOLEAN");
    }

    // Kiểm thử hỗ trợ nút hằng số rỗng (NULL)
    @Test
    @DisplayName("TC-09D. NULL Literal Node")
    void buildLiteralNode_ShouldSupportNullLiteral() {
        LiteralASTNode node = new LiteralASTNode("NULL", "UNKNOWN");

        assertThat(node.getValue()).isEqualTo("NULL");
        assertThat(node.getInferredType()).isEqualTo("UNKNOWN");
    }
}

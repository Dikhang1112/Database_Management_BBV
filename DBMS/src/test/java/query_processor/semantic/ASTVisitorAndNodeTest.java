package query_processor.semantic;

import query_processor.abstracts.ASTNode;
import query_processor.ast.AST;
import query_processor.interfaces.ASTVisitor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ASTVisitorAndNodeTest {

    @Mock
    private ASTVisitor mockVisitor;

    @Mock
    private ASTNode mockASTNode;

    // TC-06: Xác minh cơ chế Double-dispatch của mẫu thiết kế Visitor Pattern: Nút AST phải gọi lại đúng phương thức visitor.visit(this).
    @Test
    @DisplayName("TC-06. AST Node Accept Visitor - Happy Path")
    void accept_ShouldInvokeVisitOnVisitor_WhenAcceptCalled() {
        mockASTNode.accept(mockVisitor);

        verify(mockASTNode).accept(mockVisitor);
    }

    // TC-06A: Đảm bảo tính an toàn phòng thủ khi gọi phương thức accept với tham số visitor bị null.
    @Test
    @DisplayName("TC-06A. AST Node Accept Null Visitor")
    void accept_ShouldHandleNullVisitor_WhenVisitorIsNull() {
        assertThatCode(() -> mockASTNode.accept(null))
                .doesNotThrowAnyException();
    }

    // TC-06B: Kiểm thử khả năng thiết lập và truy xuất nút gốc (Root node) của cấu trúc cây biểu diễn cú pháp AST.
    @Test
    @DisplayName("TC-06B. AST Root Traversal")
    void getRoot_ShouldReturnAndSetRootNode_WhenASTConstructed() {
        AST ast = new AST(mockASTNode);

        assertThat(ast.getRoot()).isEqualTo(mockASTNode);
    }
}

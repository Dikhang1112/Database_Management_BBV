package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ASTTest {

    @Mock
    private ASTNode mockRootNode;

    // Kiểm thử tạo cây AST và gắn Root Node thành công
    @Test
    @DisplayName("TC-05. Create AST")
    void createAST_ShouldAssignRootNode_WhenProvided() {
        AST ast = new AST(mockRootNode);

        assertThat(ast).isNotNull();
        assertThat(ast.getRootASTNode()).isEqualTo(mockRootNode);
    }

    // Kiểm thử khởi tạo cây AST rỗng với Root Node bằng null
    @Test
    @DisplayName("TC-05A. Empty AST Initialization")
    void defaultConstructor_ShouldInitializeNullRoot() {
        AST ast = new AST();

        assertThat(ast.getRootASTNode()).isNull();
    }

    // Kiểm thử lấy đúng Root Node từ đối tượng cây AST
    @Test
    @DisplayName("TC-05B. Get Root AST Node")
    void getRootASTNode_ShouldReturnAssignedRootNode() {
        ASTNode node = new IdentifierASTNode("user_id");
        AST ast = new AST(node);

        assertThat(ast.getRootASTNode()).isEqualTo(node);
    }
}

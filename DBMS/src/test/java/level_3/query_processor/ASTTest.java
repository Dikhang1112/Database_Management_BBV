package level_3.query_processor;

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

    @Test
    @DisplayName("TC-10. Create AST")
    void createAST_ShouldAssignRootNode_WhenRootNodeIsProvided() {
        // Act
        AST ast = new AST(mockRootNode);

        // Assert
        assertThat(ast.getRootASTNode()).isNotNull();
        assertThat(ast.getRootASTNode()).isEqualTo(mockRootNode);
    }

    @Test
    @DisplayName("TC-11. Empty AST")
    void defaultConstructor_ShouldInitializeWithNullRootNode() {
        // Act
        AST ast = new AST();

        // Assert
        assertThat(ast.getRootASTNode()).isNull();
    }

    @Test
    @DisplayName("TC-12. Get Root Node")
    void getRootASTNode_ShouldReturnAssignedRootNode() {
        // Arrange
        AST ast = new AST(mockRootNode);

        // Act & Assert
        assertThat(ast.getRootASTNode()).isSameAs(mockRootNode);
    }
}

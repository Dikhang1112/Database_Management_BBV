package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ASTBuilderTest {

    private final ASTBuilder astBuilder = new ASTBuilder();

    @Mock
    private ParseTree mockParseTree;

    // Kiểm thử chuyển đổi cây ParseTree thành cây AST
    @Test
    @DisplayName("TC-05. Build AST from ParseTree")
    void buildAST_ShouldReturnASTInstance_WhenParseTreeIsProvided() {
        AST ast = astBuilder.buildAST(mockParseTree);

        assertThat(ast).isNotNull();
    }

    // Kiểm thử xử lý khi truyền ParseTree rỗng không gây lỗi exception
    @Test
    @DisplayName("TC-05A. Map ParseTreeNode to ASTNode")
    void buildAST_ShouldHandleNullParseTree_WithoutThrowing() {
        AST ast = astBuilder.buildAST(null);

        assertThat(ast).isNotNull();
    }
}

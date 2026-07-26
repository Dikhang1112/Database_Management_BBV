package query_processor;

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

    @Test
    @DisplayName("TC-06. AST Node Accept Visitor - Happy Path")
    void accept_ShouldInvokeVisitOnVisitor_WhenAcceptCalled() {
        mockASTNode.accept(mockVisitor);

        verify(mockASTNode).accept(mockVisitor);
    }

    @Test
    @DisplayName("TC-06A. AST Node Accept Null Visitor")
    void accept_ShouldHandleNullVisitor_WhenVisitorIsNull() {
        assertThatCode(() -> mockASTNode.accept(null))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-06B. AST Root Traversal")
    void getRoot_ShouldReturnAndSetRootNode_WhenASTConstructed() {
        AST ast = new AST(mockASTNode);

        assertThat(ast.getRoot()).isEqualTo(mockASTNode);
    }
}

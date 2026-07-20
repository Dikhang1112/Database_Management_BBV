package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BinaryOpASTNodeTest {

    @Mock
    private ASTNode mockLeftNode;

    @Mock
    private ASTNode mockRightNode;

    @Test
    @DisplayName("TC-18. Build Binary Operator AST Node")
    void buildBinaryOperatorASTNode_ShouldLinkLeftAndRightOperands_WhenParsingBinaryExpression() {
        // Act
        BinaryOpASTNode binaryOpNode = new BinaryOpASTNode("=", mockLeftNode, mockRightNode);

        // Assert
        assertThat(binaryOpNode.getNodeName()).isEqualTo("BinaryOpASTNode");
        assertThat(binaryOpNode.getOperatorType()).isEqualTo("=");
        assertThat(binaryOpNode.getLeftNode()).isEqualTo(mockLeftNode);
        assertThat(binaryOpNode.getRightNode()).isEqualTo(mockRightNode);
    }

    @Test
    @DisplayName("TC-19. Child Operands Evaluation")
    void getLeftNode_And_getRightNode_ShouldReturnChildNodes() {
        // Arrange
        BinaryOpASTNode node = new BinaryOpASTNode("AND", mockLeftNode, mockRightNode);

        // Act & Assert
        assertThat(node.getLeftNode()).isSameAs(mockLeftNode);
        assertThat(node.getRightNode()).isSameAs(mockRightNode);
    }

    @Test
    @DisplayName("TC-20. Default Constructor")
    void defaultConstructor_ShouldSetNodeName() {
        // Act
        BinaryOpASTNode node = new BinaryOpASTNode();

        // Assert
        assertThat(node.getNodeName()).isEqualTo("BinaryOpASTNode");
        assertThat(node.getOperatorType()).isNull();
        assertThat(node.getLeftNode()).isNull();
        assertThat(node.getRightNode()).isNull();
    }
}

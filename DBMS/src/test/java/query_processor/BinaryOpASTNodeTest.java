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

    // Kiểm thử tạo BinaryOpASTNode lưu toán tử và hai cây con vế trái, vế phải
    @Test
    @DisplayName("TC-07. Build Binary Operator AST Node")
    void buildBinaryOpNode_ShouldStoreOperatorAndLeftRightChildNodes() {
        BinaryOpASTNode node = new BinaryOpASTNode(">", mockLeftNode, mockRightNode);

        assertThat(node.getOperatorType()).isEqualTo(">");
        assertThat(node.getLeftNode()).isEqualTo(mockLeftNode);
        assertThat(node.getRightNode()).isEqualTo(mockRightNode);
        assertThat(node.getNodeName()).isEqualTo("BinaryOpASTNode");
    }

    // Kiểm thử hỗ trợ đánh giá các toán tử nhị phân lồng nhau (AND/OR)
    @Test
    @DisplayName("TC-07A. Nested Binary Operators Evaluation")
    void buildBinaryOpNode_ShouldSupportNestedBinaryOperators() {
        BinaryOpASTNode leftChild = new BinaryOpASTNode(">", mockLeftNode, mockRightNode);
        BinaryOpASTNode rightChild = new BinaryOpASTNode("<", mockLeftNode, mockRightNode);

        BinaryOpASTNode rootNode = new BinaryOpASTNode("AND", leftChild, rightChild);

        assertThat(rootNode.getOperatorType()).isEqualTo("AND");
        assertThat(rootNode.getLeftNode()).isEqualTo(leftChild);
        assertThat(rootNode.getRightNode()).isEqualTo(rightChild);
    }
}

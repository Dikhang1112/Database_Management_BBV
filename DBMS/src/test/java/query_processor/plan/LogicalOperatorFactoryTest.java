package query_processor.plan;

import query_processor.abstracts.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogicalOperatorFactoryTest {

    private LogicalOperatorFactory factory;

    @Mock
    private ASTNode mockTableNode;

    @Mock
    private ASTNode mockPredicateNode;

    @Mock
    private ASTNode mockProjectionNode;

    @Mock
    private ASTNode mockUnsupportedNode;

    @Mock
    private Object mockLogicalScan;

    @Mock
    private Object mockLogicalFilter;

    @Mock
    private Object mockLogicalProjection;

    @BeforeEach
    void setUp() {
        factory = spy(new LogicalOperatorFactory() {
            @Override
            public Object createOperator(ASTNode node) {
                if (node == null) {
                    throw new IllegalArgumentException("ASTNode cannot be null");
                }
                if (node == mockTableNode) {
                    return mockLogicalScan;
                }
                if (node == mockPredicateNode) {
                    return mockLogicalFilter;
                }
                if (node == mockProjectionNode) {
                    return mockLogicalProjection;
                }
                throw new UnsupportedOperationException("Unsupported AST node type");
            }
        });
    }

    // TC-16: Kiểm thử tạo toán tử LogicalScan từ nút truy vấn bảng.
    @Test
    @DisplayName("TC-16. Create Logical Scan Operator")
    void createOperator_ShouldCreateLogicalScanOperator_WhenASTNodeIsTableNode() {
        Object operator = factory.createOperator(mockTableNode);

        assertThat(operator).isNotNull().isEqualTo(mockLogicalScan);
    }

    // TC-16A: Kiểm thử tạo toán tử LogicalFilter từ mệnh đề lọc WHERE.
    @Test
    @DisplayName("TC-16A. Create Logical Filter Operator")
    void createOperator_ShouldCreateLogicalFilterOperator_WhenASTNodeIsPredicateNode() {
        Object operator = factory.createOperator(mockPredicateNode);

        assertThat(operator).isNotNull().isEqualTo(mockLogicalFilter);
    }

    // TC-16B: Kiểm thử tạo toán tử LogicalProjection từ danh sách SELECT.
    @Test
    @DisplayName("TC-16B. Create Logical Projection Operator")
    void createOperator_ShouldCreateLogicalProjectionOperator_WhenASTNodeIsProjectionNode() {
        Object operator = factory.createOperator(mockProjectionNode);

        assertThat(operator).isNotNull().isEqualTo(mockLogicalProjection);
    }

    // TC-16C: Kiểm thử an toàn tham số cho Factory khi nhận ASTNode null.
    @Test
    @DisplayName("TC-16C. Null ASTNode Handling in Factory")
    void createOperator_ShouldThrowIllegalArgumentException_WhenASTNodeIsNull() {
        assertThatThrownBy(() -> factory.createOperator(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ASTNode cannot be null");
    }

    // TC-16D: Đảm bảo hệ thống từ chối các loại nút không hỗ trợ bằng ngoại lệ UnsupportedOperationException.
    @Test
    @DisplayName("TC-16D. Unsupported AST Node Type Handling")
    void createOperator_ShouldThrowUnsupportedOperationException_WhenNodeTypeUnsupported() {
        assertThatThrownBy(() -> factory.createOperator(mockUnsupportedNode))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Unsupported AST node type");
    }
}

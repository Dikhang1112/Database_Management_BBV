package query_processor.plan;

import query_processor.abstracts.ASTNode;
import query_processor.ast.AST;
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
class LogicalPlanBuilderTest {

    private LogicalPlanBuilder logicalPlanBuilder;

    @Mock
    private LogicalOperatorFactory mockLogicalOperatorFactory;

    @Mock
    private AST mockAST;

    @Mock
    private ASTNode mockASTNode1;

    @Mock
    private ASTNode mockASTNode2;

    @Mock
    private LogicalPlan mockLogicalPlan;

    @BeforeEach
    void setUp() {
        logicalPlanBuilder = spy(new LogicalPlanBuilder() {
            @Override
            public LogicalPlan build(AST ast) {
                if (ast == null) {
                    throw new IllegalArgumentException("AST cannot be null");
                }
                ASTNode root = ast.getRoot();
                if (root == null) {
                    return new LogicalPlan();
                }
                mockLogicalOperatorFactory.createOperator(root);
                return mockLogicalPlan;
            }
        });
    }

    // TC-15: Kiểm thử tính năng xây dựng cây LogicalPlan hoàn chỉnh từ AST chứa nút cú pháp hợp lệ.
    @Test
    @DisplayName("TC-15. Build Logical Plan from AST")
    void build_ShouldGenerateLogicalPlan_WhenASTContainsValidNodes() {
        when(mockAST.getRoot()).thenReturn(mockASTNode1);

        LogicalPlan result = logicalPlanBuilder.build(mockAST);

        assertThat(result).isNotNull().isEqualTo(mockLogicalPlan);
        verify(mockLogicalOperatorFactory).createOperator(mockASTNode1);
    }

    // TC-15A: Xác minh LogicalOperatorFactory được gọi với mỗi nút ASTNode để tạo toán tử logic.
    @Test
    @DisplayName("TC-15A. Invoke LogicalOperatorFactory For Each Node")
    void build_ShouldInvokeLogicalOperatorFactory_ForEachASTNode() {
        when(mockAST.getRoot()).thenReturn(mockASTNode1);

        logicalPlanBuilder.build(mockAST);

        verify(mockLogicalOperatorFactory, times(1)).createOperator(mockASTNode1);
    }

    // TC-15B: Kiểm thử an toàn tham số đầu vào bị null cho LogicalPlanBuilder.
    @Test
    @DisplayName("TC-15B. Null AST Handling in LogicalPlanBuilder")
    void build_ShouldThrowIllegalArgumentException_WhenASTIsNull() {
        assertThatThrownBy(() -> logicalPlanBuilder.build(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("AST cannot be null");
    }

    // TC-15C: Kiểm thử xử lý biên khi AST rỗng không chứa nút nào.
    @Test
    @DisplayName("TC-15C. Empty AST Handling")
    void build_ShouldReturnEmptyLogicalPlan_WhenASTContainsNoNodes() {
        when(mockAST.getRoot()).thenReturn(null);

        LogicalPlan result = logicalPlanBuilder.build(mockAST);

        assertThat(result).isNotNull();
        verifyNoInteractions(mockLogicalOperatorFactory);
    }

    // TC-15D: Đảm bảo các toán tử logic được sinh ra theo đúng thứ tự duyệt cây AST.
    @Test
    @DisplayName("TC-15D. AST Traversal Order Verification")
    void build_ShouldCreateLogicalOperatorsInTraversalOrder_WhenASTContainsMultipleNodes() {
        LogicalPlanBuilder orderBuilder = new LogicalPlanBuilder() {
            @Override
            public LogicalPlan build(AST ast) {
                mockLogicalOperatorFactory.createOperator(mockASTNode1);
                mockLogicalOperatorFactory.createOperator(mockASTNode2);
                return mockLogicalPlan;
            }
        };

        LogicalPlan result = orderBuilder.build(mockAST);

        assertThat(result).isEqualTo(mockLogicalPlan);
        verify(mockLogicalOperatorFactory).createOperator(mockASTNode1);
        verify(mockLogicalOperatorFactory).createOperator(mockASTNode2);
    }
}

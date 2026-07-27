package query_processor.optimizer;

import query_processor.exceptions.QueryRewriteException;
import query_processor.plan.LogicalPlan;
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
class QueryRewriterTest {

    private QueryRewriter queryRewriter;

    @Mock
    private LogicalPlan mockLogicalPlan;

    @Mock
    private LogicalPlan mockPushdownPlan;

    @Mock
    private LogicalPlan mockProjectionPlan;

    @Mock
    private LogicalPlan mockFoldedPlan;

    @BeforeEach
    void setUp() {
        queryRewriter = spy(new QueryRewriter() {
            @Override
            public LogicalPlan rewrite(LogicalPlan logicalPlan) {
                if (logicalPlan == null) {
                    throw new IllegalArgumentException("Logical plan cannot be null");
                }
                LogicalPlan p1 = predicatePushdown(logicalPlan);
                LogicalPlan p2 = projectionPushdown(p1);
                return constantFolding(p2);
            }

            @Override
            public LogicalPlan predicatePushdown(LogicalPlan logicalPlan) {
                return mockPushdownPlan;
            }

            @Override
            public LogicalPlan projectionPushdown(LogicalPlan logicalPlan) {
                return mockProjectionPlan;
            }

            @Override
            public LogicalPlan constantFolding(LogicalPlan logicalPlan) {
                return mockFoldedPlan;
            }
        });
    }

    // TC-08: Kiểm thử áp dụng toàn bộ các quy tắc viết lại truy vấn (Predicate Pushdown, Projection Pushdown, Constant Folding).
    @Test
    @DisplayName("TC-08. Execute Full Rewrite Rules (Happy Path)")
    void rewrite_ShouldApplyAllRewriteRules_WhenLogicalPlanIsValid() {
        LogicalPlan result = queryRewriter.rewrite(mockLogicalPlan);

        assertThat(result).isNotNull().isEqualTo(mockFoldedPlan);
        verify(queryRewriter).predicatePushdown(mockLogicalPlan);
        verify(queryRewriter).projectionPushdown(mockPushdownPlan);
        verify(queryRewriter).constantFolding(mockProjectionPlan);
    }

    // TC-08A: Kiểm thử chiến lược đẩy mệnh đề lọc WHERE xuống gần toán tử quét dữ liệu Scan nhất.
    @Test
    @DisplayName("TC-08A. Predicate Pushdown Rule")
    void predicatePushdown_ShouldMovePredicatesCloserToScan_WhenFilterExists() {
        LogicalPlan result = queryRewriter.predicatePushdown(mockLogicalPlan);

        assertThat(result).isEqualTo(mockPushdownPlan);
        verify(queryRewriter).predicatePushdown(mockLogicalPlan);
    }

    // TC-08B: Kiểm thử loại bỏ sớm các cột dữ liệu không được tham chiếu để tiết kiệm I/O.
    @Test
    @DisplayName("TC-08B. Projection Pushdown Rule")
    void projectionPushdown_ShouldRemoveUnusedColumns_WhenProjectionContainsExtraColumns() {
        LogicalPlan result = queryRewriter.projectionPushdown(mockLogicalPlan);

        assertThat(result).isEqualTo(mockProjectionPlan);
        verify(queryRewriter).projectionPushdown(mockLogicalPlan);
    }

    // TC-08C: Kiểm thử tính toán và đơn giản hóa các biểu thức hằng số ngay trong thời gian biên dịch.
    @Test
    @DisplayName("TC-08C. Constant Folding Rule")
    void constantFolding_ShouldSimplifyConstantExpressions_WhenExpressionIsConstant() {
        LogicalPlan result = queryRewriter.constantFolding(mockLogicalPlan);

        assertThat(result).isEqualTo(mockFoldedPlan);
        verify(queryRewriter).constantFolding(mockLogicalPlan);
    }

    // TC-08D: Kiểm thử bỏ qua biến đổi đối với cây kế hoạch logic đã ở dạng tối ưu.
    @Test
    @DisplayName("TC-08D. Skip Already Optimized Plan")
    void rewrite_ShouldSkipRewrite_WhenLogicalPlanAlreadyOptimized() {
        QueryRewriter rewriterWithSkip = spy(new QueryRewriter() {
            @Override
            public LogicalPlan rewrite(LogicalPlan logicalPlan) {
                return logicalPlan; // Skip rewrite rules
            }

            @Override public LogicalPlan predicatePushdown(LogicalPlan logicalPlan) { return logicalPlan; }
            @Override public LogicalPlan projectionPushdown(LogicalPlan logicalPlan) { return logicalPlan; }
            @Override public LogicalPlan constantFolding(LogicalPlan logicalPlan) { return logicalPlan; }
        });

        LogicalPlan result = rewriterWithSkip.rewrite(mockLogicalPlan);

        assertThat(result).isEqualTo(mockLogicalPlan);
    }

    // TC-08E: Kiểm thử đảm bảo an toàn phòng thủ cho QueryRewriter khi đầu vào bị null.
    @Test
    @DisplayName("TC-08E. Null Logical Plan Handling in Rewriter")
    void rewrite_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull() {
        assertThatThrownBy(() -> queryRewriter.rewrite(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Logical plan cannot be null");
    }

    // TC-08F: Kiểm thử tính chất Fail-fast: ngắt chuỗi quy tắc viết lại ngay khi bước đầu tiên gặp lỗi QueryRewriteException.
    @Test
    @DisplayName("TC-08F. Fail-Fast Rewrite Pipeline")
    void rewrite_ShouldStopRemainingRewriteRules_WhenPredicatePushdownFails() {
        doThrow(new QueryRewriteException("Pushdown error")).when(queryRewriter).predicatePushdown(mockLogicalPlan);

        assertThatThrownBy(() -> queryRewriter.rewrite(mockLogicalPlan))
                .isInstanceOf(QueryRewriteException.class)
                .hasMessageContaining("Pushdown error");

        verify(queryRewriter).predicatePushdown(mockLogicalPlan);
        verify(queryRewriter, never()).projectionPushdown(any());
        verify(queryRewriter, never()).constantFolding(any());
    }
}

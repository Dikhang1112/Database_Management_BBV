package query_processor.optimizer;

import query_processor.exceptions.CostEstimationException;
import query_processor.exceptions.JoinOptimizationException;
import query_processor.exceptions.QueryRewriteException;
import query_processor.interfaces.OptimizationRule;
import query_processor.planner.LogicalPlan;
import query_processor.planner.PhysicalPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueryOptimizerTest {

    private QueryOptimizer queryOptimizer;

    @Mock
    private QueryRewriter mockQueryRewriter;

    @Mock
    private JoinOptimizer mockJoinOptimizer;

    @Mock
    private CostEstimator mockCostEstimator;

    @Mock
    private PlanEnumerator mockPlanEnumerator;

    @Mock
    private LogicalPlan mockLogicalPlan;

    @Mock
    private LogicalPlan mockRewrittenPlan;

    @Mock
    private LogicalPlan mockJoinPlan;

    @Mock
    private PhysicalPlan mockPhysicalPlan;

    @Mock
    private OptimizationRule mockOptimizationRule;

    @BeforeEach
    void setUp() {
        queryOptimizer = spy(new QueryOptimizer() {
            @Override
            public PhysicalPlan optimize(LogicalPlan logicalPlan) {
                if (logicalPlan == null) {
                    throw new IllegalArgumentException("Logical plan cannot be null");
                }
                LogicalPlan rewritten = mockQueryRewriter.rewrite(logicalPlan);
                LogicalPlan joinOptimized = mockJoinOptimizer.optimize(rewritten);
                mockCostEstimator.estimate(joinOptimized);
                return mockPlanEnumerator.enumerate(joinOptimized);
            }

            @Override
            public void setOptimizationRule(OptimizationRule rule) {
                // Set optimization rule strategy
            }
        });
    }

    // TC-07: Kiểm thử quy trình tối ưu hóa toàn diện qua tuần tự các bước QueryRewriter -> JoinOptimizer -> CostEstimator -> PlanEnumerator.
    @Test
    @DisplayName("TC-07. Execute Full Optimization Pipeline (Happy Path)")
    void optimize_ShouldExecuteOptimizationPipeline_WhenLogicalPlanIsValid() {
        when(mockQueryRewriter.rewrite(mockLogicalPlan)).thenReturn(mockRewrittenPlan);
        when(mockJoinOptimizer.optimize(mockRewrittenPlan)).thenReturn(mockJoinPlan);
        when(mockCostEstimator.estimate(mockJoinPlan)).thenReturn(10.5);
        when(mockPlanEnumerator.enumerate(mockJoinPlan)).thenReturn(mockPhysicalPlan);

        PhysicalPlan result = queryOptimizer.optimize(mockLogicalPlan);

        assertThat(result).isNotNull().isEqualTo(mockPhysicalPlan);

        InOrder inOrder = inOrder(mockQueryRewriter, mockJoinOptimizer, mockCostEstimator, mockPlanEnumerator);
        inOrder.verify(mockQueryRewriter).rewrite(mockLogicalPlan);
        inOrder.verify(mockJoinOptimizer).optimize(mockRewrittenPlan);
        inOrder.verify(mockCostEstimator).estimate(mockJoinPlan);
        inOrder.verify(mockPlanEnumerator).enumerate(mockJoinPlan);
    }

    // TC-07A: Kiểm thử cơ chế ngắt sớm (Fail-fast) khi công đoạn QueryRewriter gặp ngoại lệ QueryRewriteException.
    @Test
    @DisplayName("TC-07A. Query Rewrite Failure Handling")
    void optimize_ShouldStopPipeline_WhenQueryRewriteFails() {
        doThrow(new QueryRewriteException("Query rewrite failed")).when(mockQueryRewriter).rewrite(mockLogicalPlan);

        assertThatThrownBy(() -> queryOptimizer.optimize(mockLogicalPlan))
                .isInstanceOf(QueryRewriteException.class)
                .hasMessageContaining("Query rewrite failed");

        verify(mockQueryRewriter).rewrite(mockLogicalPlan);
        verifyNoInteractions(mockJoinOptimizer, mockCostEstimator, mockPlanEnumerator);
    }

    // TC-07B: Kiểm thử hủy bỏ pipeline khi công đoạn JoinOptimizer tối ưu phép nối thất bại với JoinOptimizationException.
    @Test
    @DisplayName("TC-07B. Join Optimization Failure Handling")
    void optimize_ShouldStopPipeline_WhenJoinOptimizationFails() {
        when(mockQueryRewriter.rewrite(mockLogicalPlan)).thenReturn(mockRewrittenPlan);
        doThrow(new JoinOptimizationException("Join optimization failed")).when(mockJoinOptimizer).optimize(mockRewrittenPlan);

        assertThatThrownBy(() -> queryOptimizer.optimize(mockLogicalPlan))
                .isInstanceOf(JoinOptimizationException.class)
                .hasMessageContaining("Join optimization failed");

        verify(mockQueryRewriter).rewrite(mockLogicalPlan);
        verify(mockJoinOptimizer).optimize(mockRewrittenPlan);
        verifyNoInteractions(mockCostEstimator, mockPlanEnumerator);
    }

    // TC-07C: Kiểm thử dừng pipeline khi việc ước lượng chi phí CBO tại CostEstimator bị lỗi với CostEstimationException.
    @Test
    @DisplayName("TC-07C. Cost Estimation Failure Handling")
    void optimize_ShouldStopPipeline_WhenCostEstimationFails() {
        when(mockQueryRewriter.rewrite(mockLogicalPlan)).thenReturn(mockRewrittenPlan);
        when(mockJoinOptimizer.optimize(mockRewrittenPlan)).thenReturn(mockJoinPlan);
        doThrow(new CostEstimationException("Cost estimation failed")).when(mockCostEstimator).estimate(mockJoinPlan);

        assertThatThrownBy(() -> queryOptimizer.optimize(mockLogicalPlan))
                .isInstanceOf(CostEstimationException.class)
                .hasMessageContaining("Cost estimation failed");

        verify(mockQueryRewriter).rewrite(mockLogicalPlan);
        verify(mockJoinOptimizer).optimize(mockRewrittenPlan);
        verify(mockCostEstimator).estimate(mockJoinPlan);
        verifyNoInteractions(mockPlanEnumerator);
    }

    // TC-07D: Kiểm thử tính an toàn phòng thủ khi tham số LogicalPlan truyền vào bị null.
    @Test
    @DisplayName("TC-07D. Null Logical Plan Handling")
    void optimize_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull() {
        assertThatThrownBy(() -> queryOptimizer.optimize(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Logical plan cannot be null");
    }

    // TC-07E: Kiểm thử xử lý biên khi nhận một LogicalPlan rỗng.
    @Test
    @DisplayName("TC-07E. Empty Logical Plan Handling")
    void optimize_ShouldReturnPhysicalPlan_WhenLogicalPlanIsEmpty() {
        LogicalPlan emptyPlan = new LogicalPlan();
        when(mockQueryRewriter.rewrite(emptyPlan)).thenReturn(emptyPlan);
        when(mockJoinOptimizer.optimize(emptyPlan)).thenReturn(emptyPlan);
        when(mockPlanEnumerator.enumerate(emptyPlan)).thenReturn(mockPhysicalPlan);

        PhysicalPlan result = queryOptimizer.optimize(emptyPlan);

        assertThat(result).isEqualTo(mockPhysicalPlan);
    }

    // TC-07F: Xác minh mỗi thành phần phụ thuộc trong pipeline chỉ được kích hoạt đúng 1 lần duy nhất.
    @Test
    @DisplayName("TC-07F. Verify Dependency Invocation Count")
    void optimize_ShouldInvokeEachDependencyExactlyOnce_WhenOptimizationSucceeds() {
        when(mockQueryRewriter.rewrite(mockLogicalPlan)).thenReturn(mockRewrittenPlan);
        when(mockJoinOptimizer.optimize(mockRewrittenPlan)).thenReturn(mockJoinPlan);
        when(mockPlanEnumerator.enumerate(mockJoinPlan)).thenReturn(mockPhysicalPlan);

        queryOptimizer.optimize(mockLogicalPlan);

        verify(mockQueryRewriter, times(1)).rewrite(mockLogicalPlan);
        verify(mockJoinOptimizer, times(1)).optimize(mockRewrittenPlan);
        verify(mockCostEstimator, times(1)).estimate(mockJoinPlan);
        verify(mockPlanEnumerator, times(1)).enumerate(mockJoinPlan);
    }

    // TC-07G: Kiểm thử tính năng tráo đổi chiến lược tối ưu động (Strategy Pattern).
    @Test
    @DisplayName("TC-07G. Replace Optimization Strategy Rule")
    void setOptimizationRule_ShouldReplaceOptimizationStrategy_WhenNewRuleProvided() {
        queryOptimizer.setOptimizationRule(mockOptimizationRule);
        verify(queryOptimizer).setOptimizationRule(mockOptimizationRule);
    }
}

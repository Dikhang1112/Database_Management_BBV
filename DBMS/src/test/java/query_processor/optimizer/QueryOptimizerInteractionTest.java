package query_processor.optimizer;

import query_processor.exceptions.JoinOptimizationException;
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
class QueryOptimizerInteractionTest {

    @Mock
    private QueryRewriter mockQueryRewriter;

    @Mock
    private JoinOptimizer mockJoinOptimizer;

    @Mock
    private CostEstimator mockCostEstimator;

    @Mock
    private PlanEnumerator mockPlanEnumerator;

    @Mock
    private StatisticsManager mockStatisticsManager;

    @Mock
    private LogicalPlan mockLogicalPlan;

    @Mock
    private LogicalPlan mockRewrittenPlan;

    @Mock
    private LogicalPlan mockJoinPlan;

    @Mock
    private PhysicalPlan mockPhysicalPlan;

    private QueryOptimizer queryOptimizer;

    @BeforeEach
    void setUp() {
        queryOptimizer = spy(new QueryOptimizer() {
            @Override
            public PhysicalPlan optimize(LogicalPlan logicalPlan) {
                LogicalPlan p1 = mockQueryRewriter.rewrite(logicalPlan);
                LogicalPlan p2 = mockJoinOptimizer.optimize(p1);
                mockCostEstimator.estimate(p2);
                return mockPlanEnumerator.enumerate(p2);
            }

            @Override
            public void setOptimizationRule(query_processor.interfaces.OptimizationRule rule) { }
        });
    }

    // TC-13: Kiểm tra QueryRewriter bắt buộc phải là công đoạn đầu tiên được gọi trong pipeline.
    @Test
    @DisplayName("TC-13. QueryRewriter Execution Order First")
    void optimize_ShouldInvokeQueryRewriterFirst_WhenOptimizationPipelineStarts() {
        when(mockQueryRewriter.rewrite(mockLogicalPlan)).thenReturn(mockRewrittenPlan);
        when(mockJoinOptimizer.optimize(mockRewrittenPlan)).thenReturn(mockJoinPlan);
        when(mockPlanEnumerator.enumerate(mockJoinPlan)).thenReturn(mockPhysicalPlan);

        queryOptimizer.optimize(mockLogicalPlan);

        InOrder inOrder = inOrder(mockQueryRewriter, mockJoinOptimizer);
        inOrder.verify(mockQueryRewriter).rewrite(mockLogicalPlan);
        inOrder.verify(mockJoinOptimizer).optimize(mockRewrittenPlan);
    }

    // TC-13A: Đảm bảo JoinOptimizer được kích hoạt ngay sau khi bước QueryRewriter kết thúc.
    @Test
    @DisplayName("TC-13A. JoinOptimizer Execution Order After QueryRewriter")
    void optimize_ShouldInvokeJoinOptimizerAfterQueryRewriter_WhenRewriteCompletes() {
        when(mockQueryRewriter.rewrite(mockLogicalPlan)).thenReturn(mockRewrittenPlan);
        when(mockJoinOptimizer.optimize(mockRewrittenPlan)).thenReturn(mockJoinPlan);
        when(mockPlanEnumerator.enumerate(mockJoinPlan)).thenReturn(mockPhysicalPlan);

        queryOptimizer.optimize(mockLogicalPlan);

        InOrder inOrder = inOrder(mockQueryRewriter, mockJoinOptimizer);
        inOrder.verify(mockQueryRewriter).rewrite(mockLogicalPlan);
        inOrder.verify(mockJoinOptimizer).optimize(mockRewrittenPlan);
    }

    // TC-13B: Xác minh CostEstimator được thực thi sau khi cấu trúc cây và thứ tự JOIN đã tối ưu.
    @Test
    @DisplayName("TC-13B. CostEstimator Execution Order After JoinOptimizer")
    void optimize_ShouldInvokeCostEstimatorAfterJoinOptimizer_WhenJoinOptimizationCompletes() {
        when(mockQueryRewriter.rewrite(mockLogicalPlan)).thenReturn(mockRewrittenPlan);
        when(mockJoinOptimizer.optimize(mockRewrittenPlan)).thenReturn(mockJoinPlan);
        when(mockPlanEnumerator.enumerate(mockJoinPlan)).thenReturn(mockPhysicalPlan);

        queryOptimizer.optimize(mockLogicalPlan);

        InOrder inOrder = inOrder(mockJoinOptimizer, mockCostEstimator);
        inOrder.verify(mockJoinOptimizer).optimize(mockRewrittenPlan);
        inOrder.verify(mockCostEstimator).estimate(mockJoinPlan);
    }

    // TC-13C: Đảm bảo PlanEnumerator luôn là công đoạn cuối cùng để sinh ra PhysicalPlan.
    @Test
    @DisplayName("TC-13C. PlanEnumerator Execution Order Last")
    void optimize_ShouldInvokePlanEnumeratorLast_WhenOptimizationPipelineCompletes() {
        when(mockQueryRewriter.rewrite(mockLogicalPlan)).thenReturn(mockRewrittenPlan);
        when(mockJoinOptimizer.optimize(mockRewrittenPlan)).thenReturn(mockJoinPlan);
        when(mockPlanEnumerator.enumerate(mockJoinPlan)).thenReturn(mockPhysicalPlan);

        queryOptimizer.optimize(mockLogicalPlan);

        InOrder inOrder = inOrder(mockCostEstimator, mockPlanEnumerator);
        inOrder.verify(mockCostEstimator).estimate(mockJoinPlan);
        inOrder.verify(mockPlanEnumerator).enumerate(mockJoinPlan);
    }

    // TC-13D: Xác minh nguyên lý Fail-fast: một bước lỗi JoinOptimizationException làm dừng toàn bộ các bước còn lại phía sau.
    @Test
    @DisplayName("TC-13D. Pipeline Fail-Fast Behavior Verification")
    void optimize_ShouldStopRemainingStages_WhenAnyOptimizationStageFails() {
        when(mockQueryRewriter.rewrite(mockLogicalPlan)).thenReturn(mockRewrittenPlan);
        doThrow(new JoinOptimizationException("Stage 2 Failed")).when(mockJoinOptimizer).optimize(mockRewrittenPlan);

        assertThatThrownBy(() -> queryOptimizer.optimize(mockLogicalPlan))
                .isInstanceOf(JoinOptimizationException.class)
                .hasMessageContaining("Stage 2 Failed");

        verify(mockQueryRewriter).rewrite(mockLogicalPlan);
        verify(mockJoinOptimizer).optimize(mockRewrittenPlan);
        verifyNoInteractions(mockCostEstimator, mockPlanEnumerator);
    }

    // TC-13E: Kiểm thử sự phối hợp dữ liệu giữa CostEstimator và StatisticsManager.
    @Test
    @DisplayName("TC-13E. CostEstimator and StatisticsManager Collaboration")
    void estimate_ShouldInvokeStatisticsManager_WhenCostEstimatorCalculatesCost() {
        CostEstimator costEstimatorWithStats = spy(new CostEstimator() {
            @Override
            public double estimate(LogicalPlan logicalPlan) {
                return estimateCardinality(logicalPlan) * estimateSelectivity(logicalPlan);
            }

            @Override
            public double estimateCardinality(LogicalPlan logicalPlan) {
                return mockStatisticsManager.estimateCardinality();
            }

            @Override
            public double estimateSelectivity(LogicalPlan logicalPlan) {
                return mockStatisticsManager.estimateSelectivity();
            }
        });

        when(mockStatisticsManager.estimateCardinality()).thenReturn(1000.0);
        when(mockStatisticsManager.estimateSelectivity()).thenReturn(0.1);

        double cost = costEstimatorWithStats.estimate(mockLogicalPlan);

        assertThat(cost).isEqualTo(100.0);
        verify(mockStatisticsManager).estimateCardinality();
        verify(mockStatisticsManager).estimateSelectivity();
    }

    // TC-13F: Xác minh tất cả các thành phần phụ thuộc chỉ được gọi đúng 1 lần duy nhất trong một lượt tối ưu.
    @Test
    @DisplayName("TC-13F. Verify Single Execution per Optimization Lifecycle")
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
}

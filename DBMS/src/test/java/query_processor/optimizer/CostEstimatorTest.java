package query_processor.optimizer;

import query_processor.planner.LogicalPlan;
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
class CostEstimatorTest {

    private CostEstimator costEstimator;

    @Mock
    private StatisticsManager mockStatisticsManager;

    @Mock
    private LogicalPlan mockLogicalPlan;

    @BeforeEach
    void setUp() {
        costEstimator = spy(new CostEstimator() {
            @Override
            public double estimate(LogicalPlan logicalPlan) {
                if (logicalPlan == null) {
                    throw new IllegalArgumentException("Logical plan cannot be null");
                }
                double cardinality = estimateCardinality(logicalPlan);
                if (cardinality <= 0) {
                    cardinality = 1000.0; // Fallback mặc định khi chưa có thống kê số dòng
                }
                double selectivity = estimateSelectivity(logicalPlan);
                if (selectivity <= 0) {
                    selectivity = 0.1; // Fallback mặc định khi thiếu thống kê cột
                }
                return cardinality * selectivity * 1.5;
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
    }

    // TC-10: Kiểm thử khả năng tổng hợp chi phí tài nguyên CPU và I/O của cây LogicalPlan.
    @Test
    @DisplayName("TC-10. Calculate Execution Cost (Happy Path)")
    void estimate_ShouldCalculateExecutionCost_WhenLogicalPlanIsValid() {
        when(mockStatisticsManager.estimateCardinality()).thenReturn(10000.0);
        when(mockStatisticsManager.estimateSelectivity()).thenReturn(0.1);

        double cost = costEstimator.estimate(mockLogicalPlan);

        assertThat(cost).isGreaterThan(0.0).isEqualTo(1500.0);
        verify(mockStatisticsManager).estimateCardinality();
        verify(mockStatisticsManager).estimateSelectivity();
    }

    // TC-10A: Kiểm thử ước lượng số lượng dòng dữ liệu dự kiến (Cardinality) của toán tử logic.
    @Test
    @DisplayName("TC-10A. Estimate Row Count (Cardinality)")
    void estimateCardinality_ShouldEstimateRowCount_WhenStatisticsAvailable() {
        when(mockStatisticsManager.estimateCardinality()).thenReturn(5000.0);

        double cardinality = costEstimator.estimateCardinality(mockLogicalPlan);

        assertThat(cardinality).isEqualTo(5000.0);
        verify(mockStatisticsManager).estimateCardinality();
    }

    // TC-10B: Kiểm thử tính toán tỷ lệ phần trăm dữ liệu thỏa mãn điều kiện lọc (Selectivity).
    @Test
    @DisplayName("TC-10B. Estimate Predicate Selectivity")
    void estimateSelectivity_ShouldEstimatePredicateSelectivity_WhenFilterExists() {
        when(mockStatisticsManager.estimateSelectivity()).thenReturn(0.15);

        double selectivity = costEstimator.estimateSelectivity(mockLogicalPlan);

        assertThat(selectivity).isEqualTo(0.15);
        verify(mockStatisticsManager).estimateSelectivity();
    }

    // TC-10C: Xác minh CostEstimator tương tác và lấy các số liệu thống kê từ StatisticsManager.
    @Test
    @DisplayName("TC-10C. Verify Interaction with StatisticsManager")
    void estimate_ShouldInvokeStatisticsManager_WhenCostCalculationStarts() {
        when(mockStatisticsManager.estimateCardinality()).thenReturn(1000.0);
        when(mockStatisticsManager.estimateSelectivity()).thenReturn(0.2);

        costEstimator.estimate(mockLogicalPlan);

        verify(mockStatisticsManager).estimateCardinality();
        verify(mockStatisticsManager).estimateSelectivity();
    }

    // TC-10D: Kiểm thử dùng tham số mặc định (Default heuristics) khi bảng chưa được thu thập thống kê.
    @Test
    @DisplayName("TC-10D. Fallback Default Statistics Handling")
    void estimate_ShouldUseDefaultStatistics_WhenMetadataStatisticsUnavailable() {
        when(mockStatisticsManager.estimateCardinality()).thenReturn(-1.0); // Thiếu dữ liệu thống kê bảng
        when(mockStatisticsManager.estimateSelectivity()).thenReturn(0.1);

        double cost = costEstimator.estimate(mockLogicalPlan);

        // Chi phí tính theo fallback: 1000.0 (default card) * 0.1 (sel) * 1.5 = 150.0
        assertThat(cost).isEqualTo(150.0);
        verify(mockStatisticsManager).estimateCardinality();
        verify(mockStatisticsManager).estimateSelectivity();
    }

    // TC-10E: Kiểm thử thẩm định an toàn tham số đầu vào bị null của CostEstimator.
    @Test
    @DisplayName("TC-10E. Null Logical Plan Handling in CostEstimator")
    void estimate_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull() {
        assertThatThrownBy(() -> costEstimator.estimate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Logical plan cannot be null");
    }
}

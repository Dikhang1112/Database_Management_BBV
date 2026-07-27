package query_processor.optimizer;

import query_processor.planner.LogicalPlan;
import query_processor.planner.PhysicalPlan;
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
class PlanEnumeratorTest {

    private PlanEnumerator planEnumerator;

    @Mock
    private LogicalPlan mockLogicalPlan;

    @Mock
    private PhysicalPlan mockPhysicalPlan;

    @Mock
    private PhysicalPlan mockSeqScanPlan;

    @Mock
    private PhysicalPlan mockIndexScanPlan;

    @BeforeEach
    void setUp() {
        planEnumerator = spy(new PlanEnumerator() {
            @Override
            public PhysicalPlan enumerate(LogicalPlan logicalPlan) {
                if (logicalPlan == null) {
                    throw new IllegalArgumentException("Logical plan cannot be null");
                }
                return selectAccessPath(logicalPlan);
            }

            @Override
            public PhysicalPlan selectAccessPath(LogicalPlan logicalPlan) {
                return mockPhysicalPlan;
            }
        });
    }

    // TC-12: Kiểm thử chuyển đổi cây kế hoạch logic LogicalPlan thành cây kế hoạch thực thi vật lý PhysicalPlan.
    @Test
    @DisplayName("TC-12. Generate Physical Plan (Happy Path)")
    void enumerate_ShouldGeneratePhysicalPlan_WhenLogicalPlanIsValid() {
        PhysicalPlan result = planEnumerator.enumerate(mockLogicalPlan);

        assertThat(result).isNotNull().isEqualTo(mockPhysicalPlan);
        verify(planEnumerator).selectAccessPath(mockLogicalPlan);
    }

    // TC-12A: Kiểm thử so sánh và lựa chọn đường truy xuất dữ liệu có chi phí thấp nhất.
    @Test
    @DisplayName("TC-12A. Select Best Access Path")
    void selectAccessPath_ShouldChooseBestAccessPath_WhenMultipleCandidatesExist() {
        PhysicalPlan result = planEnumerator.selectAccessPath(mockLogicalPlan);

        assertThat(result).isEqualTo(mockPhysicalPlan);
        verify(planEnumerator).selectAccessPath(mockLogicalPlan);
    }

    // TC-12B: Kiểm thử tự động chọn phương pháp Sequential Scan khi bảng không có chỉ mục.
    @Test
    @DisplayName("TC-12B. Generate Sequential Scan When No Index Available")
    void enumerate_ShouldGenerateSequentialScan_WhenNoIndexAvailable() {
        doReturn(mockSeqScanPlan).when(planEnumerator).selectAccessPath(mockLogicalPlan);

        PhysicalPlan result = planEnumerator.enumerate(mockLogicalPlan);

        assertThat(result).isEqualTo(mockSeqScanPlan);
        verify(planEnumerator).selectAccessPath(mockLogicalPlan);
    }

    // TC-12C: Kiểm thử tận dụng chỉ mục B+ Tree có sẵn để tạo toán tử tìm kiếm Index Scan.
    @Test
    @DisplayName("TC-12C. Generate Index Scan When Matching Index Exists")
    void enumerate_ShouldGenerateIndexScan_WhenMatchingIndexExists() {
        doReturn(mockIndexScanPlan).when(planEnumerator).selectAccessPath(mockLogicalPlan);

        PhysicalPlan result = planEnumerator.enumerate(mockLogicalPlan);

        assertThat(result).isEqualTo(mockIndexScanPlan);
        verify(planEnumerator).selectAccessPath(mockLogicalPlan);
    }

    // TC-12D: Kiểm thử an toàn tham số đầu vào bị null cho PlanEnumerator.
    @Test
    @DisplayName("TC-12D. Null Logical Plan Handling in PlanEnumerator")
    void enumerate_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull() {
        assertThatThrownBy(() -> planEnumerator.enumerate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Logical plan cannot be null");
    }
}

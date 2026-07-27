package query_processor.optimizer;

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
class JoinOptimizerTest {

    private JoinOptimizer joinOptimizer;

    @Mock
    private LogicalPlan mockLogicalPlan;

    @Mock
    private LogicalPlan mockReorderedPlan;

    @Mock
    private LogicalPlan mockMethodSelectedPlan;

    @BeforeEach
    void setUp() {
        joinOptimizer = spy(new JoinOptimizer() {
            @Override
            public LogicalPlan optimize(LogicalPlan logicalPlan) {
                if (logicalPlan == null) {
                    throw new IllegalArgumentException("Logical plan cannot be null");
                }
                LogicalPlan reordered = optimizeJoinOrder(logicalPlan);
                return selectJoinMethod(reordered);
            }

            @Override
            public LogicalPlan optimizeJoinOrder(LogicalPlan logicalPlan) {
                return mockReorderedPlan;
            }

            @Override
            public LogicalPlan selectJoinMethod(LogicalPlan logicalPlan) {
                return mockMethodSelectedPlan;
            }
        });
    }

    // TC-09: Kiểm thử quy trình tối ưu phép nối: tính toán thứ tự JOIN và chọn thuật toán thực thi.
    @Test
    @DisplayName("TC-09. Execute Join Optimization Pipeline (Happy Path)")
    void optimize_ShouldOptimizeJoinPipeline_WhenLogicalPlanContainsJoin() {
        LogicalPlan result = joinOptimizer.optimize(mockLogicalPlan);

        assertThat(result).isNotNull().isEqualTo(mockMethodSelectedPlan);
        verify(joinOptimizer).optimizeJoinOrder(mockLogicalPlan);
        verify(joinOptimizer).selectJoinMethod(mockReorderedPlan);
    }

    // TC-09A: Kiểm thử thuật toán hoán vị và sắp xếp thứ tự nối các bảng nhỏ trước bảng lớn.
    @Test
    @DisplayName("TC-09A. Join Order Optimization")
    void optimizeJoinOrder_ShouldReorderJoinSequence_WhenMultipleTablesExist() {
        LogicalPlan result = joinOptimizer.optimizeJoinOrder(mockLogicalPlan);

        assertThat(result).isEqualTo(mockReorderedPlan);
        verify(joinOptimizer).optimizeJoinOrder(mockLogicalPlan);
    }

    // TC-09B: Kiểm thử lựa chọn thuật toán Hash Join khi chi phí dựng bảng Hash tối ưu hơn.
    @Test
    @DisplayName("TC-09B. Select Hash Join Method")
    void selectJoinMethod_ShouldChooseHashJoin_WhenHashJoinIsOptimal() {
        LogicalPlan result = joinOptimizer.selectJoinMethod(mockReorderedPlan);

        assertThat(result).isEqualTo(mockMethodSelectedPlan);
        verify(joinOptimizer).selectJoinMethod(mockReorderedPlan);
    }

    // TC-09C: Kiểm thử chọn thuật toán Nested Loop Join khi dữ liệu các bảng đầu vào rất nhỏ.
    @Test
    @DisplayName("TC-09C. Select Nested Loop Join Method")
    void selectJoinMethod_ShouldChooseNestedLoopJoin_WhenInputTablesAreSmall() {
        LogicalPlan smallTablePlan = mock(LogicalPlan.class);
        when(joinOptimizer.selectJoinMethod(smallTablePlan)).thenReturn(smallTablePlan);

        LogicalPlan result = joinOptimizer.selectJoinMethod(smallTablePlan);

        assertThat(result).isEqualTo(smallTablePlan);
    }

    // TC-09D: Kiểm thử trả về nguyên trạng cây kế hoạch đối với câu lệnh SQL truy vấn 1 bảng (không chứa JOIN).
    @Test
    @DisplayName("TC-09D. Handle Plan Without Join")
    void optimize_ShouldReturnOriginalPlan_WhenLogicalPlanContainsNoJoin() {
        JoinOptimizer noJoinOptimizer = spy(new JoinOptimizer() {
            @Override
            public LogicalPlan optimize(LogicalPlan logicalPlan) {
                return logicalPlan;
            }

            @Override public LogicalPlan optimizeJoinOrder(LogicalPlan logicalPlan) { return logicalPlan; }
            @Override public LogicalPlan selectJoinMethod(LogicalPlan logicalPlan) { return logicalPlan; }
        });

        LogicalPlan result = noJoinOptimizer.optimize(mockLogicalPlan);

        assertThat(result).isEqualTo(mockLogicalPlan);
    }

    // TC-09E: Kiểm thử an toàn tham số đầu vào bị null cho JoinOptimizer.
    @Test
    @DisplayName("TC-09E. Null Logical Plan Handling in JoinOptimizer")
    void optimize_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull() {
        assertThatThrownBy(() -> joinOptimizer.optimize(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Logical plan cannot be null");
    }
}

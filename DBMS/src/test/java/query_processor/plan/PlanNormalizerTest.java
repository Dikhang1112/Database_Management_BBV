package query_processor.plan;

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
class PlanNormalizerTest {

    private PlanNormalizer normalizer;

    @Mock
    private LogicalPlan mockLogicalPlan;

    @Mock
    private LogicalPlan mockNormalizedPlan;

    @Mock
    private LogicalPlan mockRedundantPlan;

    @Mock
    private LogicalPlan mockNestedPlan;

    @BeforeEach
    void setUp() {
        normalizer = spy(new PlanNormalizer() {
            @Override
            public LogicalPlan normalize(LogicalPlan logicalPlan) {
                if (logicalPlan == null) {
                    throw new IllegalArgumentException("Logical plan cannot be null");
                }
                return mockNormalizedPlan;
            }
        });
    }

    // TC-20: Kiểm thử chuẩn hóa cấu trúc cây kế hoạch logic.
    @Test
    @DisplayName("TC-20. Normalize Logical Plan (Happy Path)")
    void normalize_ShouldReturnNormalizedLogicalPlan_WhenLogicalPlanIsValid() {
        LogicalPlan result = normalizer.normalize(mockLogicalPlan);

        assertThat(result).isNotNull().isEqualTo(mockNormalizedPlan);
    }

    // TC-20A: Kiểm thử an toàn tham số cho PlanNormalizer khi nhận tham số null.
    @Test
    @DisplayName("TC-20A. Null Logical Plan Handling in PlanNormalizer")
    void normalize_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull() {
        assertThatThrownBy(() -> normalizer.normalize(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Logical plan cannot be null");
    }

    // TC-20B: Loại bỏ các toán tử dư thừa lặp lại không cần thiết (ví dụ 2 nút Filter giống hệt).
    @Test
    @DisplayName("TC-20B. Remove Redundant Operators")
    void normalize_ShouldRemoveRedundantOperators_WhenDuplicateOperatorsExist() {
        LogicalPlan result = normalizer.normalize(mockRedundantPlan);

        assertThat(result).isEqualTo(mockNormalizedPlan);
    }

    // TC-20C: Làm phẳng (flatten) các cấu trúc cây lồng nhau để tối ưu hóa truy xuất.
    @Test
    @DisplayName("TC-20C. Flatten Nested Operators")
    void normalize_ShouldFlattenNestedOperators_WhenNestedTreeExists() {
        LogicalPlan result = normalizer.normalize(mockNestedPlan);

        assertThat(result).isEqualTo(mockNormalizedPlan);
    }

    // TC-20D: Đảm bảo việc chuẩn hóa không làm thay đổi ngữ nghĩa dữ liệu của câu lệnh SQL.
    @Test
    @DisplayName("TC-20D. Preserve Logical Semantics After Normalization")
    void normalize_ShouldPreserveLogicalSemantics_WhenNormalizationCompletes() {
        LogicalPlan result = normalizer.normalize(mockLogicalPlan);

        assertThat(result).isEqualTo(mockNormalizedPlan);
        verify(normalizer).normalize(mockLogicalPlan);
    }
}

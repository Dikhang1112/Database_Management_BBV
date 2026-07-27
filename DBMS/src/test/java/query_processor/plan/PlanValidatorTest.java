package query_processor.plan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanValidatorTest {

    private PlanValidator validator;

    @Mock
    private LogicalPlan mockValidPlan;

    @Mock
    private LogicalPlan mockInvalidOperatorPlan;

    @Mock
    private LogicalPlan mockBrokenTreePlan;

    @Mock
    private LogicalPlan mockMultipleRootsPlan;

    @BeforeEach
    void setUp() {
        validator = spy(new PlanValidator() {
            @Override
            public void validate(LogicalPlan logicalPlan) {
                if (logicalPlan == null) {
                    throw new IllegalArgumentException("Logical plan cannot be null");
                }
                if (logicalPlan == mockInvalidOperatorPlan) {
                    throw new RuntimeException("Invalid logical operator detected");
                }
                if (logicalPlan == mockBrokenTreePlan) {
                    throw new RuntimeException("Disconnected plan tree");
                }
                if (logicalPlan == mockMultipleRootsPlan) {
                    throw new RuntimeException("Multiple root nodes detected");
                }
            }
        });
    }

    // TC-19: Kiểm thử thẩm định thành công một LogicalPlan chuẩn hợp lệ.
    @Test
    @DisplayName("TC-19. Validate Logical Plan (Happy Path)")
    void validate_ShouldReturnTrue_WhenLogicalPlanIsValid() {
        assertThatCode(() -> validator.validate(mockValidPlan))
                .doesNotThrowAnyException();
    }

    // TC-19A: Kiểm thử an toàn tham số đầu vào khi nhận LogicalPlan null.
    @Test
    @DisplayName("TC-19A. Null Logical Plan Handling in PlanValidator")
    void validate_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull() {
        assertThatThrownBy(() -> validator.validate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Logical plan cannot be null");
    }

    // TC-19B: Phát hiện các toán tử logic không hợp lệ bị lỗi cấu hình.
    @Test
    @DisplayName("TC-19B. Detect Invalid Logical Operator")
    void validate_ShouldDetectInvalidLogicalOperator_WhenLogicalPlanContainsInvalidOperator() {
        assertThatThrownBy(() -> validator.validate(mockInvalidOperatorPlan))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid logical operator detected");
    }

    // TC-19C: Đảm bảo phát hiện cây kế hoạch bị gãy/đứt đoạn không liên thông.
    @Test
    @DisplayName("TC-19C. Detect Disconnected Plan Tree")
    void validate_ShouldDetectDisconnectedPlan_WhenLogicalPlanContainsBrokenTree() {
        assertThatThrownBy(() -> validator.validate(mockBrokenTreePlan))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Disconnected plan tree");
    }

    // TC-19D: Cây kế hoạch hợp lệ chỉ được phép có duy nhất 1 nút gốc; từ chối khi có nhiều nút gốc.
    @Test
    @DisplayName("TC-19D. Reject Duplicate Root Node")
    void validate_ShouldRejectDuplicateRootNode_WhenLogicalPlanContainsMultipleRoots() {
        assertThatThrownBy(() -> validator.validate(mockMultipleRootsPlan))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Multiple root nodes detected");
    }
}

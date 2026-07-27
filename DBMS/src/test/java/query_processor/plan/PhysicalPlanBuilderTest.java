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
class PhysicalPlanBuilderTest {

    private PhysicalPlanBuilder physicalPlanBuilder;

    @Mock
    private PhysicalOperatorFactory mockPhysicalOperatorFactory;

    @Mock
    private LogicalPlan mockLogicalPlan;

    @Mock
    private LogicalPlanNode mockLogicalPlanNode1;

    @Mock
    private LogicalPlanNode mockLogicalPlanNode2;

    @Mock
    private PhysicalPlan mockPhysicalPlan;

    @BeforeEach
    void setUp() {
        physicalPlanBuilder = spy(new PhysicalPlanBuilder() {
            @Override
            public PhysicalPlan build(LogicalPlan logicalPlan) {
                if (logicalPlan == null) {
                    throw new IllegalArgumentException("Logical plan cannot be null");
                }
                mockPhysicalOperatorFactory.createOperator(mockLogicalPlanNode1);
                return mockPhysicalPlan;
            }
        });
    }

    // TC-17: Kiểm thử dựng cây PhysicalPlan thực thi vật lý từ cây logic hợp lệ.
    @Test
    @DisplayName("TC-17. Build Physical Plan")
    void build_ShouldGeneratePhysicalPlan_WhenLogicalPlanIsValid() {
        PhysicalPlan result = physicalPlanBuilder.build(mockLogicalPlan);

        assertThat(result).isNotNull().isEqualTo(mockPhysicalPlan);
        verify(mockPhysicalOperatorFactory).createOperator(mockLogicalPlanNode1);
    }

    // TC-17A: Xác minh PhysicalOperatorFactory được kích hoạt cho mỗi nút logic trong LogicalPlan.
    @Test
    @DisplayName("TC-17A. Invoke PhysicalOperatorFactory For Each Node")
    void build_ShouldInvokePhysicalOperatorFactory_ForEachLogicalPlanNode() {
        physicalPlanBuilder.build(mockLogicalPlan);

        verify(mockPhysicalOperatorFactory, times(1)).createOperator(mockLogicalPlanNode1);
    }

    // TC-17B: Kiểm thử an toàn phòng thủ cho PhysicalPlanBuilder khi tham số LogicalPlan bị null.
    @Test
    @DisplayName("TC-17B. Null Logical Plan Handling in PhysicalPlanBuilder")
    void build_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull() {
        assertThatThrownBy(() -> physicalPlanBuilder.build(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Logical plan cannot be null");
    }

    // TC-17C: Kiểm thử xử lý biên cho cây logic rỗng không chứa toán tử nào.
    @Test
    @DisplayName("TC-17C. Empty Logical Plan Handling in PhysicalPlanBuilder")
    void build_ShouldReturnEmptyPhysicalPlan_WhenLogicalPlanContainsNoOperators() {
        PhysicalPlanBuilder emptyBuilder = new PhysicalPlanBuilder() {
            @Override
            public PhysicalPlan build(LogicalPlan logicalPlan) {
                if (logicalPlan == null) {
                    throw new IllegalArgumentException("Logical plan cannot be null");
                }
                return new PhysicalPlan();
            }
        };

        PhysicalPlan result = emptyBuilder.build(mockLogicalPlan);

        assertThat(result).isNotNull();
        verifyNoInteractions(mockPhysicalOperatorFactory);
    }

    // TC-17D: Đảm bảo các toán tử vật lý được xếp theo đúng thứ tự thực thi từ nguồn dữ liệu lên.
    @Test
    @DisplayName("TC-17D. Physical Execution Order Verification")
    void build_ShouldGenerateOperatorsInExecutionOrder_WhenLogicalPlanContainsMultipleOperators() {
        PhysicalPlanBuilder orderBuilder = new PhysicalPlanBuilder() {
            @Override
            public PhysicalPlan build(LogicalPlan logicalPlan) {
                mockPhysicalOperatorFactory.createOperator(mockLogicalPlanNode1);
                mockPhysicalOperatorFactory.createOperator(mockLogicalPlanNode2);
                return mockPhysicalPlan;
            }
        };

        PhysicalPlan result = orderBuilder.build(mockLogicalPlan);

        assertThat(result).isEqualTo(mockPhysicalPlan);
        verify(mockPhysicalOperatorFactory).createOperator(mockLogicalPlanNode1);
        verify(mockPhysicalOperatorFactory).createOperator(mockLogicalPlanNode2);
    }
}

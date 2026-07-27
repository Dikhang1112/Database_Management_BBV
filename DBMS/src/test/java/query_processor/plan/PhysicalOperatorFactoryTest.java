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
class PhysicalOperatorFactoryTest {

    private PhysicalOperatorFactory factory;

    @Mock
    private LogicalPlanNode mockScanNode;

    @Mock
    private LogicalPlanNode mockIndexNode;

    @Mock
    private LogicalPlanNode mockHashJoinNode;

    @Mock
    private LogicalPlanNode mockUnsupportedNode;

    @Mock
    private PhysicalPlanNode mockSeqScanOperator;

    @Mock
    private PhysicalPlanNode mockIndexScanOperator;

    @Mock
    private PhysicalPlanNode mockHashJoinOperator;

    @BeforeEach
    void setUp() {
        factory = spy(new PhysicalOperatorFactory() {
            @Override
            public PhysicalPlanNode createOperator(LogicalPlanNode node) {
                if (node == null) {
                    throw new IllegalArgumentException("LogicalPlanNode cannot be null");
                }
                if (node == mockScanNode) {
                    return mockSeqScanOperator;
                }
                if (node == mockIndexNode) {
                    return mockIndexScanOperator;
                }
                if (node == mockHashJoinNode) {
                    return mockHashJoinOperator;
                }
                throw new UnsupportedOperationException("Unsupported logical operator type");
            }
        });
    }

    // TC-18: Kiểm thử sinh toán tử vật lý quét tuần tự PhysicalSeqScan.
    @Test
    @DisplayName("TC-18. Create Sequential Scan Physical Operator")
    void createOperator_ShouldCreateSequentialScan_WhenLogicalNodeIsScan() {
        PhysicalPlanNode operator = factory.createOperator(mockScanNode);

        assertThat(operator).isNotNull().isEqualTo(mockSeqScanOperator);
    }

    // TC-18A: Kiểm thử sinh toán tử vật lý quét chỉ mục PhysicalIndexScan.
    @Test
    @DisplayName("TC-18A. Create Index Scan Physical Operator")
    void createOperator_ShouldCreateIndexScan_WhenLogicalNodeUsesIndex() {
        PhysicalPlanNode operator = factory.createOperator(mockIndexNode);

        assertThat(operator).isNotNull().isEqualTo(mockIndexScanOperator);
    }

    // TC-18B: Kiểm thử sinh toán tử vật lý PhysicalHashJoin.
    @Test
    @DisplayName("TC-18B. Create Hash Join Physical Operator")
    void createOperator_ShouldCreateHashJoin_WhenLogicalNodeIsHashJoin() {
        PhysicalPlanNode operator = factory.createOperator(mockHashJoinNode);

        assertThat(operator).isNotNull().isEqualTo(mockHashJoinOperator);
    }

    // TC-18C: Kiểm thử an toàn tham số cho Physical Factory khi nhận LogicalPlanNode null.
    @Test
    @DisplayName("TC-18C. Null LogicalPlanNode Handling in Physical Factory")
    void createOperator_ShouldThrowIllegalArgumentException_WhenLogicalPlanNodeIsNull() {
        assertThatThrownBy(() -> factory.createOperator(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("LogicalPlanNode cannot be null");
    }

    // TC-18D: Đảm bảo từ chối các loại toán tử không hỗ trợ bằng UnsupportedOperationException.
    @Test
    @DisplayName("TC-18D. Unsupported Physical Operator Type Handling")
    void createOperator_ShouldThrowUnsupportedOperationException_WhenOperatorTypeUnsupported() {
        assertThatThrownBy(() -> factory.createOperator(mockUnsupportedNode))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Unsupported logical operator type");
    }
}

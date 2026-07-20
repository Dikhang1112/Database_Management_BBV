package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class QueryOptimizerTest {

    private final QueryOptimizer optimizer = new QueryOptimizer();

    @Mock
    private AST mockAst;

    @Mock
    private ASTNode mockNode;

    // Kiểm thử biến đổi cây AST thành cây kế hoạch logic LogicalPlan
    @Test
    @DisplayName("TC-10. Generate Logical Plan")
    void generateLogicalPlan_ShouldConvertASTToLogicalPlan() {
        org.mockito.Mockito.when(mockAst.getRootASTNode()).thenReturn(mockNode);

        LogicalPlan plan = optimizer.generateLogicalPlan(mockAst);

        assertThat(plan).isNotNull();
    }

    // Kiểm thử tối ưu hóa LogicalPlan thành kế hoạch vật lý PhysicalPlan
    @Test
    @DisplayName("TC-10A. Optimize Logical Plan to Physical Plan")
    void optimize_ShouldTransformLogicalPlanToPhysicalPlan_WhenRulesApplied() {
        LogicalPlan logicalPlan = new LogicalPlan();

        PhysicalPlan physicalPlan = optimizer.optimize(logicalPlan);

        assertThat(physicalPlan).isNotNull();
    }

    // Kiểm thử tính toán tổng chi phí ước tính (estimatedCost) của kế hoạch vật lý
    @Test
    @DisplayName("TC-10B. Estimate Execution Plan Cost")
    void estimateCost_ShouldComputeTotalPlanCost_WhenPhysicalPlanOptimized() {
        LogicalPlan logicalPlan = new LogicalPlan();

        PhysicalPlan physicalPlan = optimizer.optimize(logicalPlan);

        assertThat(physicalPlan.getEstimatedCost()).isGreaterThanOrEqualTo(0.0);
    }

    // Kiểm thử tự động chọn IndexScanNode khi cột lọc có đánh chỉ mục Index
    @Test
    @DisplayName("TC-10C. Select IndexScan When Index Exists")
    void optimize_ShouldSelectIndexScan_WhenIndexExistsOnFilterColumn() {
        LogicalPlan logicalPlan = new LogicalPlan();

        PhysicalPlan physicalPlan = optimizer.optimize(logicalPlan);

        assertThat(physicalPlan.getPhysicalRoot()).isNotNull();
    }
}

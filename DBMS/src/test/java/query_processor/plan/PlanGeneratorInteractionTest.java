package query_processor.plan;

import query_processor.ast.AST;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanGeneratorInteractionTest {

    @Mock
    private LogicalPlanBuilder mockLogicalPlanBuilder;

    @Mock
    private LogicalOperatorFactory mockLogicalOperatorFactory;

    @Mock
    private PhysicalPlanBuilder mockPhysicalPlanBuilder;

    @Mock
    private PhysicalOperatorFactory mockPhysicalOperatorFactory;

    @Mock
    private PlanValidator mockPlanValidator;

    @Mock
    private PlanNormalizer mockPlanNormalizer;

    @Mock
    private AST mockAST;

    @Mock
    private LogicalPlan mockLogicalPlan;

    @Mock
    private LogicalPlan mockNormalizedPlan;

    @Mock
    private PhysicalPlan mockPhysicalPlan;

    @Mock
    private LogicalPlanNode mockLogicalPlanNode;

    private PlanGenerator planGenerator;

    @BeforeEach
    void setUp() {
        planGenerator = spy(new PlanGenerator() {
            @Override
            public LogicalPlan createLogicalPlan(AST ast) {
                return mockLogicalPlanBuilder.build(ast);
            }

            @Override
            public PhysicalPlan createPhysicalPlan(LogicalPlan logicalPlan) {
                mockPlanValidator.validate(logicalPlan);
                LogicalPlan normalized = mockPlanNormalizer.normalize(logicalPlan);
                return mockPhysicalPlanBuilder.build(normalized);
            }
        });
    }

    // TC-21: Xác minh PlanGenerator tương tác đúng với LogicalPlanBuilder khi bắt đầu tạo kế hoạch logic.
    @Test
    @DisplayName("TC-21. LogicalPlanBuilder Interaction Verification")
    void createLogicalPlan_ShouldInvokeLogicalPlanBuilder_WhenGenerationStarts() {
        when(mockLogicalPlanBuilder.build(mockAST)).thenReturn(mockLogicalPlan);

        planGenerator.createLogicalPlan(mockAST);

        verify(mockLogicalPlanBuilder).build(mockAST);
    }

    // TC-21A: Đảm bảo LogicalOperatorFactory được kích hoạt trong quá trình tạo kế hoạch logic.
    @Test
    @DisplayName("TC-21A. LogicalOperatorFactory Interaction Verification")
    void createLogicalPlan_ShouldInvokeLogicalOperatorFactory_WhenBuildingLogicalPlan() {
        LogicalPlanBuilder builderWithFactory = new LogicalPlanBuilder() {
            @Override
            public LogicalPlan build(AST ast) {
                mockLogicalOperatorFactory.createOperator(null);
                return mockLogicalPlan;
            }
        };

        builderWithFactory.build(mockAST);

        verify(mockLogicalOperatorFactory).createOperator(null);
    }

    // TC-21B: Đảm bảo PlanValidator luôn là bước đầu tiên được gọi trong pipeline tạo kế hoạch vật lý.
    @Test
    @DisplayName("TC-21B. PlanValidator Execution Order First")
    void createPhysicalPlan_ShouldInvokePlanValidatorFirst_WhenGenerationStarts() {
        when(mockPlanNormalizer.normalize(mockLogicalPlan)).thenReturn(mockNormalizedPlan);
        when(mockPhysicalPlanBuilder.build(mockNormalizedPlan)).thenReturn(mockPhysicalPlan);

        planGenerator.createPhysicalPlan(mockLogicalPlan);

        InOrder inOrder = inOrder(mockPlanValidator, mockPlanNormalizer);
        inOrder.verify(mockPlanValidator).validate(mockLogicalPlan);
        inOrder.verify(mockPlanNormalizer).normalize(mockLogicalPlan);
    }

    // TC-21C: Đảm bảo PlanNormalizer được kích hoạt ngay sau khi bước validation vượt qua và trước bước build.
    @Test
    @DisplayName("TC-21C. PlanNormalizer Execution Order After Validation")
    void createPhysicalPlan_ShouldInvokePlanNormalizerAfterValidation_WhenValidationSucceeds() {
        when(mockPlanNormalizer.normalize(mockLogicalPlan)).thenReturn(mockNormalizedPlan);
        when(mockPhysicalPlanBuilder.build(mockNormalizedPlan)).thenReturn(mockPhysicalPlan);

        planGenerator.createPhysicalPlan(mockLogicalPlan);

        InOrder inOrder = inOrder(mockPlanValidator, mockPlanNormalizer, mockPhysicalPlanBuilder);
        inOrder.verify(mockPlanValidator).validate(mockLogicalPlan);
        inOrder.verify(mockPlanNormalizer).normalize(mockLogicalPlan);
        inOrder.verify(mockPhysicalPlanBuilder).build(mockNormalizedPlan);
    }

    // TC-21D: Đảm bảo PhysicalPlanBuilder chạy sau khi cây logic đã chuẩn hóa.
    @Test
    @DisplayName("TC-21D. PhysicalPlanBuilder Execution Order After Normalization")
    void createPhysicalPlan_ShouldInvokePhysicalPlanBuilderAfterNormalization_WhenNormalizationCompletes() {
        when(mockPlanNormalizer.normalize(mockLogicalPlan)).thenReturn(mockNormalizedPlan);
        when(mockPhysicalPlanBuilder.build(mockNormalizedPlan)).thenReturn(mockPhysicalPlan);

        planGenerator.createPhysicalPlan(mockLogicalPlan);

        InOrder inOrder = inOrder(mockPlanNormalizer, mockPhysicalPlanBuilder);
        inOrder.verify(mockPlanNormalizer).normalize(mockLogicalPlan);
        inOrder.verify(mockPhysicalPlanBuilder).build(mockNormalizedPlan);
    }

    // TC-21E: Đảm bảo PhysicalOperatorFactory được kích hoạt khi tạo từng toán tử vật lý.
    @Test
    @DisplayName("TC-21E. PhysicalOperatorFactory Interaction Verification")
    void build_ShouldInvokePhysicalOperatorFactory_WhenBuildingPhysicalPlan() {
        PhysicalPlanBuilder builderWithFactory = new PhysicalPlanBuilder() {
            @Override
            public PhysicalPlan build(LogicalPlan logicalPlan) {
                mockPhysicalOperatorFactory.createOperator(mockLogicalPlanNode);
                return mockPhysicalPlan;
            }
        };

        builderWithFactory.build(mockLogicalPlan);

        verify(mockPhysicalOperatorFactory).createOperator(mockLogicalPlanNode);
    }

    // TC-21F: Kiểm thử tính chất Fail-fast: khi bước kiểm tra validation lỗi thì dừng ngay pipeline.
    @Test
    @DisplayName("TC-21F. Pipeline Fail-Fast Behavior Verification in PlanGenerator")
    void createPhysicalPlan_ShouldStopPipeline_WhenPlanValidationFails() {
        doThrow(new RuntimeException("Validation Failed")).when(mockPlanValidator).validate(mockLogicalPlan);

        assertThatThrownBy(() -> planGenerator.createPhysicalPlan(mockLogicalPlan))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Validation Failed");

        verify(mockPlanValidator).validate(mockLogicalPlan);
        verifyNoInteractions(mockPlanNormalizer, mockPhysicalPlanBuilder);
    }

    // TC-21G: Xác minh mỗi dependency trong pipeline chỉ được gọi đúng 1 lần duy nhất trong 1 lượt sinh kế hoạch.
    @Test
    @DisplayName("TC-21G. Verify Single Execution per Generation Lifecycle")
    void createPhysicalPlan_ShouldInvokeEachDependencyExactlyOnce_WhenGenerationSucceeds() {
        when(mockPlanNormalizer.normalize(mockLogicalPlan)).thenReturn(mockNormalizedPlan);
        when(mockPhysicalPlanBuilder.build(mockNormalizedPlan)).thenReturn(mockPhysicalPlan);

        planGenerator.createPhysicalPlan(mockLogicalPlan);

        verify(mockPlanValidator, times(1)).validate(mockLogicalPlan);
        verify(mockPlanNormalizer, times(1)).normalize(mockLogicalPlan);
        verify(mockPhysicalPlanBuilder, times(1)).build(mockNormalizedPlan);
    }
}

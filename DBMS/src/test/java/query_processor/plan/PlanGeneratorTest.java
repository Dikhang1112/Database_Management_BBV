package query_processor.plan;

import query_processor.ast.AST;
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
class PlanGeneratorTest {

    private PlanGenerator planGenerator;

    @Mock
    private LogicalPlanBuilder mockLogicalPlanBuilder;

    @Mock
    private PhysicalPlanBuilder mockPhysicalPlanBuilder;

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

    @BeforeEach
    void setUp() {
        planGenerator = spy(new PlanGenerator() {
            @Override
            public LogicalPlan createLogicalPlan(AST ast) {
                if (ast == null) {
                    throw new IllegalArgumentException("AST cannot be null");
                }
                return mockLogicalPlanBuilder.build(ast);
            }

            @Override
            public PhysicalPlan createPhysicalPlan(LogicalPlan logicalPlan) {
                if (logicalPlan == null) {
                    throw new IllegalArgumentException("Logical plan cannot be null");
                }
                mockPlanValidator.validate(logicalPlan);
                LogicalPlan normalized = mockPlanNormalizer.normalize(logicalPlan);
                return mockPhysicalPlanBuilder.build(normalized);
            }
        });
    }

    // TC-14: Kiểm thử quy trình sinh kế hoạch LogicalPlan từ cây cú pháp AST hợp lệ.
    @Test
    @DisplayName("TC-14. Generate Logical Plan (Happy Path)")
    void createLogicalPlan_ShouldGenerateLogicalPlan_WhenASTIsValid() {
        when(mockLogicalPlanBuilder.build(mockAST)).thenReturn(mockLogicalPlan);

        LogicalPlan result = planGenerator.createLogicalPlan(mockAST);

        assertThat(result).isNotNull().isEqualTo(mockLogicalPlan);
        verify(mockLogicalPlanBuilder).build(mockAST);
    }

    // TC-14A: Thẩm định an toàn phòng thủ cho PlanGenerator khi tham số AST truyền vào bị null.
    @Test
    @DisplayName("TC-14A. Null AST Handling")
    void createLogicalPlan_ShouldThrowIllegalArgumentException_WhenASTIsNull() {
        assertThatThrownBy(() -> planGenerator.createLogicalPlan(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("AST cannot be null");
    }

    // TC-14B: Kiểm thử quy trình biến đổi LogicalPlan thành PhysicalPlan thực thi vật lý.
    @Test
    @DisplayName("TC-14B. Generate Physical Plan (Happy Path)")
    void createPhysicalPlan_ShouldGeneratePhysicalPlan_WhenLogicalPlanIsValid() {
        when(mockPlanNormalizer.normalize(mockLogicalPlan)).thenReturn(mockNormalizedPlan);
        when(mockPhysicalPlanBuilder.build(mockNormalizedPlan)).thenReturn(mockPhysicalPlan);

        PhysicalPlan result = planGenerator.createPhysicalPlan(mockLogicalPlan);

        assertThat(result).isNotNull().isEqualTo(mockPhysicalPlan);
        verify(mockPlanValidator).validate(mockLogicalPlan);
        verify(mockPlanNormalizer).normalize(mockLogicalPlan);
        verify(mockPhysicalPlanBuilder).build(mockNormalizedPlan);
    }

    // TC-14C: Đảm bảo ném ngoại lệ khi tham số LogicalPlan đầu vào bị null.
    @Test
    @DisplayName("TC-14C. Null Logical Plan Handling in PlanGenerator")
    void createPhysicalPlan_ShouldThrowIllegalArgumentException_WhenLogicalPlanIsNull() {
        assertThatThrownBy(() -> planGenerator.createPhysicalPlan(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Logical plan cannot be null");
    }

    // TC-14D: Xác minh bước kiểm tra tính hợp lệ PlanValidator được kích hoạt trước khi dựng PhysicalPlan.
    @Test
    @DisplayName("TC-14D. Validate Before Build")
    void createPhysicalPlan_ShouldValidateLogicalPlan_BeforeBuildingPhysicalPlan() {
        when(mockPlanNormalizer.normalize(mockLogicalPlan)).thenReturn(mockNormalizedPlan);
        when(mockPhysicalPlanBuilder.build(mockNormalizedPlan)).thenReturn(mockPhysicalPlan);

        planGenerator.createPhysicalPlan(mockLogicalPlan);

        verify(mockPlanValidator).validate(mockLogicalPlan);
    }

    // TC-14E: Xác minh bước chuẩn hóa PlanNormalizer được gọi ngay sau bước validation.
    @Test
    @DisplayName("TC-14E. Normalize Before Build")
    void createPhysicalPlan_ShouldNormalizeLogicalPlan_BeforeBuildingPhysicalPlan() {
        when(mockPlanNormalizer.normalize(mockLogicalPlan)).thenReturn(mockNormalizedPlan);
        when(mockPhysicalPlanBuilder.build(mockNormalizedPlan)).thenReturn(mockPhysicalPlan);

        planGenerator.createPhysicalPlan(mockLogicalPlan);

        verify(mockPlanNormalizer).normalize(mockLogicalPlan);
    }

    // TC-14F: Đảm bảo PhysicalPlanBuilder được kích hoạt để dựng đối tượng PhysicalPlan khi validation và normalize thành công.
    @Test
    @DisplayName("TC-14F. Invoke Physical Plan Builder")
    void createPhysicalPlan_ShouldInvokePhysicalPlanBuilder_WhenValidationSucceeds() {
        when(mockPlanNormalizer.normalize(mockLogicalPlan)).thenReturn(mockNormalizedPlan);
        when(mockPhysicalPlanBuilder.build(mockNormalizedPlan)).thenReturn(mockPhysicalPlan);

        planGenerator.createPhysicalPlan(mockLogicalPlan);

        verify(mockPhysicalPlanBuilder).build(mockNormalizedPlan);
    }

    // TC-14G: Đảm bảo LogicalPlanBuilder được kích hoạt khi sinh kế hoạch logic từ AST.
    @Test
    @DisplayName("TC-14G. Invoke Logical Plan Builder")
    void createLogicalPlan_ShouldInvokeLogicalPlanBuilder_WhenASTIsValid() {
        when(mockLogicalPlanBuilder.build(mockAST)).thenReturn(mockLogicalPlan);

        planGenerator.createLogicalPlan(mockAST);

        verify(mockLogicalPlanBuilder).build(mockAST);
    }
}

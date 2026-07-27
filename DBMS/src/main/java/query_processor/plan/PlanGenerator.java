package query_processor.plan;

import query_processor.ast.AST;
import query_processor.external.ExecutionEngine;

/**
 * Điều phối sinh Kế hoạch Thực thi Logic và Kế hoạch Thực thi Vật lý.
 */
public class PlanGenerator {

    private final LogicalPlanBuilder logicalPlanBuilder;
    private final PhysicalPlanBuilder physicalPlanBuilder;
    private final PlanValidator planValidator;
    private final PlanNormalizer planNormalizer;
    private ExecutionEngine executionEngine;

    public PlanGenerator() {
        this(new LogicalPlanBuilder(), new PhysicalPlanBuilder(), new PlanValidator(), new PlanNormalizer());
    }

    public PlanGenerator(LogicalPlanBuilder logicalPlanBuilder,
                         PhysicalPlanBuilder physicalPlanBuilder,
                         PlanValidator planValidator,
                         PlanNormalizer planNormalizer) {
        this.logicalPlanBuilder = logicalPlanBuilder != null ? logicalPlanBuilder : new LogicalPlanBuilder();
        this.physicalPlanBuilder = physicalPlanBuilder != null ? physicalPlanBuilder : new PhysicalPlanBuilder();
        this.planValidator = planValidator != null ? planValidator : new PlanValidator();
        this.planNormalizer = planNormalizer != null ? planNormalizer : new PlanNormalizer();
    }

    public PlanGenerator(ExecutionEngine executionEngine) {
        this();
        this.executionEngine = executionEngine;
    }

    /**
     * Sinh kế hoạch thực thi logic LogicalPlan từ cây cú pháp AST.
     *
     * @param ast Cây cú pháp trừu tượng AST.
     * @return LogicalPlan hoàn chỉnh.
     */
    public LogicalPlan createLogicalPlan(AST ast) {
        if (ast == null) {
            throw new IllegalArgumentException("AST cannot be null");
        }
        return logicalPlanBuilder.build(ast);
    }

    /**
     * Sinh kế hoạch vật lý PhysicalPlan từ LogicalPlan qua 3 bước: Validate -> Normalize -> Build.
     *
     * @param logicalPlan Kế hoạch thực thi logic.
     * @return PhysicalPlan hoàn chỉnh cho ExecutionEngine.
     */
    public PhysicalPlan createPhysicalPlan(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        planValidator.validate(logicalPlan);
        LogicalPlan normalizedPlan = planNormalizer.normalize(logicalPlan);
        return physicalPlanBuilder.build(normalizedPlan);
    }
}

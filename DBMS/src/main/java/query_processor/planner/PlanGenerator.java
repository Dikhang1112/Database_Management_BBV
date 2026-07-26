package query_processor.planner;

import query_processor.external.ExecutionEngine;

/**
 * Điều phối sinh Kế hoạch Vật lý cho ExecutionEngine.
 */
public class PlanGenerator {

    private final ExecutionEngine executionEngine;

    /**
     * Khởi tạo PlanGenerator với Constructor Dependency Injection.
     *
     * @param executionEngine Engine thực thi truy vấn.
     */
    public PlanGenerator(ExecutionEngine executionEngine) {
        this.executionEngine = executionEngine;
        // TODO: Future DBMS logic implementation
    }

    /**
     * Sinh kế hoạch vật lý PhysicalPlan từ LogicalPlan.
     *
     * @param plan Kế hoạch thực thi logic.
     * @return PhysicalPlan hoàn chỉnh.
     */
    public PhysicalPlan createPhysicalPlan(LogicalPlan plan) {
        // TODO: Future DBMS logic implementation
        return null;
    }
}

package query_processor.optimizer;

import query_processor.interfaces.CompilerStage;
import query_processor.interfaces.OptimizationRule;
import query_processor.ast.AST;
import query_processor.planner.PhysicalPlan;

/**
 * Bộ tối ưu hóa truy vấn dựa trên chi phí CBO (Cost-Based Optimization).
 * Pattern: Chain of Responsibility, Strategy Context.
 */
public class QueryOptimizer implements CompilerStage {

    private OptimizationRule optimizationRule;
    private final CostEstimator costEstimator;

    /**
     * Khởi tạo QueryOptimizer với Constructor Dependency Injection cho CostEstimator.
     *
     * @param costEstimator Bộ ước lượng chi phí CBO.
     */
    public QueryOptimizer(CostEstimator costEstimator) {
        this.costEstimator = costEstimator;
        // TODO: Future DBMS logic implementation
    }

    /**
     * Thực thi giai đoạn QueryOptimizer theo giao diện CompilerStage.
     *
     * @param input Đối tượng cây AST.
     * @return Kế hoạch vật lý PhysicalPlan.
     */
    @Override
    public Object process(Object input) {
        if (input instanceof AST ast) {
            return process(ast);
        }
        // TODO: Future DBMS logic implementation
        return null;
    }

    /**
     * Tối ưu cây AST và sinh kế hoạch vật lý tối ưu.
     *
     * @param ast Cây AST đầu vào.
     * @return Kế hoạch vật lý PhysicalPlan.
     */
    public PhysicalPlan process(AST ast) {
        // TODO: Future DBMS logic implementation
        return null;
    }

    /**
     * Thiết lập chiến lược quy tắc tối ưu hóa mới.
     *
     * @param rule Chiến lược OptimizationRule.
     */
    public void setOptimizationRule(OptimizationRule rule) {
        this.optimizationRule = rule;
        // TODO: Future DBMS logic implementation
    }
}

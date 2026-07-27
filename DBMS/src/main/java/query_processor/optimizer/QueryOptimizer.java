package query_processor.optimizer;

import query_processor.plan.LogicalPlan;
import query_processor.plan.PhysicalPlan;
import query_processor.interfaces.OptimizationRule;
import query_processor.interfaces.CompilerStage;

public class QueryOptimizer implements CompilerStage {

    private QueryRewriter queryRewriter;
    private JoinOptimizer joinOptimizer;
    private CostEstimator costEstimator;
    private PlanEnumerator planEnumerator;
    private OptimizationRule optimizationRule;

    public QueryOptimizer() {
        this.queryRewriter = new QueryRewriter();
        this.joinOptimizer = new JoinOptimizer();
        this.costEstimator = new CostEstimator();
        this.planEnumerator = new PlanEnumerator();
    }

    public QueryOptimizer(QueryRewriter queryRewriter,
                          JoinOptimizer joinOptimizer,
                          CostEstimator costEstimator,
                          PlanEnumerator planEnumerator) {
        this.queryRewriter = queryRewriter;
        this.joinOptimizer = joinOptimizer;
        this.costEstimator = costEstimator;
        this.planEnumerator = planEnumerator;
    }

    public PhysicalPlan optimize(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        LogicalPlan rewritten = (queryRewriter != null) ? queryRewriter.rewrite(logicalPlan) : logicalPlan;
        LogicalPlan joinOptimized = (joinOptimizer != null) ? joinOptimizer.optimize(rewritten) : rewritten;
        if (costEstimator != null) {
            costEstimator.estimate(joinOptimized);
        }
        return (planEnumerator != null) ? planEnumerator.enumerate(joinOptimized) : new PhysicalPlan();
    }

    public void setOptimizationRule(OptimizationRule rule) {
        this.optimizationRule = rule;
    }

    public OptimizationRule getOptimizationRule() {
        return optimizationRule;
    }

    @Override
    public Object process(Object input) {
        if (input instanceof LogicalPlan plan) {
            return optimize(plan);
        }
        return null;
    }
}

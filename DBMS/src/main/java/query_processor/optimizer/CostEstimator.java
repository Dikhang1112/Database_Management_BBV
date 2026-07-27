package query_processor.optimizer;

import query_processor.planner.LogicalPlan;

public class CostEstimator {

    private StatisticsManager statisticsManager;

    public CostEstimator() {
        this.statisticsManager = new StatisticsManager();
    }

    public CostEstimator(StatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

    public double estimate(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        double cardinality = estimateCardinality(logicalPlan);
        if (cardinality <= 0) {
            cardinality = 1000.0;
        }
        double selectivity = estimateSelectivity(logicalPlan);
        if (selectivity <= 0) {
            selectivity = 0.1;
        }
        return cardinality * selectivity * 1.5;
    }

    public double estimateCardinality(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        return (statisticsManager != null) ? statisticsManager.estimateCardinality() : 1000.0;
    }

    public double estimateSelectivity(LogicalPlan logicalPlan) {
        if (logicalPlan == null) {
            throw new IllegalArgumentException("Logical plan cannot be null");
        }
        return (statisticsManager != null) ? statisticsManager.estimateSelectivity() : 0.1;
    }

    public StatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    public void setStatisticsManager(StatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }
}

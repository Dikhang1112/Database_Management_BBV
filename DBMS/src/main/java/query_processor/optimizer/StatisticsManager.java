package query_processor.optimizer;

public abstract class StatisticsManager {

    public abstract double estimateCardinality();

    public abstract double estimateSelectivity();
}

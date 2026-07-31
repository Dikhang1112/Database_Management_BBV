package entity.query_processor.optimizer;

public class StatisticsManager {

    private double defaultCardinality = 10000.0;
    private double defaultSelectivity = 0.05;

    public StatisticsManager() {
    }

    public StatisticsManager(double defaultCardinality, double defaultSelectivity) {
        this.defaultCardinality = defaultCardinality;
        this.defaultSelectivity = defaultSelectivity;
    }

    public double estimateCardinality() {
        return defaultCardinality;
    }

    public double estimateSelectivity() {
        return defaultSelectivity;
    }

    public double getDefaultCardinality() {
        return defaultCardinality;
    }

    public void setDefaultCardinality(double defaultCardinality) {
        this.defaultCardinality = defaultCardinality;
    }

    public double getDefaultSelectivity() {
        return defaultSelectivity;
    }

    public void setDefaultSelectivity(double defaultSelectivity) {
        this.defaultSelectivity = defaultSelectivity;
    }
}

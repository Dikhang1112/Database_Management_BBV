package entity.execution_engine.operators;

import entity.execution_engine.abstracts.LeafOperator;
import entity.execution_engine.helpers.PredicateEvaluator;
import entity.execution_engine.helpers.TupleFetcher;
import entity.execution_engine.domain.Tuple;
import entity.execution_engine.strategy.ScanStrategy;
import entity.execution_engine.strategy.SequentialScanStrategy;

public class ScanOperator extends LeafOperator {

    private ScanStrategy scanStrategy;
    private TupleFetcher tupleFetcher;
    private PredicateEvaluator predicateEvaluator;

    public ScanOperator() {
        this(new SequentialScanStrategy());
    }

    public ScanOperator(ScanStrategy scanStrategy) {
        this.scanStrategy = scanStrategy != null ? scanStrategy : new SequentialScanStrategy();
        this.tupleFetcher = new TupleFetcher();
        this.predicateEvaluator = new PredicateEvaluator();
    }

    @Override
    public void open() {
        super.open();
        initialize();
    }

    @Override
    public Tuple next() {
        super.next();
        return fetchTuple();
    }

    @Override
    public void close() {
        super.close();
    }

    protected void initialize() {
    }

    protected Tuple fetchTuple() {
        if (scanStrategy != null) {
            return scanStrategy.scan();
        }
        return tupleFetcher != null ? tupleFetcher.fetch() : null;
    }

    public ScanStrategy getScanStrategy() {
        return scanStrategy;
    }

    public void setScanStrategy(ScanStrategy scanStrategy) {
        this.scanStrategy = scanStrategy;
    }
}

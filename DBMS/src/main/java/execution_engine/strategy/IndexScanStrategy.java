package execution_engine.strategy;

import execution_engine.domain.Tuple;

public class IndexScanStrategy implements ScanStrategy {

    @Override
    public Tuple scan() {
        System.out.println("  -> [ScanStrategy: Index Scan] Scanning B+Tree Index.");
        return null;
    }
}

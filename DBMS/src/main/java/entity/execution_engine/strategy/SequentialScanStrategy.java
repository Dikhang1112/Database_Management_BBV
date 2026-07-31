package entity.execution_engine.strategy;

import entity.execution_engine.domain.Tuple;

public class SequentialScanStrategy implements ScanStrategy {

    @Override
    public Tuple scan() {
        System.out.println("  -> [ScanStrategy: Sequential Scan] Full Table Scan.");
        return null;
    }
}

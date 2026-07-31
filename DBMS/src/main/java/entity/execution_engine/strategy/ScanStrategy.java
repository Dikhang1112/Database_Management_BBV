package entity.execution_engine.strategy;

import entity.execution_engine.domain.Tuple;

public interface ScanStrategy {

    Tuple scan();
}

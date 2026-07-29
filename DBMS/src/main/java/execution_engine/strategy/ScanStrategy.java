package execution_engine.strategy;

import execution_engine.domain.Tuple;

public interface ScanStrategy {

    Tuple scan();
}

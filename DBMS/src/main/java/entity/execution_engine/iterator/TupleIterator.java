package entity.execution_engine.iterator;

import entity.execution_engine.domain.Tuple;
import java.util.Iterator;

public interface TupleIterator extends Iterator<Tuple> {

    @Override
    boolean hasNext();

    @Override
    Tuple next();
}

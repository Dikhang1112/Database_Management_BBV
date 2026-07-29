package execution_engine.iterator;

import execution_engine.domain.Tuple;
import java.util.Iterator;

public interface TupleIterator extends Iterator<Tuple> {

    @Override
    boolean hasNext();

    @Override
    Tuple next();
}

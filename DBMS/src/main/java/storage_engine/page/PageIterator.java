package storage_engine.page;

import java.util.Iterator;

public class PageIterator implements Iterator<Record> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Record next() {
        return null;
    }
}

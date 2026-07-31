package entity.storage_engine.page;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PageIterator implements Iterator<Record> {

    private final List<Record> records;
    private int currentIndex = 0;

    public PageIterator(List<Record> records) {
        this.records = records;
    }

    @Override
    public boolean hasNext() {
        return records != null && currentIndex < records.size();
    }

    @Override
    public Record next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Không còn Record nào trong Page!");
        }
        return records.get(currentIndex++);
    }
}

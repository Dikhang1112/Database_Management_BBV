package storage_engine.index;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BTreeIterator implements Iterator<Object> {

    private final List<Object> entries;
    private int currentIndex = 0;

    public BTreeIterator(LeafNode leafNode) {
        this.entries = new ArrayList<>();
        if (leafNode != null && leafNode.getDataEntries() != null) {
            this.entries.addAll(leafNode.getDataEntries().values());
        }
    }

    @Override
    public boolean hasNext() {
        return currentIndex < entries.size();
    }

    @Override
    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Không còn phần tử nào trên nút lá B+Tree!");
        }
        return entries.get(currentIndex++);
    }
}

package metadata.helpers;

import metadata.Index;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IndexManager {
    private final Map<String, Index> indexes = new ConcurrentHashMap<>();

    public void add(Index index) {
        if (index == null || index.getIndexName() == null) {
            throw new IllegalArgumentException("Value is empty");
        }
        String nameIndex = index.getIndexName();
        CatalogValidator.validateIdentifier(nameIndex, "Index");
        if (contains(nameIndex)) {
            throw new IllegalStateException("Duplicate index name");
        }
        indexes.put(nameIndex.toLowerCase(), index);
    }

    public void remove(String indexName) {
        CatalogValidator.validateIdentifier(indexName, "Index");
        if (!contains(indexName)) {
            throw new IllegalArgumentException("Index not found");
        }
        indexes.remove(indexName.toLowerCase());
    }

    public Index get(String indexName) {
        if (indexName == null) return null;
        return indexes.get(indexName.toLowerCase());
    }

    public boolean contains(String indexName) {
        if (indexName == null) return false;
        return indexes.containsKey(indexName.toLowerCase());
    }

    public List<Index> listAll() {
        return Collections.unmodifiableList(new ArrayList<>(indexes.values()));
    }
}

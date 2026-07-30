package storage_engine.index;

import storage_engine.abstracts.BTreeNode;

import java.util.HashMap;
import java.util.Map;

public class LeafNode extends BTreeNode {

    private final Map<Object, Object> dataEntries = new HashMap<>();

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Object search(Object key) {
        Object val = dataEntries.get(key);
        System.out.println("  -> [LeafNode (Leaf)] Tìm thấy dữ liệu tại nút lá! Key = " + key + " => Value = " + val);
        return val;
    }

    @Override
    public void insert(Object key, Object value) {
        dataEntries.put(key, value);
    }

    @Override
    public BTreeNode split() {
        System.out.println("  -> [LeafNode] Tách nút lá B+Tree khi đầy...");
        return new LeafNode();
    }

    public Map<Object, Object> getDataEntries() {
        return dataEntries;
    }
}

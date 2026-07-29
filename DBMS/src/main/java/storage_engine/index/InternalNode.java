package storage_engine.index;

import storage_engine.abstracts.BTreeNode;

import java.util.ArrayList;
import java.util.List;

public class InternalNode extends BTreeNode {

    private final List<Object> keys = new ArrayList<>();
    private final List<BTreeNode> children = new ArrayList<>();

    public InternalNode() {
    }

    public void addChild(Object key, BTreeNode child) {
        keys.add(key);
        children.add(child);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public BTreeNode findChild(Object key) {
        if (children.isEmpty()) return null;
        for (int i = 0; i < keys.size(); i++) {
            if (key instanceof Comparable && ((Comparable) key).compareTo(keys.get(i)) < 0) {
                return children.get(i);
            }
        }
        return children.get(children.size() - 1);
    }

    @Override
    public Object search(Object key) {
        System.out.println("  -> [InternalNode (Composite)] Điều hướng qua nút trong...");
        BTreeNode child = findChild(key);
        if (child != null) {
            return child.search(key);
        }
        return null;
    }

    @Override
    public void insert(Object key, Object value) {
        BTreeNode child = findChild(key);
        if (child != null) {
            child.insert(key, value);
        } else {
            LeafNode newLeaf = new LeafNode();
            newLeaf.insert(key, value);
            addChild(key, newLeaf);
        }
    }

    @Override
    public BTreeNode split() {
        System.out.println("  -> [InternalNode] Tách nút trong B+Tree khi vượt quá dung lượng...");
        return new InternalNode();
    }

    public List<BTreeNode> getChildren() {
        return children;
    }
}

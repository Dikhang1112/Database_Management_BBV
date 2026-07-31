package entity.storage_engine.abstracts;

public abstract class BTreeNode {

    public abstract Object search(Object key);

    public abstract void insert(Object key, Object value);

    public abstract BTreeNode split();

    public boolean isLeaf() {
        return false;
    }
}

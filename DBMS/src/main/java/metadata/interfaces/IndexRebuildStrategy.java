package metadata.interfaces;

import metadata.Index;

/**
 * Interface cho Strategy Pattern trong metadata module.
 * Định nghĩa chiến lược rebuild lại chỉ mục trong Index.
 */
public interface IndexRebuildStrategy {
    void rebuildIndex(Index index);
}

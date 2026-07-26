package metadata.interfaces;

import metadata.domain.Index;

/**
 * Interface cho Strategy Pattern định nghĩa thuật toán tái cấu trúc chỉ mục.
 */
public interface IndexRebuildStrategy {
    void rebuildIndex(Index index);
}

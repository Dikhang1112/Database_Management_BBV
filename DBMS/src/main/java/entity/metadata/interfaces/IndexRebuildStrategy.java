package entity.metadata.interfaces;

import entity.metadata.domain.Index;

/**
 * Interface cho Strategy Pattern định nghĩa thuật toán tái cấu trúc chỉ mục.
 */
public interface IndexRebuildStrategy {
    void rebuildIndex(Index index);
}

package metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Class hỗ trợ Memento Pattern cho Metadata module.
 * Lưu lại ảnh chụp (Snapshot) cấu trúc của Table để phục vụ cho tính năng Rollback.
 */
public class TableMemento {
    private String tableName;
    private List<Column> columnsSnapshot;

    public TableMemento(String tableName, List<Column> columns) {
        this.tableName = tableName;
        this.columnsSnapshot = columns != null ? new ArrayList<>(columns) : new ArrayList<>();
    }

    // Pattern: Memento
    public String getTableName() {
        return tableName;
    }

    // Pattern: Memento
    public List<Column> getColumnsSnapshot() {
        return new ArrayList<>(columnsSnapshot);
    }
}

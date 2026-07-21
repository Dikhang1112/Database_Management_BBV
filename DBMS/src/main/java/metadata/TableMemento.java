package metadata;

import java.util.List;

/**
 * Class hỗ trợ Memento Pattern cho Metadata module (Method nobody).
 * Lưu lại ảnh chụp (Snapshot) cấu trúc của Table để phục vụ cho tính năng Rollback.
 */
public class TableMemento {
    private String tableName;
    private List<Column> columnsSnapshot;

    public TableMemento(String tableName, List<Column> columns) {
    }

    // Pattern: Memento (Method nobody)
    public String getTableName() {
        return null;
    }

    // Pattern: Memento (Method nobody)
    public List<Column> getColumnsSnapshot() {
        return null;
    }
}

package entity.metadata.domain;

import java.util.ArrayList;
import java.util.List;

public class TableMemento {
    private final String tableName;
    private final List<Column> columnsSnapshot;

    public TableMemento(String tableName, List<Column> columns) {
        this.tableName = tableName;
        this.columnsSnapshot = new ArrayList<>();
        for (Column col : columns) {
            this.columnsSnapshot.add(col.clone());
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<Column> getColumnsSnapshot() {
        return new ArrayList<>(columnsSnapshot);
    }
}

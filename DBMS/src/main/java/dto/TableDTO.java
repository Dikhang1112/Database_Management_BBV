package dto;

import entity.metadata.domain.Table;

import java.util.List;

public class TableDTO {
    private final String tableName;
    private final String schemaName;
    private final String databaseName;
    private final int columnCount;
    private final List<ColumnDTO> columns;

    public TableDTO(String databaseName, String schemaName, Table table) {
        this.databaseName = databaseName;
        this.schemaName = schemaName;
        this.tableName = table.getTableName();
        this.columnCount = table.listColumns() != null ? table.listColumns().size() : 0;
        this.columns = table.listColumns() != null ? 
                table.listColumns().stream().map(ColumnDTO::new).toList() : List.of();
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public List<ColumnDTO> getColumns() {
        return columns;
    }
}

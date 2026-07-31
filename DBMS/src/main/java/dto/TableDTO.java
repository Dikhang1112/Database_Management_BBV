package dto;

import entity.metadata.domain.Column;
import entity.metadata.domain.Table;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class TableDTO {
    @Schema(description = "Tên Bảng", example = "users")
    private final String tableName;

    @Schema(description = "Trạng thái khóa Bảng", example = "false")
    private final boolean locked;

    @Schema(description = "Số lượng Cột trong Bảng", example = "3")
    private final int columnCount;

    @Schema(description = "Danh sách chi tiết các Cột")
    private final List<ColumnDTO> columns;

    public TableDTO(Table table) {
        this.tableName = table.getTableName();
        this.locked = table.isLocked();
        List<Column> cols = table.listColumns();
        this.columnCount = cols != null ? cols.size() : 0;
        this.columns = cols != null ? cols.stream().map(ColumnDTO::new).toList() : List.of();
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public List<ColumnDTO> getColumns() {
        return columns;
    }
}

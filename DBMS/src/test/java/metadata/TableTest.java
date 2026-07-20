package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TableTest {

    @Test
    @DisplayName("TC-09. Add & Manage Columns")
    void addColumn_ShouldAttachColumnToTable_WhenColumnIsAdded() {
        // Arrange
        Table table = new Table("orders");
        Column column = new Column("order_id", DataType.BIGINT);

        // Act
        table.addColumn(column);
        List<Column> columns = table.listColumns();

        // Assert
        assertThat(columns).contains(column);
        assertThat(table.containsColumn("order_id")).isTrue();
    }
}

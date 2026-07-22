package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableMementoTest {

    @Test
    @DisplayName("TC-21. Memento Snapshot and Restore")
    void createMemento_ShouldCaptureSnapshot_And_restore_ShouldRevertState() {
        Table table = new Table("products");
        table.addColumn(new Column("id", DataType.INT));

        TableMemento memento = table.createMemento();
        assertThat(memento.getTableName()).isEqualTo("products");

        table.addColumn(new Column("temp_col", DataType.VARCHAR));
        assertThat(table.listColumns()).hasSize(2);

        table.restore(memento);

        assertThat(table.listColumns()).hasSize(1);
        assertThat(table.containsColumn("temp_col")).isFalse();
    }
}

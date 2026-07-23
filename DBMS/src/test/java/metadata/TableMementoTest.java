package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class TableMementoTest {

    @Test
    @DisplayName("TC-20. Memento Snapshot and Restore")
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

    @Test
    @DisplayName("TC-20A. Restore with Null Memento")
    void restore_ShouldDoNothing_WhenMementoIsNull() {
        Table table = new Table("users");
        table.addColumn(new Column("id", DataType.INT));

        assertThatCode(() -> table.restore(null)).doesNotThrowAnyException();

        assertThat(table.getTableName()).isEqualTo("users");
        assertThat(table.listColumns()).hasSize(1);
    }

    @Test
    @DisplayName("TC-20B. Restore Table Name and Columns")
    void restore_ShouldRevertTableNameAndColumns_WhenRenamedAndColumnsRemoved() {
        Table table = new Table("old_users");
        table.addColumn(new Column("id", DataType.INT));
        TableMemento memento = table.createMemento();

        table.rename("new_users");
        table.removeColumn("id");

        table.restore(memento);

        assertThat(table.getTableName()).isEqualTo("old_users");
        assertThat(table.containsColumn("id")).isTrue();
    }

    @Test
    @DisplayName("TC-20C. Memento Snapshot Immutability")
    void createMemento_ShouldMaintainIndependentSnapshot_WhenTableIsModified() {
        Table table = new Table("orders");
        table.addColumn(new Column("order_id", DataType.INT));

        TableMemento memento = table.createMemento();
        table.addColumn(new Column("total", DataType.BIGINT));

        assertThat(memento.getColumnsSnapshot()).hasSize(1);
    }

    @Test
    @DisplayName("TC-20D. Restore to Empty Columns")
    void restore_ShouldRevertToEmptyColumns_WhenSnapshotWasEmpty() {
        Table table = new Table("empty_table");
        TableMemento memento = table.createMemento();

        table.addColumn(new Column("col1", DataType.INT));
        table.restore(memento);

        assertThat(table.listColumns()).isEmpty();
    }
}

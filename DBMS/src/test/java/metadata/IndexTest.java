package metadata;

import metadata.enums.DataType;
import metadata.enums.IndexType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IndexTest {

    @Test
    @DisplayName("TC-13. Add Index")
    void addIndex_ShouldAttachIndexToTable_WhenIndexIsAddedAndRebuilt() {
        Table table = new Table("users");
        Index index = new Index("idx_user_email", IndexType.BTREE);

        table.addIndex(index);
        index.disable();
        index.rebuild();

        assertThat(table.listIndexes()).contains(index);
        assertThat(index.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("TC-13A. Add Index - Duplicate Index Name")
    void addIndex_ShouldThrowException_WhenDuplicateIndexName() {
        Table table = new Table("users");
        Index index1 = new Index("idx_user_email", IndexType.BTREE);
        table.addIndex(index1);

        Index index2 = new Index("idx_user_email", IndexType.BTREE);

        assertThatThrownBy(() -> table.addIndex(index2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate index name");
    }

    @Test
    @DisplayName("TC-13B. Add Index - Column Not Found")
    void addIndex_ShouldThrowException_WhenIndexedColumnNotFound() {
        Table table = new Table("users");
        Index index = new Index("idx_missing_col", IndexType.BTREE);
        index.setColumns(List.of(new Column("non_existing_col", DataType.VARCHAR)));

        assertThatThrownBy(() -> table.addIndex(index))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Indexed column not found");
    }

    @Test
    @DisplayName("TC-13D. Remove Index")
    void removeIndex_ShouldRemoveIndexFromTable_WhenIndexExists() {
        Table table = new Table("users");
        Index index = new Index("idx_user_email", IndexType.BTREE);
        table.addIndex(index);

        table.removeIndex("idx_user_email");

        assertThat(table.listIndexes()).doesNotContain(index);
    }

    @Test
    @DisplayName("TC-13E. Remove Index - Index Not Found")
    void removeIndex_ShouldThrowException_WhenIndexNotFound() {
        Table table = new Table("users");

        assertThatThrownBy(() -> table.removeIndex("missing_idx"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Index not found");
    }

    @Test
    @DisplayName("TC-13F. Remove Index - Table Locked")
    void removeIndex_ShouldThrowException_WhenTableIsLocked() {
        Table table = new Table("users");
        Index index = new Index("idx_user_email", IndexType.BTREE);
        table.addIndex(index);
        table.setLocked(true);

        assertThatThrownBy(() -> table.removeIndex("idx_user_email"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Table is locked");
    }

    @Test
    @DisplayName("TC-13G. Remove Index - Invalid Name Format")
    void removeIndex_ShouldThrowException_WhenIndexNameIsInvalid() {
        Table table = new Table("users");

        assertThatThrownBy(() -> table.removeIndex(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Value is empty");
    }

    @Test
    @DisplayName("TC-14. Rebuild Index")
    void rebuildIndex_ShouldReenableIndex_WhenRebuilt() {
        Index index = new Index("idx_user_email", IndexType.BTREE);
        index.disable();

        index.rebuild();

        assertThat(index.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("TC-14A. Rebuild Index - Disabled & Corrupted")
    void rebuildIndex_ShouldThrowException_WhenIndexIsDisabledAndCorrupted() {
        Index index = new Index("idx_corrupted", IndexType.BTREE);
        index.setCorrupted(true);

        assertThatThrownBy(() -> index.rebuild())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Index is corrupted");
    }
}

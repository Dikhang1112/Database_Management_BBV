package metadata;

import metadata.enums.IndexType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IndexTest {

    @Test
    @DisplayName("TC-13. Add Index to Table")
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

        assertThatThrownBy(() -> table.addIndex(new Index("idx_dup", IndexType.BTREE)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate index name");
    }

    @Test
    @DisplayName("TC-13B. Add Index - Column Not Found")
    void addIndex_ShouldThrowException_WhenIndexedColumnNotFound() {
        Table table = new Table("users");

        assertThatThrownBy(() -> table.addIndex(new Index("idx_missing_col", IndexType.BTREE)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Indexed column not found");
    }

    @Test
    @DisplayName("TC-14. Rebuild Index")
    void rebuildIndex_ShouldReenableIndex_WhenRebuilt() {
        Index index = new Index("idx_users", IndexType.BTREE);
        index.disable();

        index.rebuild();

        assertThat(index.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("TC-14A. Rebuild Index - Disabled & Corrupted")
    void rebuildIndex_ShouldThrowException_WhenIndexIsDisabledAndCorrupted() {
        Index index = new Index("idx_corrupted", IndexType.BTREE);
        index.disable();

        assertThatThrownBy(() -> index.rebuild())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Index is corrupted");
    }
}

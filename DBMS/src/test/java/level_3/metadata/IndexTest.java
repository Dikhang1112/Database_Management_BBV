package level_3.metadata;

import level_3.Metadata.Index;
import level_3.Metadata.IndexType;
import level_3.Metadata.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IndexTest {

    @Test
    @DisplayName("TC-11. Add Index to Table")
    void addIndex_ShouldAttachIndexToTable_WhenIndexIsAddedAndRebuilt() {
        // Arrange
        Table table = new Table("users");
        Index index = new Index("idx_user_email", IndexType.BTREE);

        // Act
        table.addIndex(index);
        index.disable();
        index.rebuild();

        // Assert
        assertThat(table.listIndexes()).contains(index);
    }
}

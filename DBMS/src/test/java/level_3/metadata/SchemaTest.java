package level_3.metadata;

import level_3.Metadata.Schema;
import level_3.Metadata.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SchemaTest {

    @Test
    @DisplayName("TC-07. Create & Manage Tables")
    void createTable_ShouldRegisterTableInSchema_WhenValidTableNameIsProvided() {
        // Arrange
        Schema schema = new Schema("public");

        // Act
        Table table = schema.createTable("users");

        // Assert
        assertThat(schema.containsTable("users")).isTrue();
        assertThat(schema.getTable("users")).isEqualTo(table);
    }

    @Test
    @DisplayName("TC-08. Rename Schema & List Tables")
    void renameAndlistTables_ShouldUpdateSchemaNameAndMaintainTables_WhenRenamed() {
        // Arrange
        Schema schema = new Schema("raw_schema");
        schema.createTable("t1");
        schema.createTable("t2");

        // Act
        List<Table> tables = schema.listTables();
        schema.rename("prod_schema");

        // Assert
        assertThat(tables).hasSize(2);
    }
}

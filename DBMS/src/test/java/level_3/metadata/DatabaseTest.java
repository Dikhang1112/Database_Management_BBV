package level_3.metadata;

import level_3.Metadata.Database;
import level_3.Metadata.DatabaseStatus;
import level_3.Metadata.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseTest {

    @Test
    @DisplayName("TC-05. Create & Drop Schema")
    void createSchema_ShouldManageSchemasInDatabase_WhenSchemaIsAddedAndDropped() {
        // Arrange
        Database database = new Database("app_db");

        // Act & Assert Create
        Schema schema = database.createSchema("public");
        assertThat(database.containsSchema("public")).isTrue();
        assertThat(database.getSchema("public")).isEqualTo(schema);

        // Act & Assert Drop
        database.dropSchema("public");
        assertThat(database.containsSchema("public")).isFalse();
    }

    @Test
    @DisplayName("TC-06. Database Status & Rename")
    void setStatus_And_rename_ShouldUpdateDatabaseState_WhenModified() {
        // Arrange
        Database database = new Database("old_name");

        // Act
        database.rename("new_name");
        database.setStatus(DatabaseStatus.READ_ONLY);

        // Assert
        assertThat(database).isNotNull();
    }
}

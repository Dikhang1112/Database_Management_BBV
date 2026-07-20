package metadata;

import metadata.enums.DatabaseStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DatabaseTest {

    @Test
    @DisplayName("TC-05. Create & Drop Schema")
    void createSchema_ShouldManageSchemasInDatabase_WhenSchemaIsAddedAndDropped() {
        Database database = new Database("app_db");

        Schema schema = database.createSchema("public");
        assertThat(database.containsSchema("public")).isTrue();
        assertThat(database.getSchema("public")).isEqualTo(schema);

        database.dropSchema("public");
        assertThat(database.containsSchema("public")).isFalse();
    }

    @Test
    @DisplayName("TC-05A. Create Schema - Already Exists")
    void createSchema_ShouldThrowException_WhenSchemaAlreadyExists() {
        Database database = new Database("app_db");
        database.createSchema("public");

        assertThatThrownBy(() -> database.createSchema("public"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Schema already exists");
    }

    @Test
    @DisplayName("TC-05B. Create Schema - Database Offline")
    void createSchema_ShouldThrowException_WhenDatabaseIsOffline() {
        Database database = new Database("app_db");
        database.setStatus(DatabaseStatus.OFFLINE);

        assertThatThrownBy(() -> database.createSchema("public"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Database is offline");
    }

    @Test
    @DisplayName("TC-05C. Create Schema - Permission Denied")
    void createSchema_ShouldThrowException_WhenPermissionDenied() {
        Database database = new Database("app_db");

        assertThatThrownBy(() -> database.createSchema("secure_schema"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Permission denied");
    }

    @Test
    @DisplayName("TC-06. Database Status & Rename")
    void setStatus_And_rename_ShouldUpdateDatabaseState_WhenModified() {
        Database database = new Database("old_name");

        database.rename("new_name");
        database.setStatus(DatabaseStatus.READ_ONLY);

        assertThat(database.getDatabaseName()).isEqualTo("new_name");
        assertThat(database.getStatus()).isEqualTo(DatabaseStatus.READ_ONLY);
    }

    @Test
    @DisplayName("TC-06A. Rename Database - Duplicate Name")
    void renameDatabase_ShouldThrowException_WhenDuplicateDatabaseName() {
        Database database = new Database("app_db");

        assertThatThrownBy(() -> database.rename("existing_db_name"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate database name");
    }

    @Test
    @DisplayName("TC-06B. Rename Database - Invalid Name")
    void renameDatabase_ShouldThrowException_WhenNameIsInvalid() {
        Database database = new Database("app_db");

        assertThatThrownBy(() -> database.rename(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid database name");
    }
}

package metadata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SchemaTest {

    @Test
    @DisplayName("TC-07. Create & Manage Tables")
    void createTable_ShouldRegisterTableInSchema_WhenValidTableNameIsProvided() {
        Schema schema = new Schema("public");

        Table table = schema.createTable("users");

        assertThat(schema.containsTable("users")).isTrue();
        assertThat(schema.getTable("users")).isEqualTo(table);
    }

    @Test
    @DisplayName("TC-07A. Create Table - Already Exists")
    void createTable_ShouldThrowException_WhenTableAlreadyExists() {
        Schema schema = new Schema("public");
        schema.createTable("users");

        assertThatThrownBy(() -> schema.createTable("users"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Table already exists");
    }

    @Test
    @DisplayName("TC-07B. Create Table - Permission Denied")
    void createTable_ShouldThrowException_WhenPermissionDenied() {
        Schema schema = new Schema("public");

        assertThatThrownBy(() -> schema.createTable("restricted_table"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Permission denied");
    }

    @Test
    @DisplayName("TC-07C. Create Table - Schema Read Only")
    void createTable_ShouldThrowException_WhenSchemaIsReadOnly() {
        Schema schema = new Schema("public");
        schema.setReadOnly(true);

        assertThatThrownBy(() -> schema.createTable("users"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Schema is read-only");
    }

    @Test
    @DisplayName("TC-07D. Create Table - Special Characters")
    void createTable_ShouldThrowException_WhenTableNameContainsSpecialCharacters() {
        Schema schema = new Schema("public");

        assertThatThrownBy(() -> schema.createTable("user@table!"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Table name contains invalid characters");
    }

    @Test
    @DisplayName("TC-08. Rename Schema & List Tables")
    void rename_And_listTables_ShouldUpdateSchemaNameAndMaintainTables_WhenRenamed() {
        Schema schema = new Schema("raw_schema");
        schema.createTable("t1");
        schema.createTable("t2");

        List<Table> tables = schema.listTables();
        schema.rename("prod_schema");

        assertThat(tables).hasSize(2);
        assertThat(schema.getSchemaName()).isEqualTo("prod_schema");
    }

    @Test
    @DisplayName("TC-08A. Rename Schema - Not Found")
    void renameSchema_ShouldThrowException_WhenSchemaNotFound() {
        Database database = new Database("app_db");

        assertThatThrownBy(() -> database.renameSchema("missing_schema", "new_name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Schema not found");
    }
}

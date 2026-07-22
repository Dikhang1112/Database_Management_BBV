package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class DDLCommandTest {

    private CatalogManager catalogManager;
    private Database database;
    private Schema schema;
    private Table table;
    private Column column;

    @BeforeEach
    void setUp() {
        catalogManager = CatalogManager.getInstance();
        catalogManager.clear();
        database = catalogManager.createDatabase("app_db");
        schema = database.createSchema("public");
        table = schema.createTable("users");
        column = new Column("id", DataType.INT);
    }

    @Test
    @DisplayName("TC-19A. Create Database Command")
    void execute_ShouldCreateDatabase_WhenCreateDatabaseCommandExecuted() {
        CreateDatabaseCommand cmd = new CreateDatabaseCommand("sales_db");

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-19B. Drop Database Command")
    void execute_ShouldDropDatabase_WhenDropDatabaseCommandExecuted() {
        catalogManager.createDatabase("temp_db");
        DropDatabaseCommand cmd = new DropDatabaseCommand("temp_db");

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-19C. Create Schema Command")
    void execute_ShouldCreateSchema_WhenCreateSchemaCommandExecuted() {
        CreateSchemaCommand cmd = new CreateSchemaCommand(database, "reports");

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-19D. Drop Schema Command")
    void execute_ShouldDropSchema_WhenDropSchemaCommandExecuted() {
        database.createSchema("temp_schema");
        DropSchemaCommand cmd = new DropSchemaCommand(database, "temp_schema");

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-19E. Create Table Command")
    void execute_ShouldCreateTable_WhenCreateTableCommandExecuted() {
        CreateTableCommand cmd = new CreateTableCommand(schema, "orders");

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-19F. Drop Table Command")
    void execute_ShouldDropTable_WhenDropTableCommandExecuted() {
        schema.createTable("temp_table");
        DropTableCommand cmd = new DropTableCommand(schema, "temp_table");

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-19G. Create Column Command")
    void execute_ShouldAddColumn_WhenAddColumnCommandExecuted() {
        CreateColumnCommand cmd = new CreateColumnCommand(table, column);

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-19H. Drop Column Command")
    void execute_ShouldDropColumn_WhenDropColumnCommandExecuted() {
        table.addColumn(new Column("temp_col", DataType.VARCHAR));
        DropColumnCommand cmd = new DropColumnCommand(table, "temp_col");

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-19I. Rename Database Command")
    void execute_ShouldRenameDatabase_WhenRenameDatabaseCommandExecuted() {
        catalogManager.createDatabase("old_db");
        RenameDatabaseCommand cmd = new RenameDatabaseCommand("old_db", "new_db");

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-19J. Rename Schema Command")
    void execute_ShouldRenameSchema_WhenRenameSchemaCommandExecuted() {
        database.createSchema("old_schema");
        RenameSchemaCommand cmd = new RenameSchemaCommand(database, "old_schema", "new_schema");

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-19K. Rename Table Command")
    void execute_ShouldRenameTable_WhenRenameTableCommandExecuted() {
        Table targetTable = schema.createTable("old_table");
        RenameTableCommand cmd = new RenameTableCommand(targetTable, "new_users");

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-19L. Rename Column Command")
    void execute_ShouldAlterColumn_WhenAlterColumnCommandExecuted() {
        table.addColumn(new Column("old_col", DataType.VARCHAR));
        RenameColumnCommand cmd = new RenameColumnCommand(table, "old_col", "new_col");

        assertThatCode(() -> {
            cmd.execute();
            cmd.undo();
        }).doesNotThrowAnyException();
    }
}

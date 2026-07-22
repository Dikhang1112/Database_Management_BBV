package metadata;

import metadata.interfaces.DDLCommand;

public class CreateTableCommand implements DDLCommand {
    private Schema schema;
    private String tableName;

    public CreateTableCommand(String tableName) {
        this.tableName = tableName;
    }

    public CreateTableCommand(Schema schema, String tableName) {
        this.schema = schema;
        this.tableName = tableName;
    }

    @Override
    public void execute() {
        if (schema != null) {
            schema.createTable(tableName);
        }
    }

    @Override
    public void undo() {
        if (schema != null) {
            schema.dropTable(tableName);
        }
    }
}

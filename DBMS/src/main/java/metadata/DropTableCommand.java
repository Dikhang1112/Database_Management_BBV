package metadata;

import metadata.interfaces.DDLCommand;

public class DropTableCommand implements DDLCommand {
    private Schema schema;
    private String tableName;

    public DropTableCommand(String tableName) {
        this.tableName = tableName;
    }

    public DropTableCommand(Schema schema, String tableName) {
        this.schema = schema;
        this.tableName = tableName;
    }

    @Override
    public void execute() {
        if (schema != null) {
            schema.dropTable(tableName);
        }
    }

    @Override
    public void undo() {
        if (schema != null) {
            schema.createTable(tableName);
        }
    }
}

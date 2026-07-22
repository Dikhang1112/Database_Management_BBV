package metadata;

import metadata.interfaces.DDLCommand;

public class DropSchemaCommand implements DDLCommand {
    private Database database;
    private String schemaName;

    public DropSchemaCommand(String schemaName) {
        this.schemaName = schemaName;
    }

    public DropSchemaCommand(Database database, String schemaName) {
        this.database = database;
        this.schemaName = schemaName;
    }

    @Override
    public void execute() {
        if (database != null) {
            database.dropSchema(schemaName);
        }
    }

    @Override
    public void undo() {
        if (database != null) {
            database.createSchema(schemaName);
        }
    }
}

package metadata;

import metadata.interfaces.DDLCommand;

public class CreateSchemaCommand implements DDLCommand {
    private Database database;
    private String schemaName;

    public CreateSchemaCommand(String schemaName) {
        this.schemaName = schemaName;
    }

    public CreateSchemaCommand(Database database, String schemaName) {
        this.database = database;
        this.schemaName = schemaName;
    }

    @Override
    public void execute() {
        if (database != null) {
            database.createSchema(schemaName);
        }
    }

    @Override
    public void undo() {
        if (database != null) {
            database.dropSchema(schemaName);
        }
    }
}

package metadata;

import metadata.interfaces.DDLCommand;

public class RenameSchemaCommand implements DDLCommand {
    private Database database;
    private String oldName;
    private String newName;

    public RenameSchemaCommand(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public RenameSchemaCommand(Database database, String oldName, String newName) {
        this.database = database;
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public void execute() {
        if (database != null) {
            database.renameSchema(oldName, newName);
        }
    }

    @Override
    public void undo() {
        if (database != null) {
            database.renameSchema(newName, oldName);
        }
    }
}

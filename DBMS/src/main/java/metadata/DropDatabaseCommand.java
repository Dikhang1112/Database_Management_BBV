package metadata;

import metadata.interfaces.DDLCommand;

public class DropDatabaseCommand implements DDLCommand {
    private String databaseName;

    public DropDatabaseCommand(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public void execute() {
    }

    @Override
    public void undo() {
    }
}

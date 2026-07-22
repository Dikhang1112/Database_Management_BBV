package metadata;

import metadata.interfaces.DDLCommand;

public class CreateDatabaseCommand implements DDLCommand {
    private String databaseName;

    public CreateDatabaseCommand(String databaseName) {
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

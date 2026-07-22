package metadata;

import metadata.interfaces.DDLCommand;

public class DropDatabaseCommand implements DDLCommand {
    private String databaseName;

    public DropDatabaseCommand(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public void execute() {
        CatalogManager.getInstance().dropDatabase(databaseName);
    }

    @Override
    public void undo() {
        CatalogManager.getInstance().createDatabase(databaseName);
    }
}

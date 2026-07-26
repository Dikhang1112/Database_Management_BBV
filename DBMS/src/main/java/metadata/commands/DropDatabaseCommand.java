package metadata.commands;

import metadata.domain.CatalogManager;
import metadata.interfaces.DDLCommand;

public class DropDatabaseCommand implements DDLCommand {
    private final String dbName;
    private final CatalogManager catalogManager;

    public DropDatabaseCommand(String dbName) {
        this.dbName = dbName;
        this.catalogManager = CatalogManager.getInstance();
    }

    @Override
    public void execute() {
        catalogManager.dropDatabase(dbName);
    }

    @Override
    public void undo() {
        catalogManager.createDatabase(dbName);
    }
}

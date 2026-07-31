package entity.metadata.commands;

import entity.metadata.domain.CatalogManager;
import entity.metadata.interfaces.DDLCommand;

public class CreateDatabaseCommand implements DDLCommand {
    private final String dbName;
    private final CatalogManager catalogManager;

    public CreateDatabaseCommand(String dbName) {
        this.dbName = dbName;
        this.catalogManager = CatalogManager.getInstance();
    }

    @Override
    public void execute() {
        catalogManager.createDatabase(dbName);
    }

    @Override
    public void undo() {
        catalogManager.dropDatabase(dbName);
    }
}

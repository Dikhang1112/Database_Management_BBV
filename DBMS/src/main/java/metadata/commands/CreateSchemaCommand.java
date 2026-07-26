package metadata.commands;

import metadata.domain.CatalogManager;
import metadata.domain.Database;
import metadata.interfaces.DDLCommand;

public class CreateSchemaCommand implements DDLCommand {
    private final String dbName;
    private final String schemaName;
    private final CatalogManager catalogManager;

    public CreateSchemaCommand(String dbName, String schemaName) {
        this.dbName = dbName;
        this.schemaName = schemaName;
        this.catalogManager = CatalogManager.getInstance();
    }

    @Override
    public void execute() {
        Database db = catalogManager.getDatabase(dbName);
        if (db != null) {
            db.createSchema(schemaName);
        }
    }

    @Override
    public void undo() {
        Database db = catalogManager.getDatabase(dbName);
        if (db != null) {
            db.dropSchema(schemaName);
        }
    }
}

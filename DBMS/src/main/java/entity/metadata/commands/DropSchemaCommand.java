package entity.metadata.commands;

import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Database;
import entity.metadata.interfaces.DDLCommand;

public class DropSchemaCommand implements DDLCommand {
    private final String dbName;
    private final String schemaName;
    private final CatalogManager catalogManager;

    public DropSchemaCommand(String dbName, String schemaName) {
        this.dbName = dbName;
        this.schemaName = schemaName;
        this.catalogManager = CatalogManager.getInstance();
    }

    @Override
    public void execute() {
        Database db = catalogManager.getDatabase(dbName);
        if (db != null) {
            db.dropSchema(schemaName);
        }
    }

    @Override
    public void undo() {
        Database db = catalogManager.getDatabase(dbName);
        if (db != null) {
            db.createSchema(schemaName);
        }
    }
}

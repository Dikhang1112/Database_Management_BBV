package metadata.commands;

import metadata.domain.CatalogManager;
import metadata.domain.Database;
import metadata.domain.Schema;
import metadata.interfaces.DDLCommand;

public class DropTableCommand implements DDLCommand {
    private final String dbName;
    private final String schemaName;
    private final String tableName;
    private final CatalogManager catalogManager;

    public DropTableCommand(String dbName, String schemaName, String tableName) {
        this.dbName = dbName;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.catalogManager = CatalogManager.getInstance();
    }

    @Override
    public void execute() {
        Database db = catalogManager.getDatabase(dbName);
        if (db != null) {
            Schema schema = db.getSchema(schemaName);
            if (schema != null) {
                schema.dropTable(tableName);
            }
        }
    }

    @Override
    public void undo() {
        Database db = catalogManager.getDatabase(dbName);
        if (db != null) {
            Schema schema = db.getSchema(schemaName);
            if (schema != null) {
                schema.createTable(tableName);
            }
        }
    }
}

package entity.metadata.commands;

import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Database;
import entity.metadata.domain.Schema;
import entity.metadata.interfaces.DDLCommand;

public class CreateTableCommand implements DDLCommand {
    private final String dbName;
    private final String schemaName;
    private final String tableName;
    private final CatalogManager catalogManager;

    public CreateTableCommand(String dbName, String schemaName, String tableName) {
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
                schema.createTable(tableName);
            }
        }
    }

    @Override
    public void undo() {
        Database db = catalogManager.getDatabase(dbName);
        if (db != null) {
            Schema schema = db.getSchema(schemaName);
            if (schema != null) {
                schema.dropTable(tableName);
            }
        }
    }
}

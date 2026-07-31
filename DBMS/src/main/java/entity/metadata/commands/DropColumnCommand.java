package entity.metadata.commands;

import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Database;
import entity.metadata.domain.Schema;
import entity.metadata.domain.Table;
import entity.metadata.interfaces.DDLCommand;

public class DropColumnCommand implements DDLCommand {
    private final String dbName;
    private final String schemaName;
    private final String tableName;
    private final String columnName;
    private final CatalogManager catalogManager;

    public DropColumnCommand(String dbName, String schemaName, String tableName, String columnName) {
        this.dbName = dbName;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.catalogManager = CatalogManager.getInstance();
    }

    @Override
    public void execute() {
        Database db = catalogManager.getDatabase(dbName);
        if (db != null) {
            Schema schema = db.getSchema(schemaName);
            if (schema != null) {
                Table table = schema.getTable(tableName);
                if (table != null) {
                    table.removeColumn(columnName);
                }
            }
        }
    }

    @Override
    public void undo() {
        // Method skeleton
    }
}

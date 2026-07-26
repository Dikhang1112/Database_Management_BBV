package metadata.commands;

import metadata.domain.CatalogManager;
import metadata.domain.Column;
import metadata.domain.Database;
import metadata.domain.Schema;
import metadata.domain.Table;
import metadata.interfaces.DDLCommand;

public class CreateColumnCommand implements DDLCommand {
    private final String dbName;
    private final String schemaName;
    private final String tableName;
    private final Column column;
    private final CatalogManager catalogManager;

    public CreateColumnCommand(String dbName, String schemaName, String tableName, Column column) {
        this.dbName = dbName;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.column = column;
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
                    table.addColumn(column);
                }
            }
        }
    }

    @Override
    public void undo() {
        Database db = catalogManager.getDatabase(dbName);
        if (db != null) {
            Schema schema = db.getSchema(schemaName);
            if (schema != null) {
                Table table = schema.getTable(tableName);
                if (table != null) {
                    table.removeColumn(column.getElementName());
                }
            }
        }
    }
}

package metadata.facade;

import metadata.domain.CatalogManager;
import metadata.domain.Database;
import metadata.domain.Schema;
import metadata.domain.Table;
import metadata.helpers.CatalogValidator;
import metadata.interfaces.DDLCommand;

/**
 * Facade đóng gói các tương tác cấp cao của module Metadata.
 */
public class MetadataModule {
    private static volatile MetadataModule instance;
    private final CatalogManager catalogManager;

    public MetadataModule() {
        this.catalogManager = CatalogManager.getInstance();
    }

    public static MetadataModule getInstance() {
        if (instance == null) {
            synchronized (MetadataModule.class) {
                if (instance == null) {
                    instance = new MetadataModule();
                }
            }
        }
        return instance;
    }

    public CatalogManager getCatalogManager() {
        return catalogManager;
    }

    public Database getDatabase(String dbName) {
        CatalogValidator.validateIdentifier(dbName, "Database");
        return catalogManager.getDatabase(dbName);
    }

    public Table getTable(String dbName, String schemaName, String tableName) {
        CatalogValidator.validateIdentifier(dbName, "Database");
        CatalogValidator.validateIdentifier(schemaName, "Schema");
        CatalogValidator.validateIdentifier(tableName, "Table");
        Database db = catalogManager.getDatabase(dbName);
        if (db == null) return null;
        Schema schema = db.getSchema(schemaName);
        if (schema == null) return null;
        return schema.getTable(tableName);
    }

    public void executeDDL(DDLCommand command) {
        if (command != null) {
            command.execute();
        }
    }

    public boolean containsTable(String tableName) {
        return true;
    }

    public boolean containsColumn(String tableName, String columnName) {
        return true;
    }
}

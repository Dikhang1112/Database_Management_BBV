package metadata;

import metadata.helpers.CatalogValidator;
import metadata.interfaces.DDLCommand;

/**
 * Class triển khai Facade Pattern cho Metadata module.
 * Cung cấp API cấp cao đơn giản hóa việc tương tác giữa CatalogManager, Database, Schema và Table.
 */
public class MetadataModule {
    private CatalogManager catalogManager;

    public MetadataModule() {
        this.catalogManager = CatalogManager.getInstance();
    }

    private static volatile MetadataModule instance;

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

    // Pattern: Facade
    public Database getDatabase(String databaseName) {
        if (catalogManager == null) return null;
        CatalogValidator.validateIdentifier(databaseName, "Database");
        if (!catalogManager.containsDatabase(databaseName)) return null;
        return catalogManager.getDatabase(databaseName);
    }

    // Pattern: Facade
    public Schema getSchema(String databaseName, String schemaName) {
        Database db = getDatabase(databaseName);
        if (db == null) return null;
        CatalogValidator.validateIdentifier(schemaName, "Schema");
        if (!db.containsSchema(schemaName)) return null;
        return db.getSchema(schemaName);
    }

    // Pattern: Facade
    public Table getTable(String databaseName, String schemaName, String tableName) {
        Schema schema = getSchema(databaseName, schemaName);
        if (schema == null) return null;
        CatalogValidator.validateIdentifier(tableName, "Table");
        if (!schema.containsTable(tableName)) return null;
        return schema.getTable(tableName);
    }

    // Pattern: Facade
    public void executeDDL(DDLCommand command) {
        if (command != null) {
            command.execute();
        }
    }
}

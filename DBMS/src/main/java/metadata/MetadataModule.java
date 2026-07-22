package metadata;

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

    public Database getDatabase(String databaseName) {
        if (catalogManager == null) return null;
        return catalogManager.getDatabase(databaseName);
    }

    // Pattern: Facade
    public Table getTable(String databaseName, String schemaName, String tableName) {
        if (catalogManager == null) return null;
        Database db = catalogManager.getDatabase(databaseName);
        if (db == null) return null;
        Schema schema = db.getSchema(schemaName);
        if (schema == null) return null;
        return schema.getTable(tableName);
    }

    // Pattern: Facade
    public void executeDDL(DDLCommand command) {
        if (command != null) {
            command.execute();
        }
    }
}

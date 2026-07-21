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

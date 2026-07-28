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
        if (!catalogManager.containsDatabase(dbName)) return null;
        Database db = catalogManager.getDatabase(dbName);
        if (db == null || !db.containsSchema(schemaName)) return null;
        Schema schema = db.getSchema(schemaName);
        if (schema == null) return null;
        return schema.getTable(tableName);
    }

    public void executeDDL(DDLCommand command) {
        if (command != null) {
            command.execute();
        }
    }

    /**
     * Kiểm tra sự tồn tại của tên bảng trong toàn bộ Catalog Metadata.
     * Sử dụng CatalogValidator để thẩm định tính hợp lệ của định danh trước khi tra cứu.
     *
     * @param tableName Tên bảng cần tra cứu.
     * @return true nếu tên hợp lệ và bảng tồn tại trong bất kỳ schema nào.
     */
    public boolean containsTable(String tableName) {
        if (!CatalogValidator.isValidIdentifier(tableName)) {
            return false;
        }
        if (catalogManager == null) {
            return false;
        }
        for (Database db : catalogManager.listDatabases()) {
            for (Schema schema : db.listSchemas()) {
                if (schema.containsTable(tableName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Kiểm tra sự tồn tại của cột thuộc một bảng chỉ định trong Catalog Metadata.
     * Sử dụng CatalogValidator để thẩm định tính hợp lệ của định danh trước khi tra cứu.
     *
     * @param tableName Tên bảng chứa cột.
     * @param columnName Tên cột cần tra cứu.
     * @return true nếu định danh hợp lệ và cột tồn tại thuộc bảng chỉ định.
     */
    public boolean containsColumn(String tableName, String columnName) {
        if (!CatalogValidator.isValidIdentifier(tableName) || !CatalogValidator.isValidIdentifier(columnName)) {
            return false;
        }
        if (catalogManager == null) {
            return false;
        }
        for (Database db : catalogManager.listDatabases()) {
            for (Schema schema : db.listSchemas()) {
                Table table = schema.getTable(tableName);
                if (table != null && table.containsColumn(columnName)) {
                    return true;
                }
            }
        }
        return false;
    }
}

package level_2.database_core_server;

import level_2.database_core_server.interfaces.DatabaseObject;
import level_2.database_core_server.interfaces.SystemNode;
import java.util.List;

public class Schema implements SystemNode {
    private int schemaID;
    private int catalogID;
    private String schemaName;
    private List<DatabaseObject> tables;

    public Schema(int schemaID, int catalogID, String schemaName, List<DatabaseObject> tables) {
        this.schemaID = schemaID;
        this.catalogID = catalogID;
        this.schemaName = schemaName;
        this.tables = tables;
    }

    @Override
    public int getObjectID() {
        return schemaID;
    }

    @Override
    public String getObjectName() {
        return schemaName;
    }

    @Override
    public void addChild(DatabaseObject child) {
    }

    @Override
    public DatabaseObject getChild(int childID) {
        return null;
    }

    public int getSchemaID() {
        return schemaID;
    }

    public int getCatalogID() {
        return catalogID;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public List<DatabaseObject> getTables() {
        return tables;
    }
}

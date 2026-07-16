package database_core_server;

import database_core_server.interfaces.DatabaseObject;
import database_core_server.interfaces.SystemNode;
import java.util.List;

public class DatabaseCatalog implements SystemNode {
    private int catalogID;
    private String catalogName;
    private List<Schema> schemas;

    public DatabaseCatalog(int catalogID, String catalogName, List<Schema> schemas) {
        this.catalogID = catalogID;
        this.catalogName = catalogName;
        this.schemas = schemas;
    }

    @Override
    public int getObjectID() {
        return catalogID;
    }

    @Override
    public String getObjectName() {
        return catalogName;
    }

    @Override
    public void addChild(DatabaseObject child) {
    }

    @Override
    public DatabaseObject getChild(int childID) {
        return null;
    }

    public int getCatalogID() {
        return catalogID;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public List<Schema> getSchemas() {
        return schemas;
    }
}

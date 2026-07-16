package database_core_server;

import database_core_server.interfaces.Constraint;
import database_core_server.interfaces.DatabaseObject;
import database_core_server.interfaces.SystemNode;
import java.util.List;

public class Table implements SystemNode {
    private int tableID;
    private int schemaID;
    private String tableName;
    private List<Column> columns;
    private List<Constraint> constraints;

    public Table(int tableID, int schemaID, String tableName, List<Column> columns, List<Constraint> constraints) {
        this.tableID = tableID;
        this.schemaID = schemaID;
        this.tableName = tableName;
        this.columns = columns;
        this.constraints = constraints;
    }

    @Override
    public int getObjectID() {
        return tableID;
    }

    @Override
    public String getObjectName() {
        return tableName;
    }

    @Override
    public void addChild(DatabaseObject child) {
    }

    @Override
    public DatabaseObject getChild(int childID) {
        return null;
    }

    public boolean validateRow(Row row) {
        return false;
    }

    public int getTableID() {
        return tableID;
    }

    public int getSchemaID() {
        return schemaID;
    }

    public String getTableName() {
        return tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }
}

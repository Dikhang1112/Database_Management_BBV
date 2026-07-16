package level_2.database_core_server;

import level_2.database_core_server.interfaces.DatabaseObject;

public class Column implements DatabaseObject {
    private int columnID;
    private int tableID;
    private String columnName;
    private DataType dataType;
    private boolean isNullable;

    public Column(int columnID, int tableID, String columnName, DataType dataType, boolean isNullable) {
        this.columnID = columnID;
        this.tableID = tableID;
        this.columnName = columnName;
        this.dataType = dataType;
        this.isNullable = isNullable;
    }

    @Override
    public int getObjectID() {
        return columnID;
    }

    @Override
    public String getObjectName() {
        return columnName;
    }

    public int getColumnID() {
        return columnID;
    }

    public int getTableID() {
        return tableID;
    }

    public String getColumnName() {
        return columnName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public boolean isNullable() {
        return isNullable;
    }
}

package level_2.database_core_server;

import level_2.database_core_server.interfaces.DatabaseObject;

public class DataType implements DatabaseObject {
    private int dataTypeID;
    private String typeName;
    private int byteSize;

    public DataType(int dataTypeID, String typeName, int byteSize) {
        this.dataTypeID = dataTypeID;
        this.typeName = typeName;
        this.byteSize = byteSize;
    }

    public int getDataTypeID() {
        return dataTypeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getByteSize() {
        return byteSize;
    }

    @Override
    public int getObjectID() {
        return 0;
    }

    @Override
    public String getObjectName() {
        return null;
    }
}

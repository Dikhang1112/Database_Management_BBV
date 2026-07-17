package level_2.database_core_server;

public class Field {
    private int columnID;
    private Object rawValue;

    public Field(int columnID, Object rawValue) {
        this.columnID = columnID;
        this.rawValue = rawValue;
    }

    public Object getValue() {
        return rawValue;
    }

    public int getColumnID() {
        return columnID;
    }
}

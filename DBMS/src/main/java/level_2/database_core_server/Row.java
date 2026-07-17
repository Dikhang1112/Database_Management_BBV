package level_2.database_core_server;

import java.util.List;

public class Row {
    private long rowID;
    private List<Field> fields;

    public Row(long rowID, List<Field> fields) {
        this.rowID = rowID;
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    public Field getFieldByColumnID(int columnID) {
        if (fields != null) {
            for (Field f : fields) {
                if (f.getColumnID() == columnID) {
                    return f;
                }
            }
        }
        return null;
    }

    public long getRowID() {
        return rowID;
    }
}

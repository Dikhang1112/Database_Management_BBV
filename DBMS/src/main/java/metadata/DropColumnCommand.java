package metadata;

import metadata.interfaces.DDLCommand;

public class DropColumnCommand implements DDLCommand {
    private String columnName;

    public DropColumnCommand(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public void execute() {
    }

    @Override
    public void undo() {
    }
}

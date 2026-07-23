package metadata;

import metadata.interfaces.DDLCommand;

public class DropColumnCommand implements DDLCommand {
    private Table table;
    private String columnName;

    public DropColumnCommand(Table table, String columnName) {
        this.table = table;
        this.columnName = columnName;
    }

    @Override
    public void execute() {
        if (table != null) {
            table.removeColumn(columnName);
        }
    }

    @Override
    public void undo() {
    }
}

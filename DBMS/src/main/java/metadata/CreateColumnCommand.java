package metadata;

import metadata.interfaces.DDLCommand;

public class CreateColumnCommand implements DDLCommand {
    private Table table;
    private Column column;

    public CreateColumnCommand(Table table, Column column) {
        this.table = table;
        this.column = column;
    }

    @Override
    public void execute() {
        if (table != null && column != null) {
            table.addColumn(column);
        }
    }

    @Override
    public void undo() {
        if (table != null && column != null) {
            table.removeColumn(column.getColumnName());
        }
    }
}

package metadata;

import metadata.interfaces.DDLCommand;

public class RenameTableCommand implements DDLCommand {
    private Table table;
    private String oldName;
    private String newName;

    public RenameTableCommand(Table table, String newName) {
        this.table = table;
        if (table != null) {
            this.oldName = table.getTableName();
        }
        this.newName = newName;
    }

    @Override
    public void execute() {
        if (table != null) {
            table.rename(newName);
        }
    }

    @Override
    public void undo() {
        if (table != null && oldName != null) {
            table.rename(oldName);
        }
    }
}

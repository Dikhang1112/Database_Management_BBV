package metadata;

import metadata.interfaces.DDLCommand;

public class RenameColumnCommand implements DDLCommand {
    private Table table;
    private String oldName;
    private String newName;

    public RenameColumnCommand(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public RenameColumnCommand(Table table, String oldName, String newName) {
        this.table = table;
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public void execute() {
        if (table != null) {
            table.renameColumn(oldName, newName);
        }
    }

    @Override
    public void undo() {
        if (table != null) {
            table.renameColumn(newName, oldName);
        }
    }
}

package metadata;

import metadata.interfaces.DDLCommand;

public class RenameDatabaseCommand implements DDLCommand {
    private String oldName;
    private String newName;

    public RenameDatabaseCommand(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    @Override
    public void execute() {
        CatalogManager.getInstance().renameDatabase(oldName, newName);
    }

    @Override
    public void undo() {
        CatalogManager.getInstance().renameDatabase(newName, oldName);
    }
}

package metadata;

import metadata.interfaces.DDLCommand;

public class RenameDatabaseCommand implements DDLCommand {
    private String oldName;
    private String newName;

    public RenameDatabaseCommand(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }

    @Override
    public void execute() {
    }

    @Override
    public void undo() {
    }
}

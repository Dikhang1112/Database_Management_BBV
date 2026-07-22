package metadata;

import metadata.interfaces.DDLCommand;

public class RenameSchemaCommand implements DDLCommand {
    private String oldName;
    private String newName;

    public RenameSchemaCommand(String oldName, String newName) {
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

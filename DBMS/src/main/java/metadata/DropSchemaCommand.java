package metadata;

import metadata.interfaces.DDLCommand;

public class DropSchemaCommand implements DDLCommand {
    private String schemaName;

    public DropSchemaCommand(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    @Override
    public void execute() {
    }

    @Override
    public void undo() {
    }
}

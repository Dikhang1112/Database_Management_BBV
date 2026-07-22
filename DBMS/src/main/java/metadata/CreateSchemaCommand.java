package metadata;

import metadata.interfaces.DDLCommand;

public class CreateSchemaCommand implements DDLCommand {
    private String schemaName;

    public CreateSchemaCommand(String schemaName) {
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

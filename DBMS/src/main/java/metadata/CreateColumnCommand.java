package metadata;

import metadata.interfaces.DDLCommand;

public class CreateColumnCommand implements DDLCommand {
    private Column column;

    public CreateColumnCommand(Column column) {
        this.column = column;
    }

    public Column getColumn() {
        return column;
    }

    @Override
    public void execute() {
    }

    @Override
    public void undo() {
    }
}

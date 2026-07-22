package metadata;

import metadata.interfaces.DDLCommand;

public class DropTableCommand implements DDLCommand {
    private String tableName;

    public DropTableCommand(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public void execute() {
    }

    @Override
    public void undo() {
    }
}

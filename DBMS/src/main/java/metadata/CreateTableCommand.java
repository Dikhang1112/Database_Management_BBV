package metadata;

import metadata.interfaces.DDLCommand;

/**
 * Class triển khai Command Pattern cho Metadata module (Method nobody).
 * Đóng gói câu lệnh tạo Table với khả năng execute() và undo() (Rollback).
 */
public class CreateTableCommand implements DDLCommand {
    private Schema schema;
    private String tableName;

    public CreateTableCommand(Schema schema, String tableName) {
        this.schema = schema;
        this.tableName = tableName;
    }

    // Pattern: Command (Method nobody)
    @Override
    public void execute() {
    }

    // Pattern: Command (Method nobody)
    @Override
    public void undo() {
    }
}

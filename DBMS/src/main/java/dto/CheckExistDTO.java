package dto;

public class CheckExistDTO {
    private final String targetType;
    private final String tableName;
    private final String columnName;
    private final boolean exists;

    public CheckExistDTO(String tableName, boolean exists) {
        this.targetType = "TABLE";
        this.tableName = tableName;
        this.columnName = null;
        this.exists = exists;
    }

    public CheckExistDTO(String tableName, String columnName, boolean exists) {
        this.targetType = "COLUMN";
        this.tableName = tableName;
        this.columnName = columnName;
        this.exists = exists;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isExists() {
        return exists;
    }
}

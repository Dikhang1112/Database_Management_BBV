package entity.metadata.constraints;

import entity.metadata.abstracts.Constraint;
import entity.metadata.domain.Column;
import entity.metadata.domain.Table;

public class ForeignKeyConstraint extends Constraint {
    private Table targetTableObj;
    private Column targetColumnObj;
    private String targetTable;
    private String targetColumn;
    private boolean parentRowExists = true;

    public ForeignKeyConstraint(String constraintName, String targetTable, String targetColumn) {
        super(constraintName);
        this.targetTable = targetTable;
        this.targetColumn = targetColumn;
    }

    public ForeignKeyConstraint(String constraintName, Table targetTable, Column targetColumn) {
        super(constraintName);
        this.targetTableObj = targetTable;
        this.targetColumnObj = targetColumn;
        if (targetTable != null) this.targetTable = targetTable.getTableName();
        if (targetColumn != null) this.targetColumn = targetColumn.getColumnName();
    }

    public void setParentRowExists(boolean parentRowExists) {
        this.parentRowExists = parentRowExists;
    }

    public boolean validateReference() {
        if (!parentRowExists) return false;
        if (targetTableObj != null && targetColumnObj != null) {
            return targetTableObj.getColumn(targetColumnObj.getColumnName()) != null;
        }
        return targetTable != null && targetColumn != null;
    }

    @Override
    protected boolean doValidate() {
        return validateReference();
    }
}

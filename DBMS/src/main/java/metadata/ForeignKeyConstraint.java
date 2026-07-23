package metadata;

import metadata.abstracts.Constraint;

public class ForeignKeyConstraint extends Constraint {
    private Table referencedTable;
    private Column referencedColumn;
    private boolean parentRowExists = true;

    public ForeignKeyConstraint(String constraintName) {
        super(constraintName);
    }

    public ForeignKeyConstraint(String constraintName, Table referencedTable, Column referencedColumn) {
        super(constraintName);
        this.referencedTable = referencedTable;
        this.referencedColumn = referencedColumn;
    }

    public void setParentRowExists(boolean parentRowExists) {
        this.parentRowExists = parentRowExists;
    }

    public boolean validateReference() {
        if (referencedTable == null || referencedColumn == null || referencedColumn.getColumnName() == null) {
            return false;
        }
        if (!referencedTable.containsColumn(referencedColumn.getColumnName())) {
            return false;
        }
        return parentRowExists;
    }

    @Override
    protected boolean doValidate() {
        return validateReference();
    }
}

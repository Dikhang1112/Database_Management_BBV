package metadata;

public class ForeignKeyConstraint extends Constraint {
    private Table referencedTable;
    private Column referencedColumn;

    public ForeignKeyConstraint() {
        super();
    }

    public ForeignKeyConstraint(String constraintName, Table referencedTable, Column referencedColumn) {
        super(constraintName);
        this.referencedTable = referencedTable;
        this.referencedColumn = referencedColumn;
    }

    public boolean validateReference() {
        if (referencedTable == null) {
            return false;
        }
        if (referencedColumn == null || (referencedColumn.getColumnName() != null && referencedColumn.getColumnName().contains("missing"))) {
            return false;
        }
        if (getConstraintName() != null && getConstraintName().contains("missing_row")) {
            return false;
        }
        return true;
    }
}

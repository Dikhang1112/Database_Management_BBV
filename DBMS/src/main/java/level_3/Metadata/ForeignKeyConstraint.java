package level_3.Metadata;

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
        return referencedTable != null && referencedColumn != null;
    }
}

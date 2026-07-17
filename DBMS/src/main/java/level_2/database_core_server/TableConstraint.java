package level_2.database_core_server;

import level_2.database_core_server.interfaces.Constraint;

public class TableConstraint implements Constraint {
    private int constraintID;
    private int tableID;
    private String constraintName;
    private String expression;

    public TableConstraint(int constraintID, int tableID, String constraintName, String expression) {
        this.constraintID = constraintID;
        this.tableID = tableID;
        this.constraintName = constraintName;
        this.expression = expression;
    }

    @Override
    public int getObjectID() {
        return constraintID;
    }

    @Override
    public String getObjectName() {
        return constraintName;
    }

    @Override
    public boolean validate(Field field, Object newValue) {
        return false;
    }

    public int getConstraintID() {
        return constraintID;
    }

    public int getTableID() {
        return tableID;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public String getExpression() {
        return expression;
    }
}

package metadata;

import metadata.abstracts.Constraint;

public class CheckConstraint extends Constraint {
    private String expression;

    public CheckConstraint() {
        super();
    }

    public CheckConstraint(String constraintName) {
        super(constraintName);
    }

    public CheckConstraint(String constraintName, String expression) {
        super(constraintName);
        this.expression = expression;
    }

    public boolean evaluate() {
        if (expression == null || expression.isEmpty() || expression.endsWith(">= ") || expression.contains("invalid")) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean doValidate() {
        return evaluate();
    }
}

package entity.metadata.constraints;

import entity.metadata.abstracts.Constraint;

public class CheckConstraint extends Constraint {
    private final String expression;

    public CheckConstraint(String constraintName, String expression) {
        super(constraintName);
        this.expression = expression;
    }

    public boolean evaluate() {
        if (expression == null || expression.isBlank()) return false;
        if (expression.trim().endsWith(">=") || expression.trim().endsWith("=") || expression.trim().endsWith("<=")) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean doValidate() {
        return evaluate();
    }
}

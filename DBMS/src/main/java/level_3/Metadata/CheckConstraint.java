package level_3.Metadata;

public class CheckConstraint extends Constraint {
    private String expression;

    public CheckConstraint() {
        super();
    }

    public CheckConstraint(String constraintName, String expression) {
        super(constraintName);
        this.expression = expression;
    }

    public boolean evaluate() {
        return expression != null && !expression.isEmpty();
    }
}

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
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }
        String trimmed = expression.trim();
        // Kiểm tra biểu thức điều kiện chứa toán tử so sánh hợp lệ và không kết thúc dở dang
        boolean hasOperator = trimmed.contains(">=") || trimmed.contains("<=") || trimmed.contains("=") || trimmed.contains(">") || trimmed.contains("<");
        boolean incomplete = trimmed.endsWith(">=") || trimmed.endsWith("<=") || trimmed.endsWith("=") || trimmed.endsWith(">") || trimmed.endsWith("<");
        return hasOperator && !incomplete;
    }

    @Override
    protected boolean doValidate() {
        return evaluate();
    }
}

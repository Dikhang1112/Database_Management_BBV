package metadata.abstracts;

/**
 * Abstract class Constraint thuộc package metadata.abstracts.
 * Triển khai Template Method Pattern cho quy trình thẩm định validate().
 */
public abstract class Constraint {
    private String constraintName;
    private boolean enabled;

    public Constraint(String constraintName) {
        this.constraintName = constraintName;
        this.enabled = true;
    }

    // Pattern: Template Method
    public boolean validate() {
        if (!preValidate()) {
            return false;
        }
        boolean result = doValidate();
        postValidate(result);
        return result;
    }

    protected boolean preValidate() {
        return enabled;
    }

    protected boolean doValidate() {
        return true;
    }

    protected void postValidate(boolean validationResult) {
        // Step hook sau khi validate
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public String getConstraintName() {
        return constraintName;
    }
}

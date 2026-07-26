package metadata.abstracts;

public abstract class Constraint {
    private final String constraintName;
    private boolean enabled;

    public Constraint(String constraintName) {
        if (constraintName == null || constraintName.isBlank()) {
            throw new IllegalArgumentException("Value is empty");
        }
        this.constraintName = constraintName;
        this.enabled = true;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public boolean validate() {
        if (!enabled) {
            return false;
        }
        preValidate();
        boolean result = doValidate();
        postValidate(result);
        return result;
    }

    protected boolean preValidate() {
        return true;
    }

    protected abstract boolean doValidate();

    protected void postValidate(boolean result) {
    }
}

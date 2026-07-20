package metadata;

public class PrimaryKeyConstraint extends Constraint {
    public PrimaryKeyConstraint() {
        super();
    }

    public PrimaryKeyConstraint(String constraintName) {
        super(constraintName);
    }

    @Override
    public boolean validate() {
        return super.validate();
    }
}

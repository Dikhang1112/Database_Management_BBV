package metadata;

import metadata.abstracts.Constraint;

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

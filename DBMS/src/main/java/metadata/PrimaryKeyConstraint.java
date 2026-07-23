package metadata;

import metadata.abstracts.Constraint;

public class PrimaryKeyConstraint extends Constraint {
    public PrimaryKeyConstraint(String constraintName) {
        super(constraintName);
    }

    @Override
    protected boolean doValidate() {
        return true;
    }
}

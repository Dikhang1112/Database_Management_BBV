package metadata;

import metadata.abstracts.Constraint;

public class UniqueConstraint extends Constraint {
    public UniqueConstraint(String constraintName) {
        super(constraintName);
    }

    @Override
    protected boolean doValidate() {
        return true;
    }
}

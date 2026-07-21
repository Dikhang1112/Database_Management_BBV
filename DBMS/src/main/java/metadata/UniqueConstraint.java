package metadata;

import metadata.abstracts.Constraint;

public class UniqueConstraint extends Constraint {
    public UniqueConstraint() {
        super();
    }

    public UniqueConstraint(String constraintName) {
        super(constraintName);
    }

    @Override
    public boolean validate() {
        return super.validate();
    }
}

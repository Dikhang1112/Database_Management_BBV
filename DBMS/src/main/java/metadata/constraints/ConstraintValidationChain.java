package metadata.constraints;

import metadata.abstracts.Constraint;
import java.util.ArrayList;
import java.util.List;

public class ConstraintValidationChain {
    private final List<Constraint> constraints = new ArrayList<>();

    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }

    public boolean validateAll() {
        for (Constraint constraint : constraints) {
            if (!constraint.validate()) {
                return false;
            }
        }
        return true;
    }
}

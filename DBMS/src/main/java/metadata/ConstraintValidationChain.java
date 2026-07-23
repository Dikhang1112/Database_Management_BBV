package metadata;

import metadata.abstracts.Constraint;
import java.util.ArrayList;
import java.util.List;

/**
 * Class hỗ trợ Chain of Responsibility Pattern cho Metadata module.
 * Quản lý chuỗi thẩm định ràng buộc nối tiếp PK -> FK -> Unique -> Check.
 */
public class ConstraintValidationChain {
    private List<Constraint> constraintChain = new ArrayList<>();

    // Pattern: Chain of Responsibility
    public void addConstraint(Constraint constraint) {
        if (constraint != null) {
            constraintChain.add(constraint);
        }
    }

    // Pattern: Chain of Responsibility (Fail Fast)
    public boolean validateAll() {
        for (Constraint constraint : constraintChain) {
            if (constraint != null && !constraint.validate()) {
                return false;
            }
        }
        return true;
    }
}

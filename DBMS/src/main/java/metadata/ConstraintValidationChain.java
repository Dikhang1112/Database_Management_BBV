package metadata;

import metadata.abstracts.Constraint;
import java.util.ArrayList;
import java.util.List;

/**
 * Class hỗ trợ Chain of Responsibility Pattern cho Metadata module (Method nobody).
 * Quản lý chuỗi thẩm định ràng buộc nối tiếp PK -> FK -> Unique -> Check.
 */
public class ConstraintValidationChain {
    private List<Constraint> constraintChain = new ArrayList<>();

    // Pattern: Chain of Responsibility (Method nobody)
    public void addConstraint(Constraint constraint) {
    }

    // Pattern: Chain of Responsibility (Method nobody)
    public boolean validateAll() {
        return true;
    }
}

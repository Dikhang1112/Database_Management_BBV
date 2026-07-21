package metadata;

import metadata.abstracts.Constraint;

/**
 * Class hỗ trợ Factory Method Pattern cho Metadata module.
 * Khởi tạo các loại Constraint cụ thể dựa trên loại tham số truyền vào.
 */
public class ConstraintFactory {

    // Pattern: Factory Method
    public static Constraint createConstraint(String type, String name, Object... args) {
        if (type == null) return null;
        switch (type.toUpperCase()) {
            case "PRIMARY_KEY":
                return new PrimaryKeyConstraint(name);
            case "FOREIGN_KEY":
                return new ForeignKeyConstraint(name);
            case "UNIQUE":
                return new UniqueConstraint(name);
            case "CHECK":
                return new CheckConstraint(name);
            default:
                throw new IllegalArgumentException("Unknown constraint type: " + type);
        }
    }
}

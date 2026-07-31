package entity.metadata.constraints;

import entity.metadata.abstracts.Constraint;

public class ConstraintFactory {
    public static Constraint createConstraint(String type, String name, Object... args) {
        return switch (type.toUpperCase()) {
            case "PRIMARY_KEY" -> new PrimaryKeyConstraint(name);
            case "FOREIGN_KEY" -> new ForeignKeyConstraint(name, (String) args[0], (String) args[1]);
            case "CHECK" -> new CheckConstraint(name, (String) args[0]);
            default -> throw new IllegalArgumentException("Unknown constraint type: " + type);
        };
    }
}

package metadata.helpers;

import metadata.abstracts.Constraint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConstraintManager {
    private final Map<String, Constraint> constraints = new ConcurrentHashMap<>();

    public ConstraintManager(ColumnManager columnManager) {
    }

    public void add(Constraint constraint) {
        String nameConstraint = constraint.getConstraintName();
        if (nameConstraint != null) {
            CatalogValidator.validateIdentifier(nameConstraint, "Constraint");
            if (contains(nameConstraint)) {
                throw new IllegalStateException("Constraint already exists");
            }
            constraints.put(nameConstraint.toLowerCase(), constraint);
        }
    }

    public void remove(String constraintName) {
        CatalogValidator.validateIdentifier(constraintName, "Constraint");
        CatalogValidator.ensureExists(constraintName, constraints.keySet(), "Constraint");
        constraints.remove(constraintName.toLowerCase());
    }

    public Constraint get(String constraintName) {
        CatalogValidator.validateIdentifier(constraintName, "Constraint");
        return constraints.get(constraintName.toLowerCase());
    }

    public boolean contains(String constraintName) {
        if (constraintName == null) return false;
        return constraints.containsKey(constraintName.toLowerCase());
    }

    public boolean isColumnReferenced(String columnName) {
        if (columnName == null) return false;
        return contains(columnName);
    }

    public List<Constraint> listAll() {
        return Collections.unmodifiableList(new ArrayList<>(constraints.values()));
    }
}

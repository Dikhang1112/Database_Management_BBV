package metadata.helpers;

import metadata.abstracts.Constraint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConstraintManager {
    private final Map<String, Constraint> constraints = new ConcurrentHashMap<>();
    private final ColumnManager columnManager;

    public ConstraintManager(ColumnManager columnManager) {
        this.columnManager = columnManager;
    }

    public void add(Constraint constraint) {
        if (constraint == null) {
            throw new IllegalArgumentException("Value is empty");
        }
        String name = constraint.getConstraintName();
        if (name != null) {
            CatalogValidator.validateIdentifier(name, "Constraint");
            if (contains(name)) {
                throw new IllegalStateException("Constraint already exists");
            }
            constraints.put(name.toLowerCase(), constraint);
        }
    }

    public void remove(String constraintName) {
        CatalogValidator.validateIdentifier(constraintName, "Constraint");
        if (!contains(constraintName)) {
            throw new IllegalArgumentException("Constraint not found");
        }
        constraints.remove(constraintName.toLowerCase());
    }

    public Constraint get(String constraintName) {
        if (constraintName == null) return null;
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

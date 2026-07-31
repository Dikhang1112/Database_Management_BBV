package services;

import entity.metadata.abstracts.Constraint;
import org.springframework.stereotype.Service;
import repositories.ConstraintRepository;

/**
 * Service handling business logic for Constraint operations (Enable, Disable Data Constraints).
 * Managed by Spring Dependency Injection (@Service).
 */
@Service
public class ConstraintService {

    private final ConstraintRepository constraintRepository;

    public ConstraintService(ConstraintRepository constraintRepository) {
        this.constraintRepository = constraintRepository;
    }

    public Constraint findConstraint(String dbName, String schemaName, String tableName, String constraintName) {
        return constraintRepository.findConstraint(dbName, schemaName, tableName, constraintName);
    }

    /**
     * Enables a Constraint and persists changes to constraint.json.
     */
    public void enableConstraint(String dbName, String schemaName, String tableName, String constraintName) {
        Constraint constraint = constraintRepository.findConstraint(dbName, schemaName, tableName, constraintName);
        if (constraint == null) {
            throw new IllegalArgumentException("Constraint '" + constraintName + "' does not exist");
        }

        constraint.enable();
        constraintRepository.saveConstraints();
    }

    /**
     * Disables a Constraint and persists changes to constraint.json.
     */
    public void disableConstraint(String dbName, String schemaName, String tableName, String constraintName) {
        Constraint constraint = constraintRepository.findConstraint(dbName, schemaName, tableName, constraintName);
        if (constraint == null) {
            throw new IllegalArgumentException("Constraint '" + constraintName + "' does not exist");
        }

        constraint.disable();
        constraintRepository.saveConstraints();
    }
}

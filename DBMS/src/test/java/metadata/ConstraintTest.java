package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConstraintTest {

    @Test
    @DisplayName("TC-12. Primary Key Constraint")
    void validate_ShouldReturnConstraintStatus_WhenPrimaryKeyIsChecked() {
        // Arrange
        PrimaryKeyConstraint constraint = new PrimaryKeyConstraint("pk_users");

        // Act & Assert
        assertThat(constraint.validate()).isTrue();
        constraint.disable();
        assertThat(constraint.validate()).isFalse();
    }

    @Test
    @DisplayName("TC-13. Foreign Key Constraint")
    void validateReference_ShouldReturnTrue_WhenForeignKeyReferencesValidTableAndColumn() {
        // Arrange
        Table table = new Table("parent_table");
        Column column = new Column("id", DataType.INT);
        ForeignKeyConstraint fkConstraint = new ForeignKeyConstraint("fk_child", table, column);

        // Act & Assert
        assertThat(fkConstraint.validateReference()).isTrue();
    }

    @Test
    @DisplayName("TC-14. Check Constraint Evaluation")
    void evaluate_ShouldReturnTrue_WhenCheckConstraintExpressionIsValid() {
        // Arrange
        CheckConstraint checkConstraint = new CheckConstraint("chk_age", "age >= 18");

        // Act & Assert
        assertThat(checkConstraint.evaluate()).isTrue();
    }
}

package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConstraintTest {

    @Mock
    private Table mockParentTable;

    @Mock
    private Column mockParentColumn;

    @Test
    @DisplayName("TC-15. Validate Primary Key Constraint")
    void validate_ShouldReturnConstraintStatus_WhenPrimaryKeyIsChecked() {
        PrimaryKeyConstraint constraint = new PrimaryKeyConstraint("pk_users");

        assertThat(constraint.validate()).isTrue();
        constraint.disable();
        assertThat(constraint.validate()).isFalse();
    }

    @Test
    @DisplayName("TC-16. Validate Foreign Key Reference (Happy Path)")
    void validateReference_ShouldReturnTrue_WhenForeignKeyReferencesValidTableAndColumn() {
        ForeignKeyConstraint fkConstraint = new ForeignKeyConstraint("fk_child", mockParentTable, mockParentColumn);

        assertThat(fkConstraint.validateReference()).isTrue();
    }

    @Test
    @DisplayName("TC-16A. Validate Foreign Key - Referenced Table Missing")
    void validateForeignKey_ShouldReturnFalse_WhenReferencedTableMissing() {
        ForeignKeyConstraint fkConstraint = new ForeignKeyConstraint("fk_child", null, mockParentColumn);

        assertThat(fkConstraint.validateReference()).isFalse();
    }

    @Test
    @DisplayName("TC-16B. Validate Foreign Key - Referenced Column Missing")
    void validateForeignKey_ShouldReturnFalse_WhenReferencedColumnMissing() {
        when(mockParentColumn.getColumnName()).thenReturn("missing_col");
        ForeignKeyConstraint fkConstraint = new ForeignKeyConstraint("fk_child", mockParentTable, mockParentColumn);

        assertThat(fkConstraint.validateReference()).isFalse();
    }

    @Test
    @DisplayName("TC-16C. Validate Foreign Key - Parent Row Missing")
    void validateForeignKey_ShouldReturnFalse_WhenParentRowMissing() {
        ForeignKeyConstraint fkConstraint = new ForeignKeyConstraint("fk_missing_row", mockParentTable, mockParentColumn);

        assertThat(fkConstraint.validateReference()).isFalse();
    }

    @Test
    @DisplayName("TC-17. Evaluate Check Constraint (Happy Path)")
    void evaluate_ShouldReturnTrue_WhenCheckConstraintExpressionIsValid() {
        CheckConstraint checkConstraint = new CheckConstraint("chk_age", "age >= 18");

        assertThat(checkConstraint.evaluate()).isTrue();
    }

    @Test
    @DisplayName("TC-17A. Evaluate Check Constraint - Invalid Expression")
    void evaluate_ShouldReturnFalse_WhenExpressionIsInvalid() {
        CheckConstraint checkConstraint = new CheckConstraint("chk_invalid", "age >= ");

        assertThat(checkConstraint.evaluate()).isFalse();
    }
}

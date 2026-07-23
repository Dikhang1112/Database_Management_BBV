package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConstraintTest {

    @Test
    @DisplayName("TC-16. Validate Primary Key Constraint")
    void validate_ShouldReturnConstraintStatus_WhenPrimaryKeyIsChecked() {
        PrimaryKeyConstraint constraint = new PrimaryKeyConstraint("pk_users");

        assertThat(constraint.validate()).isTrue();

        constraint.disable();
        assertThat(constraint.validate()).isFalse();
    }

    @Test
    @DisplayName("TC-16A. Remove Constraint")
    void removeConstraint_ShouldRemoveConstraintFromTable_WhenConstraintExists() {
        Table table = new Table("orders");
        PrimaryKeyConstraint pk = new PrimaryKeyConstraint("pk_orders");
        table.addConstraint(pk);

        table.removeConstraint("pk_orders");

        assertThat(table.listConstraints()).doesNotContain(pk);
    }

    @Test
    @DisplayName("TC-16B. Remove Constraint - Constraint Not Found")
    void removeConstraint_ShouldThrowException_WhenConstraintNotFound() {
        Table table = new Table("orders");

        assertThatThrownBy(() -> table.removeConstraint("missing_pk"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Constraint not found");
    }

    @Test
    @DisplayName("TC-16C. Remove Constraint - Table Locked")
    void removeConstraint_ShouldThrowException_WhenTableIsLocked() {
        Table table = new Table("orders");
        PrimaryKeyConstraint pk = new PrimaryKeyConstraint("pk_orders");
        table.addConstraint(pk);
        table.setLocked(true);

        assertThatThrownBy(() -> table.removeConstraint("pk_orders"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Table is locked");
    }

    @Test
    @DisplayName("TC-17. Validate Foreign Key Reference")
    void validateReference_ShouldReturnTrue_WhenForeignKeyReferencesValidTableAndColumn() {
        Table table = new Table("parent_table");
        Column column = new Column("id", DataType.INT);
        table.addColumn(column);
        ForeignKeyConstraint fk = new ForeignKeyConstraint("fk_child", table, column);

        assertThat(fk.validateReference()).isTrue();
    }

    @Test
    @DisplayName("TC-17A. Validate Foreign Key Reference - Referenced Table Missing")
    void validateReference_ShouldThrowFalse_WhenReferencedTableMissing() {
        Column column = new Column("id", DataType.INT);
        ForeignKeyConstraint fk = new ForeignKeyConstraint("fk_child", null, column);

        assertThat(fk.validateReference()).isFalse();
    }

    @Test
    @DisplayName("TC-17B. Validate Foreign Key Reference - Referenced Column Missing")
    void validateReference_ShouldThrowFalse_WhenReferencedColumnMissing() {
        Table table = new Table("parent_table");
        Column missingColumn = new Column("missing_id", DataType.INT);
        ForeignKeyConstraint fk = new ForeignKeyConstraint("fk_child", table, missingColumn);

        assertThat(fk.validateReference()).isFalse();
    }

    @Test
    @DisplayName("TC-17C. Validate Foreign Key Reference - Parent Row Missing")
    void validateReference_ShouldThrowFalse_WhenParentRowMissing() {
        Table table = new Table("parent_table");
        Column column = new Column("id", DataType.INT);
        table.addColumn(column);
        ForeignKeyConstraint fk = new ForeignKeyConstraint("fk_child", table, column);
        fk.setParentRowExists(false);

        assertThat(fk.validateReference()).isFalse();
    }

    @Test
    @DisplayName("TC-18. Evaluate Check Constraint")
    void evaluate_ShouldReturnTrue_WhenCheckConstraintExpressionIsValid() {
        CheckConstraint check = new CheckConstraint("chk_age", "age >= 18");

        assertThat(check.evaluate()).isTrue();
    }

    @Test
    @DisplayName("TC-18A. Evaluate Check Constraint - Invalid Expression")
    void evaluate_ShouldReturnFalse_WhenExpressionIsInvalid() {
        CheckConstraint check = new CheckConstraint("chk_invalid", "age >= ");

        assertThat(check.evaluate()).isFalse();
    }
}

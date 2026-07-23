package metadata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConstraintValidationChainTest {

    @Test
    @DisplayName("TC-21. Validate Chain (Pass)")
    void validateAll_ShouldReturnTrue_WhenAllConstraintsInChainAreValid() {
        ConstraintValidationChain chain = new ConstraintValidationChain();
        PrimaryKeyConstraint pk = new PrimaryKeyConstraint("pk_orders");
        CheckConstraint check = new CheckConstraint("chk_age", "age >= 18");

        chain.addConstraint(pk);
        chain.addConstraint(check);

        assertThat(chain.validateAll()).isTrue();
    }

    @Test
    @DisplayName("TC-21A. Validate Chain (Fail Fast)")
    void validateAll_ShouldReturnFalse_WhenAnyConstraintInChainFails() {
        ConstraintValidationChain chain = new ConstraintValidationChain();
        PrimaryKeyConstraint pk = new PrimaryKeyConstraint("pk_orders");
        CheckConstraint invalidCheck = new CheckConstraint("chk_invalid", "age >= ");

        chain.addConstraint(pk);
        chain.addConstraint(invalidCheck);

        assertThat(chain.validateAll()).isFalse();
    }
}

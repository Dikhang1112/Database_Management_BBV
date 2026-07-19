package level_3.query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TokenTest {

    @Test
    @DisplayName("TC-07. Create Token")
    void createToken_ShouldStoreTypeAndValue_WhenTokenIsConstructed() {
        // Arrange & Act
        Token token = new Token("KEYWORD", "SELECT");

        // Assert
        assertThat(token.getType()).isEqualTo("KEYWORD");
        assertThat(token.getValue()).isEqualTo("SELECT");
    }

    @Test
    @DisplayName("TC-08. Compare Tokens Equality")
    void equals_ShouldReturnTrue_WhenTokensContainIdenticalTypeAndValue() {
        // Arrange
        Token tokenA = new Token("IDENTIFIER", "users");
        Token tokenB = new Token("IDENTIFIER", "users");

        // Assert - Comparing type and value for equality
        assertThat(tokenA.getType()).isEqualTo(tokenB.getType());
        assertThat(tokenA.getValue()).isEqualTo(tokenB.getValue());
    }

    @Test
    @DisplayName("TC-09. Default Constructor")
    void defaultConstructor_ShouldInitializeWithNullValues() {
        // Arrange & Act
        Token token = new Token();

        // Assert
        assertThat(token.getType()).isNull();
        assertThat(token.getValue()).isNull();
    }
}

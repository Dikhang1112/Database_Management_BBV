package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentifierASTNodeTest {

    @Test
    @DisplayName("TC-21. Build Identifier AST Node")
    void buildIdentifierASTNode_ShouldPreserveQualifiedIdentifier_WhenParsingIdentifier() {
        // Arrange
        String columnName = "users.user_id";

        // Act
        IdentifierASTNode node = new IdentifierASTNode(columnName);

        // Assert
        assertThat(node.getNodeName()).isEqualTo("IdentifierASTNode");
        assertThat(node.getValue()).isEqualTo("users.user_id");
    }

    @Test
    @DisplayName("TC-22. Column Identifier")
    void columnIdentifier_ShouldStoreSimpleColumnName() {
        // Act
        IdentifierASTNode node = new IdentifierASTNode("email");

        // Assert
        assertThat(node.getValue()).isEqualTo("email");
    }

    @Test
    @DisplayName("TC-23. Default Constructor")
    void defaultConstructor_ShouldInitializeWithNullValue() {
        // Act
        IdentifierASTNode node = new IdentifierASTNode();

        // Assert
        assertThat(node.getNodeName()).isEqualTo("IdentifierASTNode");
        assertThat(node.getValue()).isNull();
    }
}

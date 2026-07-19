package level_3.query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LiteralASTNodeTest {

    @Test
    @DisplayName("TC-24. Build Literal AST Node")
    void buildLiteralASTNode_ShouldInferDataType_WhenParsingLiteralValue() {
        // Arrange
        String rawValue = "100";
        String inferredType = "INTEGER";

        // Act
        LiteralASTNode node = new LiteralASTNode(rawValue, inferredType);

        // Assert
        assertThat(node.getNodeName()).isEqualTo("LiteralASTNode");
        assertThat(node.getValue()).isEqualTo("100");
        assertThat(node.getInferredType()).isEqualTo("INTEGER");
    }

    @Test
    @DisplayName("TC-25. Integer Literal")
    void integerLiteral_ShouldStoreIntegerDataType() {
        // Act
        LiteralASTNode node = new LiteralASTNode("42", "INT");

        // Assert
        assertThat(node.getValue()).isEqualTo("42");
        assertThat(node.getInferredType()).isEqualTo("INT");
    }

    @Test
    @DisplayName("TC-26. String Literal")
    void stringLiteral_ShouldStoreStringDataType() {
        // Act
        LiteralASTNode node = new LiteralASTNode("'John Doe'", "VARCHAR");

        // Assert
        assertThat(node.getValue()).isEqualTo("'John Doe'");
        assertThat(node.getInferredType()).isEqualTo("VARCHAR");
    }

    @Test
    @DisplayName("TC-27. Boolean Literal")
    void booleanLiteral_ShouldStoreBooleanDataType() {
        // Act
        LiteralASTNode node = new LiteralASTNode("TRUE", "BOOLEAN");

        // Assert
        assertThat(node.getValue()).isEqualTo("TRUE");
        assertThat(node.getInferredType()).isEqualTo("BOOLEAN");
    }

    @Test
    @DisplayName("TC-28. NULL Literal")
    void nullLiteral_ShouldStoreNullDataType() {
        // Act
        LiteralASTNode node = new LiteralASTNode("NULL", "NULL");

        // Assert
        assertThat(node.getValue()).isEqualTo("NULL");
        assertThat(node.getInferredType()).isEqualTo("NULL");
    }

    @Test
    @DisplayName("TC-29. Default Constructor")
    void defaultConstructor_ShouldInitializeWithNullFields() {
        // Act
        LiteralASTNode node = new LiteralASTNode();

        // Assert
        assertThat(node.getNodeName()).isEqualTo("LiteralASTNode");
        assertThat(node.getValue()).isNull();
        assertThat(node.getInferredType()).isNull();
    }
}

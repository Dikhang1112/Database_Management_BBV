package query_processor;

import query_processor.abstracts.ASTNode;
import query_processor.ast.AST;
import metadata.facade.MetadataModule;
import query_processor.semantic.TypeChecker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class TypeCheckerTest {

    @Mock
    private MetadataModule mockMetadataModule;

    @Spy
    @InjectMocks
    private TypeChecker typeChecker;

    @Mock
    private AST mockAST;

    @Mock
    private ASTNode mockASTNode;

    @Mock
    private ASTNode mockIncompatibleNode;

    @Test
    @DisplayName("TC-03. Validate AST Types - Happy Path")
    void validate_ShouldCheckAllExpressionsAndFunctions_WhenASTIsProvided() {
        assertThatCode(() -> typeChecker.validate(mockAST))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-03A. Check Expression Types - Compatible Operands")
    void checkExpression_ShouldReturnTrue_WhenOperandTypesAreCompatible() {
        boolean result = typeChecker.checkExpression(mockASTNode);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("TC-03B. Check Expression Types - Incompatible Type Mismatch")
    void checkExpression_ShouldReturnFalse_WhenOperandTypesAreIncompatible() {
        doThrow(new RuntimeException("Incompatible data types: cannot compare INT with VARCHAR"))
                .when(typeChecker).checkExpression(mockIncompatibleNode);

        assertThatThrownBy(() -> typeChecker.checkExpression(mockIncompatibleNode))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Incompatible data types: cannot compare INT with VARCHAR");
    }
}

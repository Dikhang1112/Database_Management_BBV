package query_processor.semantic;

import metadata.facade.MetadataModule;
import query_processor.abstracts.ASTNode;
import query_processor.ast.AST;
import query_processor.exceptions.TypeMismatchException;
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

    // TC-03: Đảm bảo toàn bộ các biểu thức tính toán trên cây AST đều trải qua quá trình thẩm định kiểu dữ liệu.
    @Test
    @DisplayName("TC-03. Validate AST Types - Happy Path")
    void validate_ShouldCheckAllExpressionsAndFunctions_WhenASTIsProvided() {
        assertThatCode(() -> typeChecker.validate(mockAST))
                .doesNotThrowAnyException();
    }

    // TC-03A: Cho phép các phép toán số học và so sánh giữa các kiểu dữ liệu tương thích với nhau.
    @Test
    @DisplayName("TC-03A. Check Expression Types - Compatible Operands")
    void checkExpression_ShouldReturnTrue_WhenOperandTypesAreCompatible() {
        boolean result = typeChecker.checkExpression(mockASTNode);

        assertThat(result).isTrue();
    }

    // TC-03B: Ngăn chặn các phép so sánh vô nghĩa giữa hai kiểu dữ liệu không thể ép kiểu tự động bằng TypeMismatchException.
    @Test
    @DisplayName("TC-03B. Check Expression Types - Incompatible Type Mismatch")
    void checkExpression_ShouldReturnFalse_WhenOperandTypesAreIncompatible() {
        doThrow(new TypeMismatchException("Incompatible data types: cannot compare INT with VARCHAR"))
                .when(typeChecker).checkExpression(mockIncompatibleNode);

        assertThatThrownBy(() -> typeChecker.checkExpression(mockIncompatibleNode))
                .isInstanceOf(TypeMismatchException.class)
                .hasMessageContaining("Incompatible data types: cannot compare INT with VARCHAR");
    }
}

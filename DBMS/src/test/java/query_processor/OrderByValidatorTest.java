package query_processor;

import query_processor.ast.AST;
import query_processor.semantic.OrderByValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class OrderByValidatorTest {

    @Spy
    private OrderByValidator orderByValidator;

    @Mock
    private AST mockValidAST;

    @Mock
    private AST mockAmbiguousColumnAST;

    @Mock
    private AST mockDistinctMissingAST;

    @Test
    @DisplayName("TC-05. Validate ORDER BY Clause - Happy Path")
    void validate_ShouldPass_WhenOrderByColumnsAreValid() {
        assertThatCode(() -> orderByValidator.validate(mockValidAST))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-05A. Validate ORDER BY Clause - Ambiguous Sort Key")
    void validate_ShouldThrowException_WhenOrderByColumnIsAmbiguous() {
        doThrow(new RuntimeException("Ambiguous column reference 'created_at' in ORDER BY"))
                .when(orderByValidator).validate(mockAmbiguousColumnAST);

        assertThatThrownBy(() -> orderByValidator.validate(mockAmbiguousColumnAST))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ambiguous column reference 'created_at' in ORDER BY");
    }

    @Test
    @DisplayName("TC-05B. Validate ORDER BY Clause - DISTINCT Query Sort Key Missing from Projection")
    void validate_ShouldThrowException_WhenDistinctQuerySortColumnNotInSelectList() {
        doThrow(new RuntimeException("ORDER BY items must appear in select list if SELECT DISTINCT is specified"))
                .when(orderByValidator).validate(mockDistinctMissingAST);

        assertThatThrownBy(() -> orderByValidator.validate(mockDistinctMissingAST))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ORDER BY items must appear in select list if SELECT DISTINCT is specified");
    }
}

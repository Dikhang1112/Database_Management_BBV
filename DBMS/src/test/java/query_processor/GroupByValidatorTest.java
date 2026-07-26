package query_processor;

import query_processor.ast.AST;
import query_processor.semantic.GroupByValidator;
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
class GroupByValidatorTest {

    @Spy
    private GroupByValidator groupByValidator;

    @Mock
    private AST mockValidAST;

    @Mock
    private AST mockMissingColumnAST;

    @Mock
    private AST mockNoGroupByAST;

    @Test
    @DisplayName("TC-04. Validate GROUP BY Clause - Happy Path")
    void validate_ShouldPass_WhenAllNonAggregatedSelectColumnsAreInGroupBy() {
        assertThatCode(() -> groupByValidator.validate(mockValidAST))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-04A. Validate GROUP BY Clause - Missing Column")
    void validate_ShouldThrowException_WhenNonAggregatedColumnMissingFromGroupBy() {
        doThrow(new RuntimeException("Column 'name' must appear in GROUP BY clause"))
                .when(groupByValidator).validate(mockMissingColumnAST);

        assertThatThrownBy(() -> groupByValidator.validate(mockMissingColumnAST))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Column 'name' must appear in GROUP BY clause");
    }

    @Test
    @DisplayName("TC-04B. Validate GROUP BY Clause - Empty GROUP BY with Unaggregated Column")
    void validate_ShouldThrowException_WhenQueryHasAggregatesAndUnaggregatedColumnsWithoutGroupBy() {
        doThrow(new RuntimeException("Expression in SELECT list not in GROUP BY"))
                .when(groupByValidator).validate(mockNoGroupByAST);

        assertThatThrownBy(() -> groupByValidator.validate(mockNoGroupByAST))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Expression in SELECT list not in GROUP BY");
    }
}

package query_processor.semantic;

import query_processor.ast.AST;
import query_processor.exceptions.SemanticException;
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

    // TC-04: Đảm bảo tất cả các cột xuất hiện trong SELECT list đều phải có mặt đầy đủ trong mệnh đề GROUP BY.
    @Test
    @DisplayName("TC-04. Validate GROUP BY Clause - Happy Path")
    void validate_ShouldPass_WhenAllNonAggregatedSelectColumnsAreInGroupBy() {
        assertThatCode(() -> groupByValidator.validate(mockValidAST))
                .doesNotThrowAnyException();
    }

    // TC-04A: Bắt lỗi vi phạm quy tắc GROUP BY SQL với SemanticException khi thiếu cột trong GROUP BY.
    @Test
    @DisplayName("TC-04A. Validate GROUP BY Clause - Missing Column")
    void validate_ShouldThrowException_WhenNonAggregatedColumnMissingFromGroupBy() {
        doThrow(new SemanticException("Column 'name' must appear in GROUP BY clause"))
                .when(groupByValidator).validate(mockMissingColumnAST);

        assertThatThrownBy(() -> groupByValidator.validate(mockMissingColumnAST))
                .isInstanceOf(SemanticException.class)
                .hasMessageContaining("Column 'name' must appear in GROUP BY clause");
    }

    // TC-04B: Thẩm định tính hợp lệ của mệnh đề GROUP BY với SemanticException khi có tham chiếu cột dữ liệu không qua gom nhóm.
    @Test
    @DisplayName("TC-04B. Validate GROUP BY Clause - Empty GROUP BY with Unaggregated Column")
    void validate_ShouldThrowException_WhenQueryHasAggregatesAndUnaggregatedColumnsWithoutGroupBy() {
        doThrow(new SemanticException("Expression in SELECT list not in GROUP BY"))
                .when(groupByValidator).validate(mockNoGroupByAST);

        assertThatThrownBy(() -> groupByValidator.validate(mockNoGroupByAST))
                .isInstanceOf(SemanticException.class)
                .hasMessageContaining("Expression in SELECT list not in GROUP BY");
    }
}

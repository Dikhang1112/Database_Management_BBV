package query_processor.semantic;

import query_processor.abstracts.ASTNode;
import query_processor.ast.AST;
import query_processor.exceptions.SemanticException;
import query_processor.exceptions.TableNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SemanticAnalyzerTest {

    private SemanticAnalyzer semanticAnalyzer;

    @Mock
    private NameResolver mockNameResolver;

    @Mock
    private TypeChecker mockTypeChecker;

    @Mock
    private GroupByValidator mockGroupByValidator;

    @Mock
    private OrderByValidator mockOrderByValidator;

    @Mock
    private AST mockAST;

    @Mock
    private ASTNode mockASTNode;

    @BeforeEach
    void setUp() {
        semanticAnalyzer = new SemanticAnalyzer(
                mockNameResolver,
                mockTypeChecker,
                mockGroupByValidator,
                mockOrderByValidator
        );
    }

    // TC-01: Kiểm thử quy trình phân tích ngữ nghĩa 4 bước tiêu chuẩn theo đúng thứ tự bắt buộc: NameResolver -> TypeChecker -> GroupByValidator -> OrderByValidator.
    @Test
    @DisplayName("TC-01. Analyze Full AST - Happy Path")
    void analyze_ShouldExecuteFourStepValidationInOrder_WhenValidASTProvided() {
        semanticAnalyzer.analyze(mockAST);

        InOrder inOrder = inOrder(
                mockNameResolver,
                mockTypeChecker,
                mockGroupByValidator,
                mockOrderByValidator
        );

        inOrder.verify(mockNameResolver).resolve(mockAST);
        inOrder.verify(mockTypeChecker).validate(mockAST);
        inOrder.verify(mockGroupByValidator).validate(mockAST);
        inOrder.verify(mockOrderByValidator).validate(mockAST);
    }

    // TC-01A: Kiểm thử khả năng phòng thủ của SemanticAnalyzer khi đối tượng AST truyền vào bị null.
    @Test
    @DisplayName("TC-01A. Analyze Null AST")
    void analyze_ShouldHandleNullASTGracefully_WhenASTIsNull() {
        assertThatCode(() -> semanticAnalyzer.analyze(null))
                .doesNotThrowAnyException();
    }

    // TC-01B: Xác minh cơ chế Double-dispatch của mẫu thiết kế Visitor Pattern: khi duyệt nút phải ủy quyền lại cho node.accept(this).
    @Test
    @DisplayName("TC-01B. Visit AST Node - Visitor Pattern")
    void visit_ShouldInvokeAcceptOnASTNode_WhenValidNodeVisited() {
        semanticAnalyzer.visit(mockASTNode);

        verify(mockASTNode).accept(semanticAnalyzer);
    }

    // TC-01C: Đảm bảo bộ duyệt bỏ qua các nút null một cách an toàn mà không bị crash hệ thống.
    @Test
    @DisplayName("TC-01C. Visit Null AST Node")
    void visit_ShouldDoNothing_WhenASTNodeIsNull() {
        assertThatCode(() -> semanticAnalyzer.visit(null))
                .doesNotThrowAnyException();
    }

    // TC-01D: Kiểm thử cơ chế ngắt sớm (Fail-fast): khi gặp lỗi TableNotFoundException thì dừng quy trình và không thực thi các bước phía sau.
    @Test
    @DisplayName("TC-01D. Semantic Analysis Error Propagation")
    void analyze_ShouldPropagateException_WhenAnyStepValidationFails() {
        doThrow(new TableNotFoundException("Table 'missing' not found"))
                .when(mockNameResolver).resolve(mockAST);

        assertThatThrownBy(() -> semanticAnalyzer.analyze(mockAST))
                .isInstanceOf(TableNotFoundException.class)
                .hasMessageContaining("Table 'missing' not found");

        verify(mockNameResolver).resolve(mockAST);
        verifyNoInteractions(mockTypeChecker, mockGroupByValidator, mockOrderByValidator);
    }
}

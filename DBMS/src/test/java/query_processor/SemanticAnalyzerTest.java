package query_processor;

import query_processor.abstracts.ASTNode;
import query_processor.ast.AST;
import query_processor.semantic.*;
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

    @Test
    @DisplayName("TC-01A. Analyze Null AST")
    void analyze_ShouldHandleNullASTGracefully_WhenASTIsNull() {
        assertThatCode(() -> semanticAnalyzer.analyze(null))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-01B. Visit AST Node - Visitor Pattern")
    void visit_ShouldInvokeAcceptOnASTNode_WhenValidNodeVisited() {
        semanticAnalyzer.visit(mockASTNode);

        verify(mockASTNode).accept(semanticAnalyzer);
    }

    @Test
    @DisplayName("TC-01C. Visit Null AST Node")
    void visit_ShouldDoNothing_WhenASTNodeIsNull() {
        assertThatCode(() -> semanticAnalyzer.visit(null))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("TC-01D. Semantic Analysis Error Propagation")
    void analyze_ShouldPropagateException_WhenAnyStepValidationFails() {
        doThrow(new RuntimeException("Table 'missing' not found"))
                .when(mockNameResolver).resolve(mockAST);

        assertThatThrownBy(() -> semanticAnalyzer.analyze(mockAST))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Table 'missing' not found");

        verify(mockNameResolver).resolve(mockAST);
        verifyNoInteractions(mockTypeChecker, mockGroupByValidator, mockOrderByValidator);
    }
}

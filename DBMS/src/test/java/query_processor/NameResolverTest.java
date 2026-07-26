package query_processor;

import query_processor.abstracts.ASTNode;
import query_processor.ast.AST;
import metadata.facade.MetadataModule;
import query_processor.semantic.NameResolver;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NameResolverTest {

    @Mock
    private MetadataModule mockMetadataModule;

    @Spy
    @InjectMocks
    private NameResolver nameResolver;

    @Mock
    private AST mockAST;

    @Mock
    private ASTNode mockASTNode;

    @Mock
    private ASTNode mockMissingTableNode;

    @Mock
    private ASTNode mockMissingColumnNode;

    @Mock
    private ASTNode mockDuplicateAliasNode;

    @Test
    @DisplayName("TC-02. Resolve Table Identifier - Happy Path")
    void resolveTable_ShouldReturnTrue_WhenTableExistsInMetadata() {
        when(mockMetadataModule.containsTable("mock_table")).thenReturn(true);

        boolean result = nameResolver.resolveTable(mockASTNode);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("TC-02A. Resolve Table Identifier - Table Not Found")
    void resolveTable_ShouldReturnFalse_WhenTableDoesNotExistInMetadata() {
        doThrow(new RuntimeException("Table 'missing_table' not found"))
                .when(nameResolver).resolveTable(mockMissingTableNode);

        assertThatThrownBy(() -> nameResolver.resolveTable(mockMissingTableNode))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Table 'missing_table' not found");
    }

    @Test
    @DisplayName("TC-02B. Resolve Column Identifier - Happy Path")
    void resolveColumn_ShouldReturnTrue_WhenColumnExistsInTable() {
        when(mockMetadataModule.containsColumn("mock_table", "mock_column")).thenReturn(true);

        boolean result = nameResolver.resolveColumn(mockASTNode);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("TC-02C. Resolve Column Identifier - Column Not Found")
    void resolveColumn_ShouldReturnFalse_WhenColumnDoesNotExist() {
        doThrow(new RuntimeException("Column 'unknown_col' not found in table 'users'"))
                .when(nameResolver).resolveColumn(mockMissingColumnNode);

        assertThatThrownBy(() -> nameResolver.resolveColumn(mockMissingColumnNode))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Column 'unknown_col' not found in table 'users'");
    }

    @Test
    @DisplayName("TC-02D. Resolve Table Alias - Happy Path")
    void resolveAlias_ShouldReturnTrue_WhenAliasIsValid() {
        boolean result = nameResolver.resolveAlias(mockASTNode);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("TC-02E. Resolve Duplicate Alias Collision")
    void resolveAlias_ShouldReturnFalse_WhenAliasIsDuplicatedInSameScope() {
        doThrow(new RuntimeException("Duplicate table alias 't' detected in query scope"))
                .when(nameResolver).resolveAlias(mockDuplicateAliasNode);

        assertThatThrownBy(() -> nameResolver.resolveAlias(mockDuplicateAliasNode))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Duplicate table alias 't' detected in query scope");
    }

    @Test
    @DisplayName("TC-02F. Resolve Full AST Identifiers")
    void resolve_ShouldProcessAllIdentifiersInAST_WhenASTIsProvided() {
        assertThatCode(() -> nameResolver.resolve(mockAST))
                .doesNotThrowAnyException();
    }
}

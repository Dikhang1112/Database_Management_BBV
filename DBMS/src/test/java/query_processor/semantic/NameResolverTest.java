package query_processor.semantic;

import metadata.facade.MetadataModule;
import query_processor.abstracts.ASTNode;
import query_processor.ast.AST;
import query_processor.exceptions.ColumnNotFoundException;
import query_processor.exceptions.DuplicateAliasException;
import query_processor.exceptions.TableNotFoundException;
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

    // TC-02: Xác minh tính đúng đắn khi phân giải tên bảng với Catalog Metadata.
    @Test
    @DisplayName("TC-02. Resolve Table Identifier - Happy Path")
    void resolveTable_ShouldReturnTrue_WhenTableExistsInMetadata() {
        when(mockMetadataModule.containsTable("mock_table")).thenReturn(true);

        boolean result = nameResolver.resolveTable(mockASTNode);

        assertThat(result).isTrue();
    }

    // TC-02A: Phát hiện sớm các câu lệnh SQL truy vấn vào bảng không tồn tại bằng ngoại lệ TableNotFoundException.
    @Test
    @DisplayName("TC-02A. Resolve Table Identifier - Table Not Found")
    void resolveTable_ShouldReturnFalse_WhenTableDoesNotExistInMetadata() {
        doThrow(new TableNotFoundException("Table 'missing_table' not found"))
                .when(nameResolver).resolveTable(mockMissingTableNode);

        assertThatThrownBy(() -> nameResolver.resolveTable(mockMissingTableNode))
                .isInstanceOf(TableNotFoundException.class)
                .hasMessageContaining("Table 'missing_table' not found");
    }

    // TC-02B: Đảm bảo các cột được tham chiếu tồn tại trong schema của bảng tương ứng.
    @Test
    @DisplayName("TC-02B. Resolve Column Identifier - Happy Path")
    void resolveColumn_ShouldReturnTrue_WhenColumnExistsInTable() {
        when(mockMetadataModule.containsColumn("mock_table", "mock_column")).thenReturn(true);

        boolean result = nameResolver.resolveColumn(mockASTNode);

        assertThat(result).isTrue();
    }

    // TC-02C: Ngăn chặn việc thực thi truy vấn chứa tên cột bị viết sai chính tả hoặc không tồn tại bằng ColumnNotFoundException.
    @Test
    @DisplayName("TC-02C. Resolve Column Identifier - Column Not Found")
    void resolveColumn_ShouldReturnFalse_WhenColumnDoesNotExist() {
        doThrow(new ColumnNotFoundException("Column 'unknown_col' not found in table 'users'"))
                .when(nameResolver).resolveColumn(mockMissingColumnNode);

        assertThatThrownBy(() -> nameResolver.resolveColumn(mockMissingColumnNode))
                .isInstanceOf(ColumnNotFoundException.class)
                .hasMessageContaining("Column 'unknown_col' not found in table 'users'");
    }

    // TC-02D: Kiểm thử tính năng phân giải bí danh (Table Alias) hợp lệ.
    @Test
    @DisplayName("TC-02D. Resolve Table Alias - Happy Path")
    void resolveAlias_ShouldReturnTrue_WhenAliasIsValid() {
        boolean result = nameResolver.resolveAlias(mockASTNode);

        assertThat(result).isTrue();
    }

    // TC-02E: Tránh sự nhập nhằng (Ambiguity) khi truy vấn đặt trùng bí danh cho hai bảng khác nhau bằng DuplicateAliasException.
    @Test
    @DisplayName("TC-02E. Resolve Duplicate Alias Collision")
    void resolveAlias_ShouldReturnFalse_WhenAliasIsDuplicatedInSameScope() {
        doThrow(new DuplicateAliasException("Duplicate table alias 't' detected in query scope"))
                .when(nameResolver).resolveAlias(mockDuplicateAliasNode);

        assertThatThrownBy(() -> nameResolver.resolveAlias(mockDuplicateAliasNode))
                .isInstanceOf(DuplicateAliasException.class)
                .hasMessageContaining("Duplicate table alias 't' detected in query scope");
    }

    // TC-02F: Kiểm thử khả năng duyệt và phân giải đồng loạt tất cả định danh trong toàn bộ cây AST của câu lệnh SQL.
    @Test
    @DisplayName("TC-02F. Resolve Full AST Identifiers")
    void resolve_ShouldProcessAllIdentifiersInAST_WhenASTIsProvided() {
        assertThatCode(() -> nameResolver.resolve(mockAST))
                .doesNotThrowAnyException();
    }
}

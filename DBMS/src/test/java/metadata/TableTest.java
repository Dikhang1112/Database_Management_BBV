package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableTest {

    @Mock
    private Column mockColumn;

    @Test
    @DisplayName("TC-09. Add Column")
    void addColumn_ShouldAttachColumnToTable_WhenColumnIsAdded() {
        Table table = new Table("orders");
        when(mockColumn.getColumnName()).thenReturn("order_id");

        table.addColumn(mockColumn);
        List<Column> columns = table.listColumns();

        assertThat(columns).contains(mockColumn);
        assertThat(table.containsColumn("order_id")).isTrue();
    }

    @Test
    @DisplayName("TC-09A. Add Column - Column Already Exists")
    void addColumn_ShouldThrowException_WhenColumnAlreadyExists() {
        Table table = new Table("orders");
        when(mockColumn.getColumnName()).thenReturn("order_id");
        table.addColumn(mockColumn);

        assertThatThrownBy(() -> table.addColumn(mockColumn))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Column already exists");
    }

    @Test
    @DisplayName("TC-09B. Add Column - Table Locked")
    void addColumn_ShouldThrowException_WhenTableIsLocked() {
        Table table = new Table("orders");
        table.setLocked(true);

        assertThatThrownBy(() -> table.addColumn(mockColumn))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Table is locked");
    }

    @Test
    @DisplayName("TC-09C. Add Column - Permission Denied")
    void addColumn_ShouldThrowException_WhenPermissionDenied() {
        Table table = new Table("orders");
        when(mockColumn.getColumnName()).thenReturn("secret_col");

        assertThatThrownBy(() -> table.addColumn(mockColumn))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Permission denied");
    }

    @Test
    @DisplayName("TC-09D. Add Column - Special Characters")
    void addColumn_ShouldThrowException_WhenColumnNameContainsSpecialCharacters() {
        Table table = new Table("orders");
        when(mockColumn.getColumnName()).thenReturn("col#name!");

        assertThatThrownBy(() -> table.addColumn(mockColumn))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Column name contains invalid characters");
    }

    @Test
    @DisplayName("TC-10. Remove Column")
    void removeColumn_ShouldDetachColumnFromTable_WhenColumnExists() {
        Table table = new Table("orders");
        Column column = new Column("temp_col", DataType.VARCHAR);
        table.addColumn(column);

        table.removeColumn("temp_col");

        assertThat(table.containsColumn("temp_col")).isFalse();
    }

    @Test
    @DisplayName("TC-10A. Remove Column - Column Not Found")
    void removeColumn_ShouldThrowException_WhenColumnNotFound() {
        Table table = new Table("orders");

        assertThatThrownBy(() -> table.removeColumn("missing_col"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Column not found");
    }

    @Test
    @DisplayName("TC-10B. Remove Column - Referenced by Constraint")
    void removeColumn_ShouldThrowException_WhenReferencedByConstraint() {
        Table table = new Table("orders");
        table.addColumn(new Column("id", DataType.INT));

        assertThatThrownBy(() -> table.removeColumn("id"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("referenced by constraint");
    }
}

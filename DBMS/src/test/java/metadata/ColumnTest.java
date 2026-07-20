package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ColumnTest {

    @Test
    @DisplayName("TC-11. Change Data Type")
    void changeDataType_ShouldUpdateColumnDataType_WhenValid() {
        Column column = new Column("age", DataType.INT);

        column.changeDataType(DataType.BIGINT);

        assertThat(column.getDataType()).isEqualTo(DataType.BIGINT);
    }

    @Test
    @DisplayName("TC-11A. Change Data Type - Unsupported Conversion")
    void changeDataType_ShouldThrowException_WhenUnsupportedConversion() {
        Column column = new Column("age", DataType.INT);

        assertThatThrownBy(() -> column.changeDataType(DataType.BOOLEAN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported conversion");
    }

    @Test
    @DisplayName("TC-11B. Change Data Type - Permission Denied")
    void changeDataType_ShouldThrowException_WhenPermissionDenied() {
        Column column = new Column("protected_col", DataType.INT);

        assertThatThrownBy(() -> column.changeDataType(DataType.VARCHAR))
                .isInstanceOf(IllegalAccessError.class).hasMessageContaining("Permission denied");
    }

    @Test
    @DisplayName("TC-12. Column Operations & Properties")
    void changeDataTypeAndsetDefaultValue_ShouldUpdateColumnProperties_WhenModified() {
        Column column = new Column("age", DataType.INT);

        column.setNullable(false);
        column.setDefaultValue("18");
        column.changeDataType(DataType.BIGINT);
        column.rename("user_age");

        assertThat(column.getColumnName()).isEqualTo("user_age");
        assertThat(column.getDataType()).isEqualTo(DataType.BIGINT);
        assertThat(column.isNullable()).isFalse();
        assertThat(column.getDefaultValue()).isEqualTo("18");
    }

    @Test
    @DisplayName("TC-12A. Set Default Value - Invalid Default Value")
    void setDefaultValue_ShouldThrowException_WhenDefaultValueIsInvalid() {
        Column column = new Column("age", DataType.INT);

        assertThatThrownBy(() -> column.setDefaultValue("abc_not_an_int"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid default value");
    }
}

package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ColumnBuilderTest {

    @Test
    @DisplayName("TC-19. Build Column")
    void build_ShouldConstructColumn_WhenValidPropertiesSet() {
        Column column = new ColumnBuilder("email")
                .setType(DataType.VARCHAR)
                .setNullable(false)
                .setDefaultValue("N/A")
                .build();

        assertThat(column).isNotNull();
        assertThat(column.getColumnName()).isEqualTo("email");
        assertThat(column.getDataType()).isEqualTo(DataType.VARCHAR);
        assertThat(column.isNullable()).isFalse();
        assertThat(column.getDefaultValue()).isEqualTo("N/A");
    }

    @Test
    @DisplayName("TC-19A. Build Column with Default Values")
    void build_ShouldApplyDefaults_WhenOptionalPropertiesOmitted() {
        Column column = new ColumnBuilder("id")
                .setType(DataType.INT)
                .build();

        assertThat(column).isNotNull();
        assertThat(column.getColumnName()).isEqualTo("id");
        assertThat(column.getDataType()).isEqualTo(DataType.INT);
        assertThat(column.isNullable()).isTrue();
        assertThat(column.getDefaultValue()).isNull();
    }

    @Test
    @DisplayName("TC-19B. Build Column - Null or Empty Name")
    void build_ShouldThrowException_WhenColumnNameIsNullOrEmpty() {
        assertThatThrownBy(() -> new ColumnBuilder(null).setType(DataType.INT).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Value is empty");

        assertThatThrownBy(() -> new ColumnBuilder("").setType(DataType.INT).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Value is empty");
    }

    @Test
    @DisplayName("TC-19C. Build Column - Invalid Characters in Name")
    void build_ShouldThrowException_WhenColumnNameContainsInvalidCharacters() {
        assertThatThrownBy(() -> new ColumnBuilder("invalid#col").setType(DataType.INT).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Column name contains invalid characters");
    }

    @Test
    @DisplayName("TC-19D. Build Column - Null Data Type")
    void build_ShouldThrowException_WhenDataTypeIsNull() {
        assertThatThrownBy(() -> new ColumnBuilder("email").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Data type cannot be null");
    }

    @Test
    @DisplayName("TC-19E. Build Column - Invalid Default Value")
    void build_ShouldThrowException_WhenDefaultValueIsInvalidForType() {
        assertThatThrownBy(() -> new ColumnBuilder("age")
                .setType(DataType.INT)
                .setDefaultValue("not_an_int")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid default value");
    }
}

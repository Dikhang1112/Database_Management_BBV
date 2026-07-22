package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnBuilderTest {

    @Test
    @DisplayName("TC-20. Build Column")
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
    @DisplayName("TC-20A. Build Column with Default Values")
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
}

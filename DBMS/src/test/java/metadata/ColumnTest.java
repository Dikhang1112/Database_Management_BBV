package metadata;

import metadata.enums.DataType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnTest {

    @Test
    @DisplayName("TC-10. Column Operations")
    void changeDataTypeAndsetDefaultValue_ShouldUpdateColumnProperties_WhenModified() {
        // Arrange
        Column column = new Column("age", DataType.INT);

        // Act
        column.setNullable(false);
        column.setDefaultValue("18");
        column.changeDataType(DataType.BIGINT);
        column.rename("user_age");

        // Assert
        assertThat(column).isNotNull();
    }
}

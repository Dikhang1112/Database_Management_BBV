package metadata;

import metadata.interfaces.DDLCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MetadataModuleTest {

    private MetadataModule metadataModule;

    @Mock
    private DDLCommand mockCommand;

    @BeforeEach
    void setUp() {
        metadataModule = MetadataModule.getInstance();
        if (metadataModule.getCatalogManager() != null) {
            metadataModule.getCatalogManager().clear();
        }
    }

    @Test
    @DisplayName("TC-18. Get Table Facade")
    void getTable_ShouldReturnTable_WhenDatabaseSchemaAndTableExist() {
        CatalogManager cm = metadataModule.getCatalogManager();
        Database db = cm.createDatabase("app_db");
        Schema schema = db.createSchema("public");
        Table table = schema.createTable("users");

        Table result = metadataModule.getTable("app_db", "public", "users");

        assertThat(result).isEqualTo(table);
    }

    @Test
    @DisplayName("TC-18B. Get Database Facade - Happy Path")
    void getDatabase_ShouldReturnDatabase_WhenExists() {
        CatalogManager cm = metadataModule.getCatalogManager();
        Database db = cm.createDatabase("app_db");

        Database result = metadataModule.getDatabase("app_db");

        assertThat(result).isEqualTo(db);
    }

    @Test
    @DisplayName("TC-18C. Get Database Facade - Invalid Name")
    void getDatabase_ShouldThrowException_WhenDatabaseNameIsInvalid() {
        assertThatThrownBy(() -> metadataModule.getDatabase("invalid#db"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Database name contains invalid characters");
    }

    @Test
    @DisplayName("TC-18D. Get Table Facade - Invalid Identifier")
    void getTable_ShouldThrowException_WhenAnyIdentifierIsInvalid() {
        assertThatThrownBy(() -> metadataModule.getTable("invalid#db", "public", "users"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Database name contains invalid characters");
    }

    @Test
    @DisplayName("TC-18E. Get Table Facade - Not Found")
    void getTable_ShouldReturnNull_WhenDatabaseDoesNotExist() {
        Table result = metadataModule.getTable("missing_db", "public", "users");

        assertThat(result).isNull();
    }
}

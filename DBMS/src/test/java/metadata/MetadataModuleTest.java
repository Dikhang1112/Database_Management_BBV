package metadata;

import metadata.interfaces.DDLCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("TC-18A. Execute DDL Facade")
    void executeDDL_ShouldInvokeCommandExecute_WhenCommandIsProvided() {
        metadataModule.executeDDL(mockCommand);

        verify(mockCommand).execute();
    }
}

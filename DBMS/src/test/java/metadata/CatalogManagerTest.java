package metadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CatalogManagerTest {

    private CatalogManager catalogManager;

    @BeforeEach
    void setUp() {
        catalogManager = new CatalogManager();
    }

    @Test
    @DisplayName("TC-01. Create Database")
    void createDatabase_ShouldAddDatabaseToCatalog_WhenValidNameIsProvided() {
        Database db = catalogManager.createDatabase("sales_db");

        assertThat(db).isNotNull();
        assertThat(catalogManager.containsDatabase("sales_db")).isTrue();
        assertThat(catalogManager.getDatabase("sales_db")).isEqualTo(db);
    }

    @Test
    @DisplayName("TC-01A. Create Database - Already Exists")
    void createDatabase_ShouldThrowException_WhenDatabaseAlreadyExists() {
        catalogManager.createDatabase("sales_db");

        assertThatThrownBy(() -> catalogManager.createDatabase("sales_db"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Database already exists");
    }

    @Test
    @DisplayName("TC-01B. Create Database - Invalid Name")
    void createDatabase_ShouldThrowException_WhenDatabaseNameIsInvalid() {
        assertThatThrownBy(() -> catalogManager.createDatabase(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid database name");
    }

    @Test
    @DisplayName("TC-01C. Create Database - Permission Denied")
    void createDatabase_ShouldThrowException_WhenPermissionDenied() {
        assertThatThrownBy(() -> catalogManager.createDatabase("protected_db"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Permission denied");
    }

    @Test
    @DisplayName("TC-02. Drop Database")
    void dropDatabase_ShouldRemoveDatabaseFromCatalog_WhenDatabaseExists() {
        catalogManager.createDatabase("temp_db");

        catalogManager.dropDatabase("temp_db");

        assertThat(catalogManager.containsDatabase("temp_db")).isFalse();
    }

    @Test
    @DisplayName("TC-02A. Drop Database - Not Found")
    void dropDatabase_ShouldThrowException_WhenDatabaseNotFound() {
        assertThatThrownBy(() -> catalogManager.dropDatabase("missing_db"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Database not found");
    }

    @Test
    @DisplayName("TC-02B. Drop Database - Database Not Empty")
    void dropDatabase_ShouldThrowException_WhenDatabaseIsNotEmpty() {
        Database db = catalogManager.createDatabase("db_with_schemas");
        db.createSchema("public");

        assertThatThrownBy(() -> catalogManager.dropDatabase("db_with_schemas"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Database is not empty");
    }

    @Test
    @DisplayName("TC-02C. Drop Database - Permission Denied")
    void dropDatabase_ShouldThrowException_WhenPermissionDenied() {
        catalogManager.createDatabase("prod_db");

        assertThatThrownBy(() -> catalogManager.dropDatabase("prod_db"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Permission denied");
    }

    @Test
    @DisplayName("TC-03. Get & List Databases")
    void listDatabases_ShouldReturnAllRegisteredDatabases_WhenDatabasesAreCreated() {
        Database db1 = catalogManager.createDatabase("db1");
        Database db2 = catalogManager.createDatabase("db2");

        List<Database> databases = catalogManager.listDatabases();

        assertThat(databases).hasSize(2).contains(db1, db2);
        assertThat(catalogManager.getDatabase("db1")).isEqualTo(db1);
    }

    @Test
    @DisplayName("TC-04. Clear Catalog")
    void clear_ShouldRemoveAllDatabases_WhenCatalogIsCleared() {
        catalogManager.createDatabase("db1");

        catalogManager.clear();

        assertThat(catalogManager.listDatabases()).isEmpty();
    }
}

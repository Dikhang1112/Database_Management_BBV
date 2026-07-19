package level_3.metadata;

import level_3.Metadata.CatalogManager;
import level_3.Metadata.Database;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogManagerTest {

    @Test
    @DisplayName("TC-01. Create Database")
    void createDatabase_ShouldAddDatabaseToCatalog_WhenValidNameIsProvided() {
        // Arrange
        CatalogManager catalogManager = new CatalogManager();

        // Act
        Database db = catalogManager.createDatabase("sales_db");

        // Assert
        assertThat(db).isNotNull();
        assertThat(catalogManager.containsDatabase("sales_db")).isTrue();
        assertThat(catalogManager.getDatabase("sales_db")).isEqualTo(db);
    }

    @Test
    @DisplayName("TC-02. Drop Database")
    void dropDatabase_ShouldRemoveDatabaseFromCatalog_WhenDatabaseExists() {
        // Arrange
        CatalogManager catalogManager = new CatalogManager();
        catalogManager.createDatabase("temp_db");

        // Act
        catalogManager.dropDatabase("temp_db");

        // Assert
        assertThat(catalogManager.containsDatabase("temp_db")).isFalse();
    }

    @Test
    @DisplayName("TC-03. Get & List Databases")
    void listDatabases_ShouldReturnAllRegisteredDatabases_WhenDatabasesAreCreated() {
        // Arrange
        CatalogManager catalogManager = new CatalogManager();
        Database db1 = catalogManager.createDatabase("db1");
        Database db2 = catalogManager.createDatabase("db2");

        // Act
        List<Database> databases = catalogManager.listDatabases();

        // Assert
        assertThat(databases).hasSize(2).contains(db1, db2);
        assertThat(catalogManager.getDatabase("db1")).isEqualTo(db1);
    }

    @Test
    @DisplayName("TC-04. Clear Catalog")
    void clear_ShouldRemoveAllDatabases_WhenCatalogIsCleared() {
        // Arrange
        CatalogManager catalogManager = new CatalogManager();
        catalogManager.createDatabase("db1");

        // Act
        catalogManager.clear();

        // Assert
        assertThat(catalogManager.listDatabases()).isEmpty();
    }
}

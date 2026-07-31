package services;

import entity.metadata.domain.Database;
import entity.metadata.domain.Schema;
import entity.metadata.enums.DatabaseStatus;
import org.springframework.stereotype.Service;
import repositories.DatabaseRepository;

import java.util.List;

/**
 * Service handling business logic for Database operations (Managing Schemas and Database Status).
 * Managed by Spring Dependency Injection (@Service).
 */
@Service
public class DatabaseService {

    private final DatabaseRepository databaseRepository;

    public DatabaseService(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    /**
     * Creates a new Schema in the specified Database and persists changes to database.json.
     */
    public String createSchema(String dbName, String schemaName) {
        if (dbName == null || dbName.isBlank()) {
            throw new IllegalArgumentException("Database name cannot be empty");
        }
        if (schemaName == null || schemaName.isBlank()) {
            throw new IllegalArgumentException("Schema name cannot be empty");
        }

        Database db = databaseRepository.findDatabase(dbName);
        if (db == null) {
            throw new IllegalArgumentException("Database '" + dbName + "' does not exist");
        }
        if (db.getStatus() == DatabaseStatus.OFFLINE) {
            throw new IllegalStateException("Database is OFFLINE");
        }

        Schema schema = db.createSchema(schemaName);
        databaseRepository.saveDatabases();

        return schema.getSchemaName();
    }

    /**
     * Drops a Schema from the specified Database and persists changes to database.json.
     */
    public void dropSchema(String dbName, String schemaName) {
        Database db = databaseRepository.findDatabase(dbName);
        if (db != null) {
            db.dropSchema(schemaName);
            databaseRepository.saveDatabases();
        }
    }

    /**
     * Lists all Schema names in the specified Database.
     */
    public List<String> listSchemas(String dbName) {
        Database db = databaseRepository.findDatabase(dbName);
        if (db == null) {
            throw new IllegalArgumentException("Database '" + dbName + "' does not exist");
        }
        List<Schema> schemas = db.listSchemas();
        return schemas != null ? schemas.stream().map(Schema::getSchemaName).toList() : List.of();
    }

    /**
     * Finds Database by name.
     */
    public Database findDatabase(String dbName) {
        return databaseRepository.findDatabase(dbName);
    }
}

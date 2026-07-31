package services;

import dto.TableDTO;
import entity.metadata.domain.Schema;
import entity.metadata.domain.Table;
import org.springframework.stereotype.Service;
import repositories.SchemaRepository;

import java.util.List;

/**
 * Service handling business logic for Schema operations (Managing Tables and Schema Read-Only properties).
 * Managed by Spring Dependency Injection (@Service).
 */
@Service
public class SchemaService {

    private final SchemaRepository schemaRepository;

    public SchemaService(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    public Schema findSchema(String dbName, String schemaName) {
        return schemaRepository.findSchema(dbName, schemaName);
    }

    /**
     * Creates a new Table in the specified Schema and persists changes to schema.json.
     */
    public TableDTO createTable(String dbName, String schemaName, String tableName) {
        Schema schema = schemaRepository.findSchema(dbName, schemaName);
        if (schema == null) {
            throw new IllegalArgumentException("Schema '" + schemaName + "' does not exist");
        }
        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("Table name cannot be empty");
        }

        Table table = schema.createTable(tableName);
        schemaRepository.saveSchemas();

        return new TableDTO(table);
    }

    /**
     * Drops a Table from the specified Schema and persists changes to schema.json.
     */
    public void dropTable(String dbName, String schemaName, String tableName) {
        Schema schema = schemaRepository.findSchema(dbName, schemaName);
        if (schema != null) {
            schema.dropTable(tableName);
            schemaRepository.saveSchemas();
        }
    }

    /**
     * Lists all Tables in the specified Schema and maps them to TableDTO list.
     */
    public List<TableDTO> listTables(String dbName, String schemaName) {
        Schema schema = schemaRepository.findSchema(dbName, schemaName);
        if (schema == null) {
            throw new IllegalArgumentException("Schema '" + schemaName + "' does not exist");
        }
        List<Table> tables = schema.listTables();
        return tables != null ? tables.stream().map(TableDTO::new).toList() : List.of();
    }
}

package services;

import dto.ColumnDTO;
import entity.metadata.domain.Column;
import entity.metadata.domain.Table;
import entity.metadata.domain.TableMemento;
import entity.metadata.enums.DataType;
import org.springframework.stereotype.Service;
import repositories.TableRepository;

/**
 * Service handling business logic for Table operations (Memento Snapshot/Restore, Observer Events).
 * Managed by Spring Dependency Injection (@Service).
 */
@Service
public class TableService {

    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    /**
     * Finds a Table by Database, Schema, and Table names.
     */
    public Table findTable(String dbName, String schemaName, String tableName) {
        return tableRepository.findTable(dbName, schemaName, tableName);
    }

    /**
     * Memento Pattern: Creates a TableMemento snapshot of current column states.
     */
    public TableMemento createMemento(String dbName, String schemaName, String tableName) {
        Table table = tableRepository.findTable(dbName, schemaName, tableName);
        if (table == null) {
            throw new IllegalArgumentException("Table '" + tableName + "' does not exist");
        }
        return table.createMemento();
    }

    /**
     * Memento Pattern: Restores Table column state from a TableMemento snapshot and persists to table.json.
     */
    public void restoreMemento(String dbName, String schemaName, String tableName, TableMemento memento) {
        Table table = tableRepository.findTable(dbName, schemaName, tableName);
        if (table == null) {
            throw new IllegalArgumentException("Table '" + tableName + "' does not exist");
        }
        table.restore(memento);
        tableRepository.saveTables();
    }

    /**
     * Observer Pattern: Adds a new Column to Table, triggers COLUMN_ADDED event, and persists to table.json.
     */
    public ColumnDTO addColumn(String dbName, String schemaName, String tableName, String colName, String dataTypeStr) {
        Table table = tableRepository.findTable(dbName, schemaName, tableName);
        if (table == null) {
            throw new IllegalArgumentException("Table '" + tableName + "' does not exist");
        }
        if (colName == null || colName.isBlank()) {
            throw new IllegalArgumentException("Column name cannot be empty");
        }

        DataType dataTypeEnum;
        try {
            dataTypeEnum = DataType.valueOf(dataTypeStr != null ? dataTypeStr.toUpperCase() : "VARCHAR");
        } catch (IllegalArgumentException e) {
            dataTypeEnum = DataType.VARCHAR;
        }

        Column col = new Column(colName, dataTypeEnum);
        table.addColumn(col);
        tableRepository.saveTables();

        return new ColumnDTO(col);
    }

    /**
     * Observer Pattern: Removes Column from Table, triggers COLUMN_REMOVED event, and persists to table.json.
     */
    public void removeColumn(String dbName, String schemaName, String tableName, String columnName) {
        Table table = tableRepository.findTable(dbName, schemaName, tableName);
        if (table != null) {
            table.removeColumn(columnName);
            tableRepository.saveTables();
        }
    }
}

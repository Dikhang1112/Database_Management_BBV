package services;

import dto.ColumnDTO;
import entity.metadata.domain.Column;
import entity.metadata.enums.DataType;
import org.springframework.stereotype.Service;
import repositories.ColumnRepository;

/**
 * Service handling business logic for Column operations (Rename, Change Data Type).
 * Managed by Spring Dependency Injection (@Service).
 */
@Service
public class ColumnService {

    private final ColumnRepository columnRepository;

    public ColumnService(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
    }

    public Column findColumn(String dbName, String schemaName, String tableName, String columnName) {
        return columnRepository.findColumn(dbName, schemaName, tableName, columnName);
    }

    /**
     * Renames a Column in the specified Table and persists changes to column.json.
     */
    public ColumnDTO renameColumn(String dbName, String schemaName, String tableName, String columnName, String newName) {
        Column col = columnRepository.findColumn(dbName, schemaName, tableName, columnName);
        if (col == null) {
            throw new IllegalArgumentException("Column '" + columnName + "' does not exist");
        }
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("New column name cannot be empty");
        }

        col.rename(newName);
        columnRepository.saveColumns();

        return new ColumnDTO(col);
    }

    /**
     * Changes the Data Type of a Column and persists changes to column.json.
     */
    public ColumnDTO changeDataType(String dbName, String schemaName, String tableName, String columnName, DataType newType) {
        Column col = columnRepository.findColumn(dbName, schemaName, tableName, columnName);
        if (col == null) {
            throw new IllegalArgumentException("Column '" + columnName + "' does not exist");
        }
        if (newType == null) {
            throw new IllegalArgumentException("New DataType cannot be null");
        }

        col.changeDataType(newType);
        columnRepository.saveColumns();

        return new ColumnDTO(col);
    }
}

package controllers.metadata;

import dto.ApiResponse;
import dto.ColumnDTO;
import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Column;
import entity.metadata.domain.Table;
import entity.metadata.domain.TableMemento;
import entity.metadata.enums.DataType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/metadata/tables")
@Tag(name = "5. Table Management", description = "REST APIs for managing Tables, Snapshot Memento, and Event Listeners")
public class TableController {

    private Table findTable(String dbName, String schemaName, String tableName) {
        if (!CatalogManager.getInstance().containsDatabase(dbName)) return null;
        var db = CatalogManager.getInstance().getDatabase(dbName);
        if (db == null || !db.containsSchema(schemaName)) return null;
        var schema = db.getSchema(schemaName);
        return schema != null ? schema.getTable(tableName) : null;
    }

    /** Memento Pattern: Create Snapshot */
    @PostMapping("/{dbName}/{schemaName}/{tableName}/memento/snapshot")
    @Operation(summary = "Create Memento Snapshot of Table (Memento Pattern)", description = "Creates a Memento snapshot object saving current Column states (Returns HTTP 201 Created)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Snapshot created successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 201,\n  \"message\": \"Memento snapshot created successfully\",\n  \"data\": {\n    \"savedColumns\": [\n      { \"columnName\": \"id\", \"dataType\": \"INT\", \"isNullable\": false, \"defaultValue\": null },\n      { \"columnName\": \"username\", \"dataType\": \"VARCHAR\", \"isNullable\": false, \"defaultValue\": null }\n    ]\n  },\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<TableMemento>> createMemento(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName
    ) {
        Table table = findTable(dbName, schemaName, tableName);
        if (table == null) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Table does not exist"));
        return ResponseEntity.status(201).body(dto.ApiResponse.success("Memento snapshot created successfully", table.createMemento()));
    }

    /** Memento Pattern: Restore Snapshot */
    @PostMapping("/{dbName}/{schemaName}/{tableName}/memento/restore")
    @Operation(summary = "Restore Table state from Memento (Memento Pattern)", description = "Restores Column state from a TableMemento object (Returns HTTP 201 Created)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Restored successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 201,\n  \"message\": \"Table state restored from Memento successfully\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<Void>> restoreMemento(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName,
        @RequestBody TableMemento memento
    ) {
        Table table = findTable(dbName, schemaName, tableName);
        if (table == null) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Table does not exist"));
        table.restore(memento);
        return ResponseEntity.status(201).body(dto.ApiResponse.success("Table state restored from Memento successfully", null));
    }

    /** Observer Pattern: Add Column */
    @PostMapping("/{dbName}/{schemaName}/{tableName}/columns")
    @Operation(summary = "Add a new Column to Table (Observer Pattern: Publishes event)", description = "Adds a Column and publishes COLUMN_ADDED event to registered Listeners (Returns HTTP 201 Created)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Column added successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 201,\n  \"message\": \"Column added successfully\",\n  \"data\": {\n    \"columnName\": \"email\",\n    \"dataType\": \"VARCHAR\",\n    \"isNullable\": true,\n    \"defaultValue\": null\n  },\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<ColumnDTO>> addColumn(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName,
        @RequestBody Map<String, Object> request
    ) {
        Table table = findTable(dbName, schemaName, tableName);
        if (table == null) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Table not found"));
        String colName = (String) request.get("columnName");
        String dataTypeStr = (String) request.getOrDefault("dataType", "VARCHAR");
        Column col = new Column(colName, DataType.valueOf(dataTypeStr.toUpperCase()));
        table.addColumn(col);
        return ResponseEntity.status(201).body(dto.ApiResponse.success("Column added successfully", new ColumnDTO(col)));
    }

    /** Observer Pattern: Remove Column */
    @DeleteMapping("/{dbName}/{schemaName}/{tableName}/columns/{columnName}")
    @Operation(summary = "Remove Column from Table (Observer Pattern: Publishes event)", description = "Removes Column and publishes COLUMN_REMOVED event to registered Listeners")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Column removed successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"Column 'email' removed successfully\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<Void>> removeColumn(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName, @PathVariable String columnName
    ) {
        Table table = findTable(dbName, schemaName, tableName);
        if (table != null) table.removeColumn(columnName);
        return ResponseEntity.ok(dto.ApiResponse.success("Column '" + columnName + "' removed successfully", null));
    }
}

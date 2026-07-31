package controllers.metadata;

import dto.ApiResponse;
import dto.ColumnDTO;
import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Column;
import entity.metadata.domain.Table;
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
@RequestMapping("/api/v1/metadata/columns")
@Tag(name = "6. Column Management", description = "REST APIs for managing Column attributes (Rename, Data Type)")
public class ColumnController {

    private Column findColumn(String dbName, String schemaName, String tableName, String columnName) {
        if (!CatalogManager.getInstance().containsDatabase(dbName)) return null;
        var db = CatalogManager.getInstance().getDatabase(dbName);
        if (db == null || !db.containsSchema(schemaName)) return null;
        var schema = db.getSchema(schemaName);
        if (schema == null || !schema.containsTable(tableName)) return null;
        Table table = schema.getTable(tableName);
        return table != null ? table.getColumn(columnName) : null;
    }

    @PutMapping("/{dbName}/{schemaName}/{tableName}/{columnName}/rename")
    @Operation(summary = "Rename a Column (rename)", description = "Changes the name of a Column in the specified Table")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Renamed successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"Column renamed successfully\",\n  \"data\": {\n    \"columnName\": \"new_username\",\n    \"dataType\": \"VARCHAR\",\n    \"isNullable\": false,\n    \"defaultValue\": null\n  },\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<ColumnDTO>> renameColumn(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName, @PathVariable String columnName,
        @RequestBody Map<String, String> request
    ) {
        Column col = findColumn(dbName, schemaName, tableName, columnName);
        if (col == null) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Column does not exist"));
        String newName = request.get("newName");
        col.rename(newName);
        return ResponseEntity.ok(dto.ApiResponse.success("Column renamed successfully", new ColumnDTO(col)));
    }

    @PutMapping("/{dbName}/{schemaName}/{tableName}/{columnName}/data-type")
    @Operation(summary = "Change Column Data Type (changeDataType)", description = "Converts the data type of the specified Column")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Data type changed successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"Column data type changed successfully\",\n  \"data\": {\n    \"columnName\": \"username\",\n    \"dataType\": \"TEXT\",\n    \"isNullable\": false,\n    \"defaultValue\": null\n  },\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<ColumnDTO>> changeDataType(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName, @PathVariable String columnName,
        @RequestBody Map<String, String> request
    ) {
        Column col = findColumn(dbName, schemaName, tableName, columnName);
        if (col == null) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Column does not exist"));
        String newTypeStr = request.get("newType");
        DataType newType = DataType.valueOf(newTypeStr.toUpperCase());
        col.changeDataType(newType);
        return ResponseEntity.ok(dto.ApiResponse.success("Column data type changed successfully", new ColumnDTO(col)));
    }
}

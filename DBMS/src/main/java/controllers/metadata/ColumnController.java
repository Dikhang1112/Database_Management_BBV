package controllers.metadata;

import dto.ApiResponse;
import dto.ColumnDTO;
import entity.metadata.domain.Column;
import entity.metadata.enums.DataType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ColumnService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/metadata/columns")
@Tag(name = "6. Column Management", description = "REST APIs for managing Column attributes (Rename, Data Type)")
public class ColumnController {

    private final ColumnService columnService;

    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
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
    public ResponseEntity<ApiResponse<ColumnDTO>> renameColumn(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName, @PathVariable String columnName,
        @RequestBody Map<String, String> request
    ) {
        Column col = columnService.findColumn(dbName, schemaName, tableName, columnName);
        if (col == null) return ResponseEntity.status(404).body(ApiResponse.error(404, "Column does not exist"));
        String newName = request.get("newName");
        ColumnDTO columnDTO = columnService.renameColumn(dbName, schemaName, tableName, columnName, newName);
        return ResponseEntity.ok(ApiResponse.success("Column renamed successfully", columnDTO));
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
    public ResponseEntity<ApiResponse<ColumnDTO>> changeDataType(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName, @PathVariable String columnName,
        @RequestBody Map<String, String> request
    ) {
        Column col = columnService.findColumn(dbName, schemaName, tableName, columnName);
        if (col == null) return ResponseEntity.status(404).body(ApiResponse.error(404, "Column does not exist"));
        String newTypeStr = request.get("newType");
        DataType newType = DataType.valueOf(newTypeStr.toUpperCase());
        ColumnDTO columnDTO = columnService.changeDataType(dbName, schemaName, tableName, columnName, newType);
        return ResponseEntity.ok(ApiResponse.success("Column data type changed successfully", columnDTO));
    }
}

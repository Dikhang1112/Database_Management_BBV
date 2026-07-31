package controllers.metadata;

import dto.ApiResponse;
import dto.TableDTO;
import entity.metadata.domain.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.SchemaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/metadata/schemas")
@Tag(name = "4. Schema Management", description = "REST APIs for managing Tables and Schema Read-Only properties")
public class SchemaController {

    private final SchemaService schemaService;

    public SchemaController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @PostMapping("/{dbName}/{schemaName}/tables")
    @Operation(summary = "Create a new Table in Schema", description = "Creates a new Table in the specified Schema (Returns HTTP 201 Created)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Created successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 201,\n  \"message\": \"Table 'users' created successfully\",\n  \"data\": {\n    \"tableName\": \"users\",\n    \"locked\": false,\n    \"columnCount\": 0,\n    \"columns\": []\n  },\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Table exists",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 400,\n  \"message\": \"Bad Request (400): Table 'users' already exists\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "405",
            description = "Schema read-only",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 405,\n  \"message\": \"Method Not Allowed (405): Schema is READ-ONLY\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<TableDTO>> createTable(
        @PathVariable String dbName,
        @PathVariable String schemaName,
        @RequestBody Map<String, String> request
    ) {
        Schema schema = schemaService.findSchema(dbName, schemaName);
        if (schema == null) return ResponseEntity.status(404).body(ApiResponse.error(404, "Schema '" + schemaName + "' does not exist"));

        String tableName = request.get("tableName");
        TableDTO tableDTO = schemaService.createTable(dbName, schemaName, tableName);
        return ResponseEntity.status(201).body(ApiResponse.success("Table '" + tableName + "' created successfully", tableDTO));
    }

    @DeleteMapping("/{dbName}/{schemaName}/tables/{tableName}")
    @Operation(summary = "Drop a Table from Schema", description = "Removes a Table from the specified Schema (Returns HTTP 204 No Content)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "204",
            description = "Deleted successfully (204 No Content)"
        )
    })
    public ResponseEntity<Void> dropTable(
        @PathVariable String dbName,
        @PathVariable String schemaName,
        @PathVariable String tableName
    ) {
        schemaService.dropTable(dbName, schemaName, tableName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{dbName}/{schemaName}/tables")
    @Operation(summary = "List all Tables in Schema", description = "Returns a list of Table objects in the specified Schema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"Tables retrieved successfully\",\n  \"data\": [\n    { \"tableName\": \"users\", \"locked\": false, \"columnCount\": 3, \"columns\": [] },\n    { \"tableName\": \"orders\", \"locked\": false, \"columnCount\": 3, \"columns\": [] }\n  ],\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<List<TableDTO>>> listTables(
        @PathVariable String dbName,
        @PathVariable String schemaName
    ) {
        Schema schema = schemaService.findSchema(dbName, schemaName);
        if (schema == null) return ResponseEntity.status(404).body(ApiResponse.error(404, "Schema does not exist"));
        List<TableDTO> dtos = schemaService.listTables(dbName, schemaName);
        return ResponseEntity.ok(ApiResponse.success("Tables retrieved successfully", dtos));
    }
}

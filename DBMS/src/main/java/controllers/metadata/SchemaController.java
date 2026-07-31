package controllers.metadata;

import dto.ApiResponse;
import dto.TableDTO;
import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Database;
import entity.metadata.domain.Schema;
import entity.metadata.domain.Table;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/metadata/schemas")
@Tag(name = "4. Schema Management", description = "REST APIs for managing Tables and Schema Read-Only properties")
public class SchemaController {

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
    public ResponseEntity<dto.ApiResponse<TableDTO>> createTable(
        @PathVariable String dbName,
        @PathVariable String schemaName,
        @RequestBody Map<String, String> request
    ) {
        Database db = CatalogManager.getInstance().getDatabase(dbName);
        if (db == null) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Database '" + dbName + "' does not exist"));
        Schema schema = db.getSchema(schemaName);
        if (schema == null) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Schema '" + schemaName + "' does not exist"));

        String tableName = request.get("tableName");
        Table table = schema.createTable(tableName);
        return ResponseEntity.status(201).body(dto.ApiResponse.success("Table '" + tableName + "' created successfully", new TableDTO(table)));
    }

    @DeleteMapping("/{dbName}/{schemaName}/tables/{tableName}")
    @Operation(summary = "Drop a Table from Schema", description = "Removes a Table from the specified Schema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Deleted successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"Table 'users' dropped successfully\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<Void>> dropTable(
        @PathVariable String dbName,
        @PathVariable String schemaName,
        @PathVariable String tableName
    ) {
        Database db = CatalogManager.getInstance().getDatabase(dbName);
        if (db != null && db.containsSchema(schemaName)) {
            db.getSchema(schemaName).dropTable(tableName);
        }
        return ResponseEntity.ok(dto.ApiResponse.success("Table '" + tableName + "' dropped successfully", null));
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
    public ResponseEntity<dto.ApiResponse<List<TableDTO>>> listTables(
        @PathVariable String dbName,
        @PathVariable String schemaName
    ) {
        Database db = CatalogManager.getInstance().getDatabase(dbName);
        if (db == null || !db.containsSchema(schemaName)) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Schema does not exist"));
        List<TableDTO> dtos = db.getSchema(schemaName).listTables().stream().map(TableDTO::new).toList();
        return ResponseEntity.ok(dto.ApiResponse.success("Tables retrieved successfully", dtos));
    }
}

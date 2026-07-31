package controllers.metadata;

import dto.ApiResponse;
import dto.DatabaseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.CatalogService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/metadata/catalog")
@Tag(name = "2. Catalog Management", description = "REST APIs for managing the root CatalogManager and Databases")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @PostMapping("/databases")
    @Operation(summary = "Create a new Database in Catalog", description = "Adds a new Database to CatalogManager (Returns HTTP 201 Created)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    value = "{\n  \"status\": 201,\n  \"message\": \"Database 'sales_db' created successfully\",\n  \"data\": {\n    \"databaseName\": \"sales_db\",\n    \"status\": \"ONLINE\",\n    \"schemaCount\": 0,\n    \"schemas\": []\n  },\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Database already exists / Invalid name",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 400,\n  \"message\": \"Bad Request (400): Database 'sales_db' already exists in Catalog\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<DatabaseDTO>> createDatabase(@RequestBody Map<String, String> request) {
        String databaseName = request.get("databaseName");
        DatabaseDTO databaseDTO = catalogService.createDatabase(databaseName);
        return ResponseEntity.status(201).body(ApiResponse.success("Database '" + databaseName + "' created successfully", databaseDTO));
    }

    @DeleteMapping("/databases/{databaseName}")
    @Operation(summary = "Drop a Database from Catalog", description = "Removes a Database from CatalogManager (Returns HTTP 204 No Content)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "204",
            description = "Deleted successfully (204 No Content)"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Database not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 404,\n  \"message\": \"Not Found (404): Database 'unknown_db' does not exist\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "405",
            description = "Method not allowed (Database not empty)",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 405,\n  \"message\": \"Method Not Allowed (405): Database is not empty\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<Void> dropDatabase(
        @Parameter(description = "Target Database Name") @PathVariable String databaseName
    ) {
        catalogService.dropDatabase(databaseName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/databases")
    @Operation(summary = "List all Databases in Catalog", description = "Returns all Database objects contained in CatalogManager")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"Databases retrieved successfully\",\n  \"data\": [\n    { \"databaseName\": \"sales_db\", \"status\": \"ONLINE\", \"schemaCount\": 2, \"schemas\": [\"public\", \"audit\"] },\n    { \"databaseName\": \"inventory_db\", \"status\": \"ONLINE\", \"schemaCount\": 1, \"schemas\": [\"public\"] }\n  ],\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<List<DatabaseDTO>>> listDatabases() {
        List<DatabaseDTO> dtos = catalogService.listDatabases();
        return ResponseEntity.ok(ApiResponse.success("Databases retrieved successfully", dtos));
    }
}

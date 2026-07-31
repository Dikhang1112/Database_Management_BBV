package controllers.metadata;

import dto.ApiResponse;
import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Database;
import entity.metadata.domain.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/metadata/databases")
@Tag(name = "3. Database Management", description = "REST APIs for managing Schemas and Database Status")
public class DatabaseController {

    @PostMapping("/{dbName}/schemas")
    @Operation(summary = "Create a new Schema in Database", description = "Adds a new Schema to the specified Database (Returns HTTP 201 Created)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Created successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 201,\n  \"message\": \"Schema 'public' created successfully\",\n  \"data\": \"public\",\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Schema exists / Invalid name",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 400,\n  \"message\": \"Bad Request (400): Schema 'public' already exists in Database\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "405",
            description = "Database offline",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 405,\n  \"message\": \"Method Not Allowed (405): Database is OFFLINE\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<String>> createSchema(
        @Parameter(description = "Database Name") @PathVariable String dbName,
        @RequestBody Map<String, String> request
    ) {
        Database db = CatalogManager.getInstance().getDatabase(dbName);
        if (db == null) {
            return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Database '" + dbName + "' does not exist"));
        }
        String schemaName = request.get("schemaName");
        Schema schema = db.createSchema(schemaName);
        return ResponseEntity.status(201).body(dto.ApiResponse.success("Schema '" + schemaName + "' created successfully", schema.getSchemaName()));
    }

    @DeleteMapping("/{dbName}/schemas/{schemaName}")
    @Operation(summary = "Drop a Schema from Database", description = "Removes a Schema from the specified Database")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Deleted successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"Schema 'public' dropped successfully\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Schema not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 404,\n  \"message\": \"Not Found (404): Schema does not exist\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<Void>> dropSchema(
        @PathVariable String dbName,
        @PathVariable String schemaName
    ) {
        Database db = CatalogManager.getInstance().getDatabase(dbName);
        if (db != null) {
            db.dropSchema(schemaName);
        }
        return ResponseEntity.ok(dto.ApiResponse.success("Schema '" + schemaName + "' dropped successfully", null));
    }

    @GetMapping("/{dbName}/schemas")
    @Operation(summary = "List all Schemas in Database", description = "Returns a list of all Schema names in the Database")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"Schemas retrieved successfully\",\n  \"data\": [\"public\", \"audit\"],\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<List<String>>> listSchemas(@PathVariable String dbName) {
        Database db = CatalogManager.getInstance().getDatabase(dbName);
        if (db == null) {
            return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Database '" + dbName + "' does not exist"));
        }
        List<String> list = db.listSchemas().stream().map(Schema::getSchemaName).toList();
        return ResponseEntity.ok(dto.ApiResponse.success("Schemas retrieved successfully", list));
    }
}

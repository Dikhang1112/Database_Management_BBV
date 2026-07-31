package controllers.metadata;

import dto.ApiResponse;
import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Index;
import entity.metadata.domain.Table;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/metadata/indexes")
@Tag(name = "8. Index Management (Strategy Pattern)", description = "REST APIs for Rebuilding Indexes using the Strategy Pattern")
public class IndexController {

    private Table findTable(String dbName, String schemaName, String tableName) {
        if (!CatalogManager.getInstance().containsDatabase(dbName)) return null;
        var db = CatalogManager.getInstance().getDatabase(dbName);
        if (db == null || !db.containsSchema(schemaName)) return null;
        var schema = db.getSchema(schemaName);
        return schema != null ? schema.getTable(tableName) : null;
    }

    /** Strategy Pattern: Rebuild Index (POST -> 201 Created) */
    @PostMapping("/{dbName}/{schemaName}/{tableName}/{indexName}/rebuild")
    @Operation(summary = "Rebuild Index (Strategy Pattern: IndexRebuildStrategy)", description = "Executes the Index Rebuild algorithm using the configured Strategy Pattern (Returns HTTP 201 Created)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Index rebuilt successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 201,\n  \"message\": \"Index 'idx_users_username' rebuilt successfully using Strategy Pattern\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<Void>> rebuildIndex(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName, @PathVariable String indexName
    ) {
        Table table = findTable(dbName, schemaName, tableName);
        if (table == null) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Table does not exist"));
        Index index = table.getIndex(indexName);
        if (index != null) index.rebuild();
        return ResponseEntity.status(201).body(dto.ApiResponse.success("Index '" + indexName + "' rebuilt successfully using Strategy Pattern", null));
    }

    /** Disable Index */
    @PutMapping("/{dbName}/{schemaName}/{tableName}/{indexName}/disable")
    @Operation(summary = "Disable an Index", description = "Disables the enabled flag of an Index")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Index disabled successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"Index 'idx_users_username' disabled successfully\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<Void>> disableIndex(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName, @PathVariable String indexName
    ) {
        Table table = findTable(dbName, schemaName, tableName);
        if (table == null) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Table does not exist"));
        Index index = table.getIndex(indexName);
        if (index != null) index.disable();
        return ResponseEntity.ok(dto.ApiResponse.success("Index '" + indexName + "' disabled successfully", null));
    }
}

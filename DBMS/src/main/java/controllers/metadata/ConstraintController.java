package controllers.metadata;

import dto.ApiResponse;
import entity.metadata.abstracts.Constraint;
import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Table;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/metadata/constraints")
@Tag(name = "7. Constraint Management", description = "REST APIs for managing Data Constraints (Enable / Disable)")
public class ConstraintController {

    private Table findTable(String dbName, String schemaName, String tableName) {
        if (!CatalogManager.getInstance().containsDatabase(dbName)) return null;
        var db = CatalogManager.getInstance().getDatabase(dbName);
        if (db == null || !db.containsSchema(schemaName)) return null;
        var schema = db.getSchema(schemaName);
        return schema != null ? schema.getTable(tableName) : null;
    }

    /** Enable Constraint */
    @PutMapping("/{dbName}/{schemaName}/{tableName}/{constraintName}/enable")
    @Operation(summary = "Enable a Data Constraint", description = "Re-enables a disabled Constraint on the Table")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Enabled constraint successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"Constraint 'pk_users' enabled successfully\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<Void>> enableConstraint(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName, @PathVariable String constraintName
    ) {
        Table table = findTable(dbName, schemaName, tableName);
        if (table == null) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Table does not exist"));
        Constraint constraint = table.getConstraint(constraintName);
        if (constraint != null) constraint.enable();
        return ResponseEntity.ok(dto.ApiResponse.success("Constraint '" + constraintName + "' enabled successfully", null));
    }

    /** Disable Constraint */
    @PutMapping("/{dbName}/{schemaName}/{tableName}/{constraintName}/disable")
    @Operation(summary = "Disable a Data Constraint", description = "Disables the enabled flag of a Constraint")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Disabled constraint successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"Constraint 'pk_users' disabled successfully\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<Void>> disableConstraint(
        @PathVariable String dbName, @PathVariable String schemaName, @PathVariable String tableName, @PathVariable String constraintName
    ) {
        Table table = findTable(dbName, schemaName, tableName);
        if (table == null) return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Table does not exist"));
        Constraint constraint = table.getConstraint(constraintName);
        if (constraint != null) constraint.disable();
        return ResponseEntity.ok(dto.ApiResponse.success("Constraint '" + constraintName + "' disabled successfully", null));
    }
}

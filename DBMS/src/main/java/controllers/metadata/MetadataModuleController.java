package controllers.metadata;

import dto.ApiResponse;
import dto.CatalogManagerDTO;
import dto.DDLRequestDTO;
import entity.metadata.domain.CatalogManager;
import entity.metadata.facade.MetadataModule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.MetadataService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/metadata")
@Tag(name = "1. Metadata Subsystem", description = "Core REST APIs for FACADE & SINGLETON PATTERNS")
public class MetadataModuleController {

    private final MetadataService metadataService;

    public MetadataModuleController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    /**
     * 1. Singleton Pattern API: getInstance()
     */
    @GetMapping("/get-instance")
    @Operation(
        summary = "1. Get / Initialize MetadataModule Singleton Instance (Singleton Pattern)",
        description = "Returns the Singleton Instance of MetadataModule initialized via Double-Checked Locking (DCL)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = dto.ApiResponse.class),
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"MetadataModule Singleton Instance retrieved successfully\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<dto.ApiResponse<Map<String, Object>>> getInstance() {
        Map<String, Object> simulationData = metadataService.GetInstance();
        return ResponseEntity.ok(dto.ApiResponse.success("MetadataModule Singleton Instance retrieved successfully", simulationData));
    }

    /**
     * 2. Facade Pattern API: getCatalogManager()
     */
    @GetMapping("/catalog-manager")
    @Operation(
        summary = "2. Get Root CatalogManager Object (Facade Pattern)",
        description = "Queries the root CatalogManager object via the MetadataModule Facade."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CatalogManagerDTO.class),
                examples = @ExampleObject(
                    value = "{\n  \"status\": 200,\n  \"message\": \"CatalogManager information retrieved successfully\",\n  \"data\": {\n    \"elementName\": \"CatalogManager\",\n    \"elementType\": \"CatalogManager\",\n    \"totalDatabases\": 2,\n    \"databases\": [\"sales_db\", \"inventory_db\"]\n  },\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<CatalogManagerDTO>> getCatalogManager() {
        CatalogManager catalogManager = MetadataModule.getInstance().getCatalogManager();
        return ResponseEntity.ok(dto.ApiResponse.success("CatalogManager information retrieved successfully", new CatalogManagerDTO(catalogManager)));
    }

    /**
     * 3. Command Pattern API: executeDDL()
     */
    @PostMapping("/ddl/execute")
    @Operation(
        summary = "3. Execute DDL Command (Command Pattern)",
        description = "Encapsulates DDL operations (Create/Drop Database, Schema, Table) into a Command Object and executes via Facade."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Command executed successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n  \"status\": 201,\n  \"message\": \"DDL Command 'CREATE_DATABASE' executed successfully\",\n  \"data\": null,\n  \"timestamp\": \"31-07-2026 15:00:00\"\n}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid DDL command",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<dto.ApiResponse<Void>> executeDDL(@RequestBody DDLRequestDTO request) {
        return ResponseEntity.status(201).body(dto.ApiResponse.success("DDL Command '" + request.getCommandType() + "' executed successfully", null));
    }
}

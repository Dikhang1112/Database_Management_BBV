package controllers.metadata;

import dto.*;
import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Database;
import entity.metadata.domain.Table;
import entity.metadata.facade.MetadataModule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Metadata Subsystem", description = "Các REST API tương tác cấp cao thuộc MetadataModule Facade")
public class MetadataController {

    private final MetadataService metadataService;

    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    /**
     * 1. GET /api/v1/metadata/get-instance
     */
    @GetMapping("/get-instance")
    @Operation(
        summary = "1. Lấy / Khởi tạo MetadataModule Singleton Instance",
        description = "Trả về đối tượng Singleton Instance của MetadataModule được khởi tạo bằng kỹ thuật Double-Checked Locking (DCL)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = dto.ApiResponse.class),
                examples = @ExampleObject(
                    value = "{\n" +
                            "  \"status\": 200,\n" +
                            "  \"message\": \"Thành công (200 OK): Khởi tạo/Lấy MetadataModule Singleton Instance thành công\",\n" +
                            "  \"data\": null,\n" +
                            "  \"timestamp\": \"31-07-2026 14:00:00\"\n" +
                            "}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n" +
                            "  \"status\": 500,\n" +
                            "  \"message\": \"Ngoại lệ hệ thống khi khởi tạo Singleton Instance\",\n" +
                            "  \"data\": null,\n" +
                            "  \"timestamp\": \"31-07-2026 14:00:00\"\n" +
                            "}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<Map<String, Object>>> getInstance() {
        Map<String, Object> simulationData = metadataService.GetInstance();
        return ResponseEntity.ok(dto.ApiResponse.success("Thành công (200 OK): Khởi tạo/Lấy MetadataModule Singleton Instance thành công", simulationData));
    }

    /**
     * 2. GET /api/v1/metadata/catalog-manager
     */
    @GetMapping("/catalog-manager")
    @Operation(
        summary = "2. Lấy thông tin CatalogManager gốc",
        description = "Truy vấn thông tin tổng quan của CatalogManager bao gồm tổng số Database và danh sách tên các Database."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CatalogManagerDTO.class),
                examples = @ExampleObject(
                    value = "{\n" +
                            "  \"status\": 200,\n" +
                            "  \"message\": \"Thành công (200 OK): Lấy thông tin CatalogManager thành công\",\n" +
                            "  \"data\": {\n" +
                            "    \"elementName\": \"CatalogManager\",\n" +
                            "    \"elementType\": \"CatalogManager\",\n" +
                            "    \"totalDatabases\": 2,\n" +
                            "    \"databases\": [\"sales_db\", \"inventory_db\"]\n" +
                            "  },\n" +
                            "  \"timestamp\": \"31-07-2026 14:00:00\"\n" +
                            "}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<dto.ApiResponse<CatalogManagerDTO>> getCatalogManager() {
        CatalogManager catalogManager = MetadataModule.getInstance().getCatalogManager();
        CatalogManagerDTO dtoObj = new CatalogManagerDTO(catalogManager);
        return ResponseEntity.ok(dto.ApiResponse.success("Thành công (200 OK): Lấy thông tin CatalogManager thành công", dtoObj));
    }

    /**
     * 3. GET /api/v1/metadata/databases/{dbName}
     */
    @GetMapping("/databases/{dbName}")
    @Operation(
        summary = "3. Lấy thông tin chi tiết một Database",
        description = "Truy vấn đối tượng Database theo tên chỉ định, bao gồm trạng thái ONLINE/OFFLINE và danh sách Schema."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DatabaseDTO.class),
                examples = @ExampleObject(
                    value = "{\n" +
                            "  \"status\": 200,\n" +
                            "  \"message\": \"Thành công (200 OK): Lấy thông tin Database thành công\",\n" +
                            "  \"data\": {\n" +
                            "    \"databaseName\": \"sales_db\",\n" +
                            "    \"status\": \"ONLINE\",\n" +
                            "    \"schemaCount\": 2,\n" +
                            "    \"schemas\": [\"public\", \"audit\"]\n" +
                            "  },\n" +
                            "  \"timestamp\": \"31-07-2026 14:00:00\"\n" +
                            "}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Database not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n" +
                            "  \"status\": 404,\n" +
                            "  \"message\": \"Ngoại lệ (404 Not Found): Database 'unknown_db' không tồn tại\",\n" +
                            "  \"data\": null,\n" +
                            "  \"timestamp\": \"31-07-2026 14:00:00\"\n" +
                            "}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<DatabaseDTO>> getDatabase(
        @Parameter(description = "Tên Database cần tra cứu", example = "sales_db")
        @PathVariable String dbName
    ) {
        Database db = MetadataModule.getInstance().getDatabase(dbName);
        if (db == null) {
            return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Ngoại lệ (404 Not Found): Database '" + dbName + "' không tồn tại"));
        }
        return ResponseEntity.ok(dto.ApiResponse.success("Thành công (200 OK): Lấy thông tin Database thành công", new DatabaseDTO(db)));
    }

    /**
     * 4. GET /api/v1/metadata/tables/{dbName}/{schemaName}/{tableName}
     */
    @GetMapping("/tables/{dbName}/{schemaName}/{tableName}")
    @Operation(
        summary = "4. Lấy thông tin Table phân cấp theo DB, Schema và Table Name",
        description = "Truy vấn chi tiết đối tượng Table bao gồm danh sách các Cột (Columns) và kiểu dữ liệu."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TableDTO.class),
                examples = @ExampleObject(
                    value = "{\n" +
                            "  \"status\": 200,\n" +
                            "  \"message\": \"Thành công (200 OK): Lấy thông tin Bảng 'users' thành công\",\n" +
                            "  \"data\": {\n" +
                            "    \"tableName\": \"users\",\n" +
                            "    \"locked\": false,\n" +
                            "    \"columnCount\": 3,\n" +
                            "    \"columns\": [\n" +
                            "      { \"columnName\": \"id\", \"dataType\": \"INT\", \"isNullable\": false, \"defaultValue\": null },\n" +
                            "      { \"columnName\": \"username\", \"dataType\": \"VARCHAR\", \"isNullable\": false, \"defaultValue\": null },\n" +
                            "      { \"columnName\": \"email\", \"dataType\": \"VARCHAR\", \"isNullable\": true, \"defaultValue\": null }\n" +
                            "    ]\n" +
                            "  },\n" +
                            "  \"timestamp\": \"31-07-2026 14:00:00\"\n" +
                            "}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Table not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n" +
                            "  \"status\": 404,\n" +
                            "  \"message\": \"Ngoại lệ (404 Not Found): Không tìm thấy Bảng 'unknown_table' trong Schema 'public'\",\n" +
                            "  \"data\": null,\n" +
                            "  \"timestamp\": \"31-07-2026 14:00:00\"\n" +
                            "}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<TableDTO>> getTable(
        @Parameter(description = "Tên Database") @PathVariable String dbName,
        @Parameter(description = "Tên Schema") @PathVariable String schemaName,
        @Parameter(description = "Tên Bảng") @PathVariable String tableName
    ) {
        Table table = MetadataModule.getInstance().getTable(dbName, schemaName, tableName);
        if (table == null) {
            return ResponseEntity.status(404).body(dto.ApiResponse.error(404, "Ngoại lệ (404 Not Found): Không tìm thấy Bảng '" + tableName + "'"));
        }
        return ResponseEntity.ok(dto.ApiResponse.success("Thành công (200 OK): Lấy thông tin Bảng '" + tableName + "' thành công", new TableDTO(table)));
    }

    /**
     * 5. POST /api/v1/metadata/ddl/execute
     */
    @PostMapping("/ddl/execute")
    @Operation(
        summary = "5. Thực thi câu lệnh DDL Command",
        description = "Thực thi các lệnh DDL mô phỏng như tạo / xóa Database, Schema hoặc Table."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n" +
                            "  \"status\": 200,\n" +
                            "  \"message\": \"Thành công (200 OK): Thực thi lệnh DDL 'CREATE_DATABASE' thành công\",\n" +
                            "  \"data\": null,\n" +
                            "  \"timestamp\": \"31-07-2026 14:00:00\"\n" +
                            "}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid DDL command",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\n" +
                            "  \"status\": 400,\n" +
                            "  \"message\": \"Ngoại lệ (400 Bad Request): Lệnh DDL không hợp lệ hoặc thiếu tham số\",\n" +
                            "  \"data\": null,\n" +
                            "  \"timestamp\": \"31-07-2026 14:00:00\"\n" +
                            "}"
                )
            )
        )
    })
    public ResponseEntity<dto.ApiResponse<Void>> executeDDL(
        @RequestBody DDLRequestDTO request
    ) {
        return ResponseEntity.ok(dto.ApiResponse.success("Thành công (200 OK): Thực thi lệnh DDL '" + request.getCommandType() + "' thành công", null));
    }

    /**
     * 6. GET /api/v1/metadata/tables/check?tableName={tableName}
     */
    @GetMapping("/tables/check")
    @Operation(
        summary = "6. Kiểm tra sự tồn tại của Table trong Catalog Metadata",
        description = "Tra cứu sự tồn tại của Table trong tất cả Schema thuộc Metadata Catalog."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CheckExistDTO.class),
                examples = @ExampleObject(
                    value = "{\n" +
                            "  \"status\": 200,\n" +
                            "  \"message\": \"Thành công (200 OK): Kiểm tra tồn tại Bảng thành công\",\n" +
                            "  \"data\": {\n" +
                            "    \"targetType\": \"TABLE\",\n" +
                            "    \"tableName\": \"users\",\n" +
                            "    \"columnName\": null,\n" +
                            "    \"exists\": true\n" +
                            "  },\n" +
                            "  \"timestamp\": \"31-07-2026 14:00:00\"\n" +
                            "}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid table identifier",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<dto.ApiResponse<CheckExistDTO>> containsTable(
        @Parameter(description = "Tên Bảng cần kiểm tra", example = "users") @RequestParam String tableName
    ) {
        boolean exists = MetadataModule.getInstance().containsTable(tableName);
        CheckExistDTO dtoObj = new CheckExistDTO("TABLE", tableName, null, exists);
        return ResponseEntity.ok(dto.ApiResponse.success("Thành công (200 OK): Kiểm tra tồn tại Bảng thành công", dtoObj));
    }

    /**
     * 7. GET /api/v1/metadata/columns/check?tableName={tableName}&columnName={columnName}
     */
    @GetMapping("/columns/check")
    @Operation(
        summary = "7. Kiểm tra sự tồn tại của Cột thuộc Bảng chỉ định",
        description = "Tra cứu sự tồn tại của Cột thuộc Bảng chỉ định trong Catalog Metadata."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CheckExistDTO.class),
                examples = @ExampleObject(
                    name = "CheckColumnSuccess",
                    value = "{\n" +
                            "  \"status\": 200,\n" +
                            "  \"message\": \"Thành công (200 OK): Kiểm tra tồn tại Cột thành công\",\n" +
                            "  \"data\": {\n" +
                            "    \"targetType\": \"COLUMN\",\n" +
                            "    \"tableName\": \"users\",\n" +
                            "    \"columnName\": \"username\",\n" +
                            "    \"exists\": true\n" +
                            "  },\n" +
                            "  \"timestamp\": \"31-07-2026 14:00:00\"\n" +
                            "}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid identifier format",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<dto.ApiResponse<CheckExistDTO>> containsColumn(
        @Parameter(description = "Tên Bảng chứa cột", example = "users") @RequestParam String tableName,
        @Parameter(description = "Tên Cột cần kiểm tra", example = "username") @RequestParam String columnName
    ) {
        boolean exists = MetadataModule.getInstance().containsColumn(tableName, columnName);
        CheckExistDTO dtoObj = new CheckExistDTO("COLUMN", tableName, columnName, exists);
        return ResponseEntity.ok(dto.ApiResponse.success("Thành công (200 OK): Kiểm tra tồn tại Cột thành công", dtoObj));
    }
}

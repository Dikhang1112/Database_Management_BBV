package controllers.metadata;

import dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.MetadataService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/metadata")
@Tag(name = "1. Metadata Module", description = "Quản lý thông tin Catalog và Metadata Subsystem")
public class MetadataController {

    private final MetadataService metadataService;

    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping("/get-instance")
    @Operation(
        summary = "Mô phỏng gọi API getInstance() của MetadataModule",
        description = "Gọi trực tiếp phương thức MetadataModule.getInstance(). Trả về 200 OK khi khởi tạo thành công lần đầu. Nếu hệ thống đã tồn tại Instance, phương thức sẽ bắn ngoại lệ IllegalStateException và được GlobalExceptionHandler xử lý tự động quăng lỗi 400 Bad Request."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Thành công (200 OK) - Khởi tạo/Lấy MetadataModule Singleton Instance thành công",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Lỗi Yêu Cầu (400 Bad Request) - Instance đã tồn tại trong hệ thống, bắn ngoại lệ IllegalStateException",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> simulateGetInstanceApi() {
        // Tự động bắt IllegalStateException và chuyển thành 400 Bad Request qua @RestControllerAdvice
        Map<String, Object> simulationData = metadataService.GetInstance();
        return ResponseEntity.ok(ApiResponse.success("Thành công (200 OK): Khởi tạo MetadataModule Singleton Instance thành công", simulationData));
    }
}

package pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Enterprise POJO representing a Customer (Company / Tenant).
 * Relationship: Customer (1) -------- (*) User
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Customer Entity details")
public class Customer {

    @Schema(description = "Customer unique ID", example = "1")
    private Long id;

    @Schema(description = "Company Name", example = "Figma")
    private String companyName;

    @Schema(description = "Company Website", example = "https://figma.com")
    private String website;

    @Schema(description = "product", example = "Design Tools")
    private String product;

    @Schema(description = "Company Description", example = "Collaborative interface design platform.")
    private String description;

    @Schema(description = "Customer Status", example = "ACTIVE")
    private CustomerStatus status;

    @Schema(description = "Created timestamp (dd/MM/yyyy HH:mm:ss)", example = "15/01/2026 09:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Updated timestamp (dd/MM/yyyy HH:mm:ss)", example = "20/07/2026 15:30:00")
    private LocalDateTime updatedAt;
}

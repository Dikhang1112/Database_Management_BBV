package pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Enterprise POJO representing a User (Member belonging to a Customer Tenant).
 * Relationship: Customer (1) -------- (*) User
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User Entity details")
public class User {

    @Schema(description = "User unique ID", example = "1")
    private Long id;

    @Schema(description = "Associated Customer (Company) ID", example = "1")
    private Long customerId;

    @Schema(description = "First Name", example = "John")
    private String firstName;

    @Schema(description = "Last Name", example = "Smith")
    private String lastName;

    @Schema(description = "User Email", example = "john@figma.com")
    private String email;

    @Schema(description = "Phone Number", example = "+1-202-555-0101")
    private String phone;

    @Schema(description = "Avatar Image URL", example = "https://i.pravatar.cc/150?img=1")
    private String avatarUrl;

    @Schema(description = "User Role", example = "OWNER")
    private UserRole role;

    @Schema(description = "User Status", example = "ACTIVE")
    private UserStatus status;

    @Schema(description = "Is user active flag", example = "true")
    private Boolean active;

    @Schema(description = "Last login timestamp (dd/MM/yyyy HH:mm:ss)", example = "02/08/2026 08:10:00")
    private LocalDateTime lastLoginAt;

    @Schema(description = "Created timestamp (dd/MM/yyyy HH:mm:ss)", example = "15/01/2026 09:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Updated timestamp (dd/MM/yyyy HH:mm:ss)", example = "20/07/2026 15:30:00")
    private LocalDateTime updatedAt;
}

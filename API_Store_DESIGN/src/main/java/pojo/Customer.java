package pojo;

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
public class Customer {

    private Long id;
    private String companyName;
    private String website;
    private String industry;
    private String description;
    private CustomerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

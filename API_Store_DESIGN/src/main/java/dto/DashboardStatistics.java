package dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Overall Dashboard Statistics Response Payload")
public class DashboardStatistics {

    @Schema(description = "Total number of customers (companies/tenants)", example = "10")
    private long totalCustomers;

    @Schema(description = "Total number of users registered in the system", example = "10")
    private long totalUsers;

    @Schema(description = "Total number of active users", example = "7")
    private long totalActiveUsers;
}

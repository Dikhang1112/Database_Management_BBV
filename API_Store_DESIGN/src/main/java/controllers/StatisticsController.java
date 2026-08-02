package controllers;

import dto.DashboardStatistics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.StatisticsService;

@RestController
@RequestMapping("/api/v1/dashboard/statistics")
@RequiredArgsConstructor
@Tag(name = "Dashboard Statistics", description = "APIs for overall system dashboard statistics")
@SecurityRequirement(name = "Bearer Authentication")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "Get overall system dashboard statistics", description = "Retrieve total customers, total users, and total active users count")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved overall dashboard statistics",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DashboardStatistics.class),
                            examples = @ExampleObject(
                                    name = "DashboardStatisticsExample",
                                    summary = "Sample overall statistics response",
                                    value = """
                                            {
                                              "totalCustomers": 10,
                                              "totalUsers": 10,
                                              "totalActiveUsers": 7
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<DashboardStatistics> getOverallStatistics() {
        return ResponseEntity.ok(statisticsService.getOverallStatistics());
    }
}

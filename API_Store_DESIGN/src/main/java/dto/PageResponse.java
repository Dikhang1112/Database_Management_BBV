package dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Generic Paginated Response Payload")
public class PageResponse<T> {

    @Schema(description = "List of items on current page")
    private List<T> data;

    @Schema(description = "Current page number (1-based index)", example = "1")
    private int page;

    @Schema(description = "Page size (number of elements per page)", example = "5")
    private int size;

    @Schema(description = "Total elements across all pages", example = "10")
    private long totalElements;

    @Schema(description = "Total number of pages", example = "2")
    private int totalPages;

    @Schema(description = "Is this the last page?", example = "false")
    private boolean last;
}

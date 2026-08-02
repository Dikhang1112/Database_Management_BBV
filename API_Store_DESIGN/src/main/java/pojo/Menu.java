package pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Enterprise POJO representing a Menu navigation item.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Menu navigation item details")
public class Menu {

    /**
     * Primary identifier of menu item.
     */
    @Schema(description = "Menu unique ID", example = "1")
    private Long id;

    /**
     * Parent menu ID (null for root menus).
     */
    @Schema(description = "Parent menu ID (null for root menus)", example = "null")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Long parentId;

    /**
     * Display title of menu item.
     */
    @Schema(description = "Menu title", example = "Store")
    private String title;

    /**
     * Icon identifier or class name.
     */
    @Schema(description = "Icon identifier", example = "store")
    private String icon;

    /**
     * Navigation route path.
     */
    @Schema(description = "Route path", example = "/store")
    private String path;

    /**
     * Sorting order index.
     */
    @Schema(description = "Display order index", example = "1")
    private Integer displayOrder;

    /**
     * Menu type (MENU, SUB_MENU).
     */
    @Schema(description = "Menu type", example = "MENU")
    private MenuType type;

    /**
     * Menu status (OPENED, CLOSED).
     */
    @Schema(description = "Menu status", example = "OPENED")
    private MenuStatus status;

    /**
     * Nested child sub-menus (populated dynamically in MenuService).
     */
    @Schema(description = "List of child sub-menus")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children;
}

package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import pojo.Menu;
import services.MenuService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
@Tag(name = "Menu Management", description = "APIs for retrieving hierarchical navigation menu tree")
@SecurityRequirement(name = "Bearer Authentication")
public class MenuController {

    private final MenuService menuService;

    // =====================================================
    // GET MENU TREE
    // =====================================================

    @Operation(summary = "Get hierarchical menu tree", description = "Retrieve full nested navigation menu tree sorted by display order")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved menu tree",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Menu.class)),
                            examples = @ExampleObject(
                                    name = "MenuTreeExample",
                                    summary = "Sample hierarchical menu tree response",
                                    value = """
                                            {
                                              "id": 2,
                                              "parentId": null,
                                              "title": "Store",
                                              "icon": "store",
                                              "path": "/store",
                                              "displayOrder": 2,
                                              "type": "MENU",
                                              "status": "OPENED",
                                              "children": [
                                                {
                                                  "id": 3,
                                                  "parentId": 2,
                                                  "title": "Products",
                                                  "icon": "shopping-bag",
                                                  "path": "/store/products",
                                                  "displayOrder": 1,
                                                  "type": "SUB_MENU",
                                                  "status": "OPENED"
                                                },
                                                {
                                                  "id": 4,
                                                  "parentId": 2,
                                                  "title": "Orders",
                                                  "icon": "shopping-cart",
                                                  "path": "/store/orders",
                                                  "displayOrder": 2,
                                                  "type": "SUB_MENU",
                                                  "status": "OPENED"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Menu>> getMenuTree() {
        return ResponseEntity.ok(menuService.getMenuTree());
    }
}

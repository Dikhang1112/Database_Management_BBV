package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pojo.User;
import services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "CRUD APIs for managing users")
public class UserController {

    private final UserService userService;

    // =====================================================
    // GET ALL USERS
    // =====================================================

    @Operation(summary = "Get all users", description = "Retrieve all users from mock JSON storage")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved users list",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class)),
                            examples = @ExampleObject(
                                    name = "UsersListExample",
                                    summary = "Sample users array from users.json",
                                    value = """
                                            [
                                              {
                                                "id": 1,
                                                "customerId": 1,
                                                "firstName": "John",
                                                "lastName": "Smith",
                                                "email": "john@figma.com",
                                                "phone": "+1-202-555-0101",
                                                "avatarUrl": "https://i.pravatar.cc/150?img=1",
                                                "role": "OWNER",
                                                "status": "ACTIVE",
                                                "lastLoginAt": "02/08/2026 08:10:00",
                                                "createdAt": "15/01/2026 09:00:00",
                                                "updatedAt": "20/07/2026 15:30:00"
                                              },
                                              {
                                                "id": 2,
                                                "customerId": 1,
                                                "firstName": "Emma",
                                                "lastName": "Brown",
                                                "email": "emma@figma.com",
                                                "phone": "+1-202-555-0102",
                                                "avatarUrl": "https://i.pravatar.cc/150?img=2",
                                                "role": "ADMIN",
                                                "status": "ACTIVE",
                                                "lastLoginAt": "02/08/2026 08:12:00",
                                                "createdAt": "16/01/2026 10:00:00",
                                                "updatedAt": "20/07/2026 15:30:00"
                                              }
                                            ]
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    // =====================================================
    // GET USER BY ID
    // =====================================================

    @Operation(summary = "Get user by id", description = "Retrieve a single user using user id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "SingleUserExample",
                                    summary = "Sample user data",
                                    value = """
                                            {
                                              "id": 1,
                                              "customerId": 1,
                                              "firstName": "John",
                                              "lastName": "Smith",
                                              "email": "john@figma.com",
                                              "phone": "+1-202-555-0101",
                                              "avatarUrl": "https://i.pravatar.cc/150?img=1",
                                              "role": "OWNER",
                                              "status": "ACTIVE",
                                              "lastLoginAt": "02/08/2026 08:10:00",
                                              "createdAt": "15/01/2026 09:00:00",
                                              "updatedAt": "20/07/2026 15:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@Parameter(description = "User ID") @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // =====================================================
    // GET USER BY EMAIL
    // =====================================================

    @Operation(summary = "Get user by email", description = "Search user by email address")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found by email",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "UserByEmailExample",
                                    summary = "Sample user data by email",
                                    value = """
                                            {
                                              "id": 1,
                                              "customerId": 1,
                                              "firstName": "John",
                                              "lastName": "Smith",
                                              "email": "john@figma.com",
                                              "phone": "+1-202-555-0101",
                                              "avatarUrl": "https://i.pravatar.cc/150?img=1",
                                              "role": "OWNER",
                                              "status": "ACTIVE",
                                              "lastLoginAt": "02/08/2026 08:10:00",
                                              "createdAt": "15/01/2026 09:00:00",
                                              "updatedAt": "20/07/2026 15:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<User> findByEmail(@Parameter(description = "User Email") @PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    // =====================================================
    // CREATE USER
    // =====================================================

    @Operation(summary = "Create new user", description = "Create a new user in mock storage")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "CreatedUserResponse",
                                    summary = "Sample created user response",
                                    value = """
                                            {
                                              "id": 1,
                                              "customerId": 1,
                                              "firstName": "John",
                                              "lastName": "Smith",
                                              "email": "john@figma.com",
                                              "phone": "+1-202-555-0101",
                                              "avatarUrl": "https://i.pravatar.cc/150?img=1",
                                              "role": "OWNER",
                                              "status": "ACTIVE",
                                              "lastLoginAt": "02/08/2026 08:10:00",
                                              "createdAt": "15/01/2026 09:00:00",
                                              "updatedAt": "20/07/2026 15:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<User> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User object to create", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(name = "CreateUserRequestBodyExample", summary = "Sample User Request Body", value = """
                                    {
                                      "id": 1,
                                      "customerId": 1,
                                      "firstName": "John",
                                      "lastName": "Smith",
                                      "email": "john@figma.com",
                                      "phone": "+1-202-555-0101",
                                      "avatarUrl": "https://i.pravatar.cc/150?img=1",
                                      "role": "OWNER",
                                      "status": "ACTIVE",
                                      "lastLoginAt": "02/08/2026 08:10:00",
                                      "createdAt": "15/01/2026 09:00:00",
                                      "updatedAt": "20/07/2026 15:30:00"
                                    }
                                    """)))
            @RequestBody User user) {
        User createdUser = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // =====================================================
    // UPDATE USER
    // =====================================================

    @Operation(summary = "Update user", description = "Update existing user information")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    name = "UpdatedUserResponse",
                                    summary = "Sample updated user response",
                                    value = """
                                            {
                                              "id": 1,
                                              "customerId": 1,
                                              "firstName": "John",
                                              "lastName": "Smith",
                                              "email": "john@figma.com",
                                              "phone": "+1-202-555-0101",
                                              "avatarUrl": "https://i.pravatar.cc/150?img=1",
                                              "role": "OWNER",
                                              "status": "ACTIVE",
                                              "lastLoginAt": "02/08/2026 08:10:00",
                                              "createdAt": "15/01/2026 09:00:00",
                                              "updatedAt": "20/07/2026 15:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @Parameter(description = "User ID") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated user object data", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(name = "UpdateUserRequestBodyExample", summary = "Sample User Update Request Body", value = """
                                    {
                                      "id": 1,
                                      "customerId": 1,
                                      "firstName": "John",
                                      "lastName": "Smith",
                                      "email": "john@figma.com",
                                      "phone": "+1-202-555-0101",
                                      "avatarUrl": "https://i.pravatar.cc/150?img=1",
                                      "role": "OWNER",
                                      "status": "ACTIVE",
                                      "lastLoginAt": "02/08/2026 08:10:00",
                                      "createdAt": "15/01/2026 09:00:00",
                                      "updatedAt": "20/07/2026 15:30:00"
                                    }
                                    """)))
            @RequestBody User user) {
        return ResponseEntity.ok(userService.update(id, user));
    }

    // =====================================================
    // DELETE USER
    // =====================================================

    @Operation(summary = "Delete user", description = "Delete user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "User ID to delete") @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =====================================================
    // DELETE ALL USERS
    // =====================================================

    @Operation(summary = "Delete all users", description = "Remove all users from mock storage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All users deleted successfully")
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        userService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
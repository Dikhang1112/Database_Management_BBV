package controllers;

import configs.JwtTokenProvider;
import dto.AuthResponse;
import dto.LoginRequest;
import dto.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pojo.User;
import services.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for authentication, JWT tokens management, token refresh, and logout")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    // =====================================================
    // LOGIN API
    // =====================================================

    @Operation(summary = "User Login", description = "Authenticate using email and password to receive JWT Access Token and Refresh Token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful, returns JWT Access Token, Refresh Token, and User Profile",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    name = "LoginSuccessExample",
                                    summary = "Successful Login Response Example",
                                    value = """
                                            {
                                              "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGZpZ21hLmNvbSIsImlhdCI6MTcyMjYwMDAwMCwiZXhwIjoxNzIyNjg2NDAwfQ...",
                                              "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGZpZ21hLmNvbSIsImlhdCI6MTcyMjYwMDAwMCwiZXhwIjoxNzIzMjA0ODAwfQ...",
                                              "tokenType": "Bearer",
                                              "user": {
                                                "id": 1,
                                                "customerId": 1,
                                                "firstName": "John",
                                                "lastName": "Smith",
                                                "email": "john@figma.com",
                                                "phone": "+1-202-555-0101",
                                                "avatarUrl": "https://i.pravatar.cc/150?img=1",
                                                "role": "OWNER",
                                                "status": "ACTIVE",
                                                "active": true,
                                                "lastLoginAt": "02/08/2026 08:10:00",
                                                "createdAt": "15/01/2026 09:00:00",
                                                "updatedAt": "20/07/2026 15:30:00"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid email or password",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "LoginErrorExample",
                                    summary = "Invalid Credentials Error Response Example",
                                    value = """
                                            {
                                              "status": 401,
                                              "message": "Invalid email or password"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login credentials payload", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class), examples = @ExampleObject(name = "LoginRequestBodyExample", summary = "Sample Login Payload", value = """
            {
              "email": "john@figma.com",
              "password": "123456"
            }
            """))) @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenProvider.generateToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(loginRequest.getEmail());
            User user = userService.findByEmail(loginRequest.getEmail());

            AuthResponse response = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .user(user)
                    .build();

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "Invalid email or password"));
        }
    }

    // =====================================================
    // REFRESH TOKEN API
    // =====================================================

    @Operation(summary = "Refresh Access Token", description = "Provide a valid refresh token to issue a new JWT Access Token and Refresh Token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    name = "RefreshTokenSuccessExample",
                                    summary = "Token Refresh Response Example",
                                    value = """
                                            {
                                              "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGZpZ21hLmNvbSIsImlhdCI6MTcyMjYwMDAwMCwiZXhwIjoxNzIyNjg2NDAwfQ...",
                                              "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGZpZ21hLmNvbSIsImlhdCI6MTcyMjYwMDAwMCwiZXhwIjoxNzIzMjA0ODAwfQ...",
                                              "tokenType": "Bearer",
                                              "user": {
                                                "id": 1,
                                                "customerId": 1,
                                                "firstName": "John",
                                                "lastName": "Smith",
                                                "email": "john@figma.com",
                                                "phone": "+1-202-555-0101",
                                                "avatarUrl": "https://i.pravatar.cc/150?img=1",
                                                "role": "OWNER",
                                                "status": "ACTIVE",
                                                "active": true,
                                                "lastLoginAt": "02/08/2026 08:10:00",
                                                "createdAt": "15/01/2026 09:00:00",
                                                "updatedAt": "20/07/2026 15:30:00"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or expired refresh token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "RefreshTokenErrorExample",
                                    value = """
                                            {
                                              "status": 401,
                                              "message": "Invalid or expired refresh token"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Refresh token payload", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = RefreshTokenRequest.class), examples = @ExampleObject(name = "RefreshTokenRequestBodyExample", value = """
            {
              "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
            }
            """))) @RequestBody RefreshTokenRequest request) {
        String token = request.getRefreshToken();
        if (token == null || !tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "Invalid or expired refresh token"));
        }

        String email = tokenProvider.getEmailFromToken(token);
        String newAccessToken = tokenProvider.generateTokenFromEmail(email);
        String newRefreshToken = tokenProvider.generateRefreshToken(email);
        User user = userService.findByEmail(email);

        AuthResponse response = AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .user(user)
                .build();

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // LOGOUT API
    // =====================================================

    @Operation(summary = "User Logout", description = "Clear authentication session and log out")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Logout successful",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "LogoutSuccessExample",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Logged out successfully"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("status", 200, "message", "Logged out successfully"));
    }

    // =====================================================
    // VIEW PROFILE API (MOCK)
    // =====================================================

    @Operation(summary = "View current user profile", description = "Retrieve profile details of currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user profile",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ViewProfileExample",
                                    value = """
                                            {
                                              "id": 1,
                                              "firstName": "John",
                                              "lastName": "Smith",
                                              "email": "john@figma.com",
                                              "phone": "+1-202-555-0101",
                                              "role": "OWNER",
                                              "status": "ACTIVE"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/profile")
    public ResponseEntity<?> viewProfile() {
        return ResponseEntity.ok(Map.of(
                "id", 1,
                "firstName", "John",
                "lastName", "Smith",
                "email", "john@figma.com",
                "phone", "+1-202-555-0101",
                "role", "OWNER",
                "status", "ACTIVE"
        ));
    }

    // =====================================================
    // ACCOUNT SETTINGS / PROFILE UPDATE API (MOCK)
    // =====================================================

    @Operation(summary = "Update account settings / profile", description = "Update account settings or profile details for authenticated user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account settings updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "UpdateProfileExample",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Account settings updated successfully"
                                            }
                                            """
                            )
                    )
            )
    })
    @org.springframework.web.bind.annotation.PutMapping("/profile")
    public ResponseEntity<?> updateAccountSettings() {
        return ResponseEntity.ok(Map.of("status", 200, "message", "Account settings updated successfully"));
    }
}

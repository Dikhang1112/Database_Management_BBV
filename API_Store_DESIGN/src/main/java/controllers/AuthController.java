package controllers;

import configs.JwtTokenProvider;
import dto.AuthResponse;
import dto.LoginRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.User;
import services.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for authentication and JWT tokens management")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Operation(summary = "User Login", description = "Authenticate using email and password to receive a JWT Access Token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful, returns JWT Token and User Profile",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class),
                            examples = @ExampleObject(
                                    name = "LoginSuccessExample",
                                    summary = "Successful Login Response Example",
                                    value = """
                                            {
                                              "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGZpZ21hLmNvbSIsImlhdCI6MTcyMjYwMDAwMCwiZXhwIjoxNzIyNjg2NDAwfQ...",
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
            String jwt = tokenProvider.generateToken(authentication);
            User user = userService.findByEmail(loginRequest.getEmail());

            AuthResponse response = AuthResponse.builder()
                    .accessToken(jwt)
                    .tokenType("Bearer")
                    .user(user)
                    .build();

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "Invalid email or password"));
        }
    }
}

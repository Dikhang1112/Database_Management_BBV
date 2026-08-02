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
@Schema(description = "User Authentication Login Request Payload")
public class LoginRequest {

    @Schema(description = "User Email", example = "john@figma.com")
    private String email;

    @Schema(description = "User Password", example = "123456")
    private String password;
}

package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredential {
    private Long id;
    private Long userId;
    private String email;
    private String password;
    private boolean enabled;
    private LocalDateTime passwordUpdatedAt;
}
package services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pojo.UserCredential;
import repositories.UserCredentialRepository;

@Service
@RequiredArgsConstructor
public class UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;

    public UserCredential findByEmail(String email) {
        return userCredentialRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("UserCredential not found with email: " + email));
    }
}

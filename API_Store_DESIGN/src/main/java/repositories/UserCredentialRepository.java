package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import pojo.UserCredential;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserCredentialRepository {

    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    private final List<UserCredential> credentials = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public UserCredentialRepository() {
        objectMapper = new ObjectMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        objectMapper.registerModule(javaTimeModule);
    }

    @PostConstruct
    public void loadMockData() {
        try (InputStream inputStream = new ClassPathResource("data/user_credential.json").getInputStream()) {
            List<UserCredential> mockCredentials = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<UserCredential>>() {}
            );
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            for (UserCredential cred : mockCredentials) {
                if (cred.getPassword() == null || !encoder.matches("123456", cred.getPassword())) {
                    cred.setPassword(encoder.encode("123456"));
                }
            }
            credentials.clear();
            credentials.addAll(mockCredentials);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data/user_credential.json", e);
        }
    }

    public Optional<UserCredential> findByEmail(String email) {
        return credentials.stream()
                .filter(cred -> cred.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}

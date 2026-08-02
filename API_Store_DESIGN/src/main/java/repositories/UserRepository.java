package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import pojo.User;
import pojo.UserStatus;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    private final List<User> users = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public UserRepository() {
        objectMapper = new ObjectMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        objectMapper.registerModule(javaTimeModule);
    }

    @PostConstruct
    public void loadMockData() {

        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("data/users.json")) {

            if (inputStream == null) {
                throw new RuntimeException("Cannot find mock/users.json");
            }

            List<User> mockUsers = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<User>>() {
                    });

            users.clear();
            users.addAll(mockUsers);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load users.json", e);
        }
    }

    // =====================================================
    // Query
    // =====================================================

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<User> findByCustomerId(Long customerId) {
        return users.stream()
                .filter(user -> user.getCustomerId().equals(customerId))
                .toList();
    }


    public boolean existsById(Long id) {
        return users.stream()
                .anyMatch(user -> user.getId().equals(id));
    }

    public boolean existsByEmail(String email) {
        return users.stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    // =====================================================
    // Aggregate
    // =====================================================

    public long count() {
        return users.size();
    }

    public long countByStatus(UserStatus status) {
        return users.stream()
                .filter(user -> user.getStatus() == status)
                .count();
    }

    // =====================================================
    // Persistence
    // =====================================================

    public User save(User user) {
        users.add(user);
        return user;
    }

    public User update(User user) {

        Optional<User> optionalUser = findById(user.getId());

        if (optionalUser.isPresent()) {

            User existing = optionalUser.get();

            existing.setCustomerId(user.getCustomerId());
            existing.setFirstName(user.getFirstName());
            existing.setLastName(user.getLastName());
            existing.setEmail(user.getEmail());
            existing.setPhone(user.getPhone());
            existing.setAvatarUrl(user.getAvatarUrl());
            existing.setRole(user.getRole());
            existing.setStatus(user.getStatus());
            existing.setLastLoginAt(user.getLastLoginAt());
            existing.setUpdatedAt(user.getUpdatedAt());

            return existing;
        }

        throw new RuntimeException("User not found: " + user.getId());
    }

    public void deleteById(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }

    public void deleteAll() {
        users.clear();
    }
}

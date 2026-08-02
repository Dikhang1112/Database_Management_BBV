package services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pojo.User;
import repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // =====================================================
    // Query Operations
    // =====================================================

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // =====================================================
    // Create Operations
    // =====================================================

    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    // =====================================================
    // Update Operations
    // =====================================================

    public User update(Long id, User user) {
        User existingUser = findById(id);

        existingUser.setCustomerId(user.getCustomerId());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setAvatarUrl(user.getAvatarUrl());
        existingUser.setRole(user.getRole());
        existingUser.setStatus(user.getStatus());
        existingUser.setLastLoginAt(user.getLastLoginAt());
        existingUser.setUpdatedAt(user.getUpdatedAt());

        return userRepository.update(existingUser);
    }

    // =====================================================
    // Delete Operations
    // =====================================================

    public void delete(Long id) {
        User user = findById(id);
        userRepository.deleteById(user.getId());
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}

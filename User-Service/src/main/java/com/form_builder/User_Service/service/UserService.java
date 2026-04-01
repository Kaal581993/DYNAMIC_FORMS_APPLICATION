package com.form_builder.User_Service.service;


import com.form_builder.User_Service.dto.CreateUserRequest;
import com.form_builder.User_Service.dto.UserResponse;
import com.form_builder.User_Service.model.User;
import com.form_builder.User_Service.model.UserRole;
import com.form_builder.User_Service.repository.UserRepository;
import com.form_builder.User_Service.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(CreateUserRequest request) {

        // Encode password with BCrypt
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .userName(request.getUserName())
                .password(encodedPassword)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);

    }

    /**
     * Validate user credentials using BCrypt
     * @param email User's email
     * @param password Plain text password to validate
     * @return User if credentials are valid, null otherwise
     */
    public User validateCredentials(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return null;
        }

        // Use BCrypt to match the plain text password with the encoded password
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    /**
     * Authenticate user by email and password
     * @param email User's email
     * @param password Plain text password
     * @return true if authentication successful, false otherwise
     */
    public boolean authenticate(String email, String password) {
        User user = validateCredentials(email, password);
        return user != null;
    }

    /**
     * Get user roles by user ID
     * @param userId User's UUID
     * @return List of role names
     */
    public List<String> getUserRoles(UUID userId) {
        return userRoleRepository.findByUserId(userId)
                .stream()
                .map(userRole -> {
                    // Get role name from Role Service via Feign client
                    // For now, return a default role
                    return "USER";
                })
                .toList();
    }

    public UserResponse getUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User with UserId:"+userId+"is not available or inactive"));

        List<UUID> roles = userRoleRepository
                .findById(userId)
                .stream()
                .map(UserRole::getRoleId)
                .toList();

        return UserResponse.builder()
                .Id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roleId(roles)
                .build();

    }

    public void assignRole(UUID userId, UUID roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserRole userRole = UserRole.builder()
                .userId(userId)
                .roleId(roleId)
                .build();

        userRoleRepository.save(userRole);
    }
}

package com.form_builder.User_Service.service;


import com.form_builder.User_Service.client.RoleClient;
import com.form_builder.User_Service.dto.CreateUserRequest;
import com.form_builder.User_Service.dto.RoleDTO;
import com.form_builder.User_Service.dto.UserResponse;
import com.form_builder.User_Service.exception.DuplicateResourceException;
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
    private final RoleClient roleClient;

    public User createUser(CreateUserRequest request) {

        // Encode password with BCrypt
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        String email = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();
        if (email != null && userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }

        String userName = request.getUserName() == null ? null : request.getUserName().trim();
        if (userName != null && userRepository.findByUserName(userName).isPresent()) {
            throw new DuplicateResourceException("Username already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(email)
                .userName(userName)
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
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        List<String> roles = userRoles.stream()
                .map(userRole -> {
                    try {
                        // Get role name from Role Service via Feign client
                        RoleDTO role = roleClient.getRole(userRole.getRoleId());
                        return "ROLE_" + role.getName();
                    } catch (Exception e) {
                        // If Role Service is unavailable, return role ID as string
                        return "ROLE_" + userRole.getRoleId().toString();
                    }
                })
                .toList();

        // If no roles assigned, assign default roles based on email
        if (roles.isEmpty()) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null && user.getEmail() != null) {
                if (user.getEmail().toLowerCase().contains("admin")) {
                    roles = List.of("ROLE_ADMIN");
                } else {
                    roles = List.of("ROLE_USER");
                }
            }
        }

        return roles;
    }

    public UserResponse getUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User with UserId:"+userId+"is not available or inactive"));

        List<UUID> roles = userRoleRepository
                .findByUserId(userId)
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

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    List<UUID> roles = userRoleRepository.findByUserId(user.getId())
                            .stream()
                            .map(UserRole::getRoleId)
                            .toList();

                    return UserResponse.builder()
                            .Id(user.getId())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .userName(user.getUserName())
                            .email(user.getEmail())
                            .roleId(roles)
                            .active(user.getActive())
                            .createdAt(user.getCreatedAt())
                            .updatedAt(user.getUpdatedAt())
                            .build();
                })
                .toList();
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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}

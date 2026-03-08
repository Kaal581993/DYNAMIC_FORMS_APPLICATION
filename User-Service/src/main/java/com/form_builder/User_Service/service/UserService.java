package com.form_builder.User_Service.service;


import com.form_builder.User_Service.dto.CreateUserRequest;
import com.form_builder.User_Service.dto.UserResponse;
import com.form_builder.User_Service.model.User;
import com.form_builder.User_Service.model.UserRole;
import com.form_builder.User_Service.repository.UserRepository;
import com.form_builder.User_Service.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public User createUser(CreateUserRequest request) {

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);

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

    public void assignRole(UUID userId, Long roleId) {
        User user = User.builder().build();

    }
}

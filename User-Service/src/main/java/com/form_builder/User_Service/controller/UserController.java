package com.form_builder.User_Service.controller;

import com.form_builder.User_Service.dto.AssignRoleRequest;
import com.form_builder.User_Service.dto.CreateUserRequest;
import com.form_builder.User_Service.dto.LoginRequest;
import com.form_builder.User_Service.dto.LoginResponse;
import com.form_builder.User_Service.dto.UserResponse;
import com.form_builder.User_Service.model.User;
import com.form_builder.User_Service.security.JwtTokenProvider;
import com.form_builder.User_Service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping ("/createUser")
    public ResponseEntity<User> createUser(
            @RequestBody
            CreateUserRequest request){

        return ResponseEntity.ok(userService.createUser(request));
    }

    /**
     * Login endpoint - validates credentials using BCrypt and returns JWT token
     * @param request Login credentials (email and password)
     * @return LoginResponse with JWT token, user details and roles
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        User user = userService.validateCredentials(request.getEmail(), request.getPassword());

        if (user != null) {
            // Get user roles for JWT token
            List<String> roles = userService.getUserRoles(user.getId());
            
            // Generate JWT token
            String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), roles);

            return ResponseEntity.ok(LoginResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .token(token)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .roles(roles)
                    .build());
        }

        return ResponseEntity.ok(LoginResponse.builder()
                .success(false)
                .message("Invalid email or password")
                .build());
    }

    @PostMapping("/{userId}/roles")
    @Secured("ROLE_ADMIN")
    public  ResponseEntity <Void> assignRole(
            @PathVariable UUID userId,
            @RequestBody AssignRoleRequest request){

        userService.assignRole(userId, request.getRoleId());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable UUID userId){

        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}

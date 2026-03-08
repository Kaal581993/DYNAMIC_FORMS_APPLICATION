package com.form_builder.User_Service.controller;

import com.form_builder.User_Service.dto.AssignRoleRequest;
import com.form_builder.User_Service.dto.CreateUserRequest;
import com.form_builder.User_Service.dto.UserResponse;
import com.form_builder.User_Service.model.User;
import com.form_builder.User_Service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping ("/createUser")
    public ResponseEntity<User> createUser(
            @RequestBody
            CreateUserRequest request){

        return ResponseEntity.ok(userService.createUser(request));
    }

    @PostMapping("/{userId}/roles")
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
}

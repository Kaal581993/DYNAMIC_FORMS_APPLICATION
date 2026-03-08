package com.form_builder.Auth_Server.client;



import com.form_builder.Auth_Server.dto.CreateUserRequest;
import com.form_builder.Auth_Server.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/api/users")
    UserResponse createUser(@RequestBody CreateUserRequest request);

    @GetMapping("/api/users/email/{email}")
    UserResponse findByEmail(@PathVariable String email);

}

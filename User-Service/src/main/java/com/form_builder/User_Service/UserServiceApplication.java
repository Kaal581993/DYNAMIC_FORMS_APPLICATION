package com.form_builder.User_Service;

import com.form_builder.User_Service.client.RoleClient;
import com.form_builder.User_Service.dto.CreateUserRequest;
import com.form_builder.User_Service.dto.RoleDTO;
import com.form_builder.User_Service.model.User;
import com.form_builder.User_Service.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class UserServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	// Hardcoded master admin user initialization
	@Bean
	CommandLineRunner initAdminUser(UserService userService, RoleClient roleClient) {
		return args -> {
			try {
				// Check if admin user exists (assuming email is unique)
				if (userService.findByEmail("admin@kaal.com") == null) {
					CreateUserRequest adminRequest = CreateUserRequest.builder()
							.firstName("Master")
							.lastName("Admin")
							.userName("admin")
							.email("admin@kaal.com")
							.password("1stLUV@152991")
							.active(true)
							.build();

					User adminUser = userService.createUser(adminRequest);
					System.out.println("Master admin user created successfully with ID: " + adminUser.getId());

					// Try to assign ADMIN role
					try {
						RoleDTO adminRole = roleClient.getRoleByName("ADMIN");
						userService.assignRole(adminUser.getId(), adminRole.getId());
						System.out.println("ADMIN role assigned to master admin user successfully");
					} catch (Exception roleEx) {
						System.out.println("Warning: Could not assign ADMIN role to master admin user. Role service may not be available. Error: " + roleEx.getMessage());
						System.out.println("You can assign the role manually later using the API.");
					}
				} else {
					System.out.println("Master admin user already exists");
				}
			} catch (Exception e) {
				System.out.println("Failed to initialize admin user: " + e.getMessage());
			}
		};
	}

}

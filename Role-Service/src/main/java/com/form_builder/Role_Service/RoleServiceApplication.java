package com.form_builder.Role_Service;

import com.form_builder.Role_Service.model.Role;
import com.form_builder.Role_Service.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableWebSecurity
public class RoleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoleServiceApplication.class, args);
	}

	// Hardcoded master admin role initialization
	@Bean
	CommandLineRunner initAdminRole(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("ADMIN").isEmpty()) {
				Role adminRole = Role.builder()
						.name("ADMIN")
						.description("Master administrator role with full system access")
						.active(true)
						.createdAt(LocalDateTime.now())
						.build();
				roleRepository.save(adminRole);
				System.out.println("Master ADMIN role created successfully");
			}
		};
	}

}

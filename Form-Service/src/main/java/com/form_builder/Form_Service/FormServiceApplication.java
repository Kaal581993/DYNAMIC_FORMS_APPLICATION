package com.form_builder.Form_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import com.form_builder.Form_Service.config.FeignAuthConfig;

@SpringBootApplication
@EnableFeignClients(defaultConfiguration = FeignAuthConfig.class)
@EnableMongoRepositories(basePackages = "com.form_builder.Form_Service.repository")
@EnableWebSecurity
public class FormServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FormServiceApplication.class, args);
	}

}

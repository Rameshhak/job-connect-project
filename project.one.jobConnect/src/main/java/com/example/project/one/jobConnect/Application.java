package com.example.project.one.jobConnect;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@OpenAPIDefinition(
		info = @Info(
				title = "Spring MVC API URL's documentation",
				description = "Project JobConnect is a Spring Boot application that links job seekers with employers for job applications and management.",
				version = "v1.0",
				contact = @Contact(
						name = "James",
						email = "james.projects@gamil.com"
				)
		)
)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

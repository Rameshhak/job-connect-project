package com.example.project.one.jobConnect.document;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User id")
    private String id;
    @Schema(description = "User name")
    private String name;
    @Schema(description = "User email")
    private String email;
    @Schema(description = "User password")
    private String password;
    @Schema(description = "User location")
    private String location;

    @Schema(description = "User role")
    private String role;

}


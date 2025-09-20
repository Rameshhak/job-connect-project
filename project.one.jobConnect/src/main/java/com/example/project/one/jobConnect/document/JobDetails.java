package com.example.project.one.jobConnect.document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "jobDetails")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobDetails {

    @Id
    @Schema(description = "Job ID")
    private int id;
    @Schema(description = "Job title")
    private String title;
    @Schema(description = "Job description")
    private String description;
    @Schema(description = "Job location")
    private String location;
    @Schema(description = "Job salary")
    private String salary;
    @Schema(description = "Job deadline")
    private String deadline;

    @Schema(description = "Email of job posted employer")
    private String employerEmail;

}

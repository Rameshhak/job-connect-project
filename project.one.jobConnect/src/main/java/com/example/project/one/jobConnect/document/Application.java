package com.example.project.one.jobConnect.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "applications")
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    private String id;

    private String jobId;      // reference job
    private String jobTitle;
    private String employerEmail;

    private String applicantEmail;

    public void setStatus(String accepted) {
    }

}

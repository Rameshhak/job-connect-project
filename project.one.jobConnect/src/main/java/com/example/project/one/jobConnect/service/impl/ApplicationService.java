package com.example.project.one.jobConnect.service.impl;

import com.example.project.one.jobConnect.document.Application;
import java.util.List;

public interface ApplicationService{

    Application applyForJob(Integer jobId, String userEmail);
    List<Application> getApplicationsByEmployer(String employerEmail);
    List<Application> getApplicationsByApplicant(String applicantEmail);

    void cancelApplication(String applicationId);

    void acceptApplication(String applicationId);
}

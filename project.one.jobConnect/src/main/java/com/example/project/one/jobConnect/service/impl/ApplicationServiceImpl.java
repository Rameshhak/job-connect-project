package com.example.project.one.jobConnect.service.impl;

import com.example.project.one.jobConnect.document.Application;
import com.example.project.one.jobConnect.document.JobDetails;
import com.example.project.one.jobConnect.exception.ApplicationNotFoundException;
import com.example.project.one.jobConnect.repository.ApplicationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobDetailsServiceImpl jobDetailsService;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  JobDetailsServiceImpl jobDetailsService) {
        this.applicationRepository = applicationRepository;
        this.jobDetailsService = jobDetailsService;
    }

    @Override
    public Application applyForJob(Integer jobId, String userEmail) {
        JobDetails job = jobDetailsService.getJobDetailsById(jobId);

        Application application = new Application();
        application.setJobId(String.valueOf(jobId));
        application.setJobTitle(job.getTitle());
        application.setEmployerEmail(job.getEmployerEmail()); // better than hardcoding
        application.setApplicantEmail(userEmail);

        return applicationRepository.save(application);
    }

    @Override
    public List<Application> getApplicationsByEmployer(String employerEmail) {
        return applicationRepository.findByEmployerEmail(employerEmail);
    }

    @Override
    public List<Application> getApplicationsByApplicant(String applicantEmail) {
        return applicationRepository.findByApplicantEmail(applicantEmail);
    }

    @Override
    public void acceptApplication(String applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application with ID " + applicationId + " not found."));
        application.setStatus("ACCEPTED");
        applicationRepository.save(application);
    }

    @Override
    public void cancelApplication(String applicationId) {
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);

        if (applicationOptional.isPresent()) {
            applicationRepository.delete(applicationOptional.get());
        } else {
            throw new ApplicationNotFoundException("Application with ID " + applicationId + " not found.");
        }
    }
}

package com.example.project.one.jobConnect.service.impl;

import com.example.project.one.jobConnect.document.JobDetails;
import com.example.project.one.jobConnect.repository.JobDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class JobDetailsServiceImpl {

    private JobDetailsRepository jobDetailsRepository;


    public JobDetails createJobDetails(JobDetails jobDetails, String employerEmail) {
        jobDetails.setEmployerEmail(employerEmail);
        return jobDetailsRepository.save(jobDetails);
    }

    public JobDetails getJobDetailsById(Integer id){
        return jobDetailsRepository.findById(id).orElseThrow(() -> new RuntimeException("Id is not found"));
    }

    public List<JobDetails> searchJob(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return jobDetailsRepository.findAll();
        }
        return jobDetailsRepository.searchByKeyword(keyword.trim());
    }

    public List<JobDetails> displayAllJobDetail(){
       return jobDetailsRepository.findAll();
    }

    public JobDetails updateJobDetails(JobDetails jobDetails){
        JobDetails existingJobDetails = jobDetailsRepository.findById(jobDetails.getId()).orElseThrow(() -> new RuntimeException("Job not found"));

        existingJobDetails.setTitle(jobDetails.getTitle());
        existingJobDetails.setDescription(jobDetails.getDescription());
        existingJobDetails.setLocation(jobDetails.getLocation());
        existingJobDetails.setSalary(jobDetails.getSalary());
        existingJobDetails.setDeadline(jobDetails.getDeadline());

        return jobDetailsRepository.save(existingJobDetails);
    }

    public void deleteJobDetails(Integer id){
        jobDetailsRepository.deleteById(id);
    }

}

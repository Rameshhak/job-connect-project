package com.example.project.one.jobConnect.service.impl;

import com.example.project.one.jobConnect.document.JobDetails;

import java.util.List;

public interface JobService {

    JobDetails createJobDetails(JobDetails jobDetails);

    JobDetails getJobDetailsById(Integer id);

    List<JobDetails> displayAllJobDetails();

    JobDetails updateJobDetails(JobDetails jobDetails);

    void deleteJobDetails(Integer id);
}

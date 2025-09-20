package com.example.project.one.jobConnect.controller;

import com.example.project.one.jobConnect.document.JobDetails;
import com.example.project.one.jobConnect.repository.JobDetailsRepository;
import com.example.project.one.jobConnect.service.impl.JobDetailsServiceImpl;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobDetailsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class JobDetailsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobDetailsServiceImpl jobDetailsService;

    @MockBean
    private JobDetailsRepository jobDetailsRepository;

    // This method should return the create-job view and add a new JobDetails object to the model.
    @Test
    @WithMockUser(roles = "EMPLOYER")
    void showCreatePage_shouldReturnCreateJobViewAndAddJobToModel() throws Exception {
        mockMvc.perform(get("/employer-page/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-job"))
                .andExpect(model().attributeExists("job"));
    }

    // This method handles a POST request to save a new job
    @Test
    @WithMockUser(roles = "EMPLOYER", username = "peter@gmail.com")
    void createJobDetail_shouldSaveJobAndRedirect() throws Exception {
        JobDetails jobDetails = new JobDetails();
        jobDetails.setTitle("Java Developer");
        jobDetails.setDescription("Spring Boot");

        Principal mockPrincipal = () -> "employer@test.com";

        mockMvc.perform(post("/employer-page/create/save")
                        .principal(mockPrincipal)  // 👈 inject principal
                        .flashAttr("job", jobDetails)
                        .with(csrf()));

        verify(jobDetailsService).createJobDetails(any(JobDetails.class), eq("employer@test.com"));
    }

    // This method returns a JSON list of all job details.
    @Test
    @WithMockUser(roles = "EMPLOYER")
    void getJobDetailsAllByEmployer_shouldReturnAllJobsAsJson() throws Exception {
        List<JobDetails> mockJobs = Arrays.asList(new JobDetails(), new JobDetails());
        when(jobDetailsService.displayAllJobDetail()).thenReturn(mockJobs);

        mockMvc.perform(get("/employer-page/displayAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockJobs.size()));

        verify(jobDetailsService, times(1)).displayAllJobDetail();
    }

    // This method should return a view with a list of all jobs and their total count in the model.
    @Test
    @WithMockUser(roles = "EMPLOYER")
    void showAllJobListsByEmployer_shouldReturnJobListsViewWithModelData() throws Exception {
        List<JobDetails> mockJobs = Arrays.asList(new JobDetails(), new JobDetails());
        when(jobDetailsService.displayAllJobDetail()).thenReturn(mockJobs);

        mockMvc.perform(get("/employer-page/job-lists/displayAll"))
                .andExpect(status().isOk())
                .andExpect(view().name("job-lists-by-employer"))
                .andExpect(model().attribute("jobLists", mockJobs))
                .andExpect(model().attribute("totalCount", mockJobs.size()));
    }

    // tests the job search functionality. test with a keyword.
    @Test
    void searchJobs_shouldReturnFilteredJobsWhenKeywordIsProvided() throws Exception {
        List<JobDetails> filteredJobs = Collections.singletonList(new JobDetails());
        when(jobDetailsRepository.findByTitleRegexOrDescriptionRegex(any(), any())).thenReturn(filteredJobs);

        mockMvc.perform(get("/search").param("keyword", "java"))
                .andExpect(status().isOk())
                .andExpect(view().name("job-lists-filter"))
                .andExpect(model().attribute("jobs", filteredJobs));
    }

    // tests the job search functionality. test without a keyword.
    @Test
    void searchJobs_shouldReturnAllJobsWhenKeywordIsEmpty() throws Exception {
        List<JobDetails> allJobs = Arrays.asList(new JobDetails(), new JobDetails());
        when(jobDetailsRepository.findAll()).thenReturn(allJobs);

        mockMvc.perform(get("/search").param("keyword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("job-lists-filter"))
                .andExpect(model().attribute("jobs", allJobs));
    }

    // This method returns a JSON list of all jobs for a job seeker.
    @Test
    @WithMockUser(roles = "JOB-SEEKER")
    void getJobDetailsAllToJobSeeker_shouldReturnAllJobsAsJson() throws Exception {
        List<JobDetails> mockJobs = Arrays.asList(new JobDetails(), new JobDetails());
        when(jobDetailsService.displayAllJobDetail()).thenReturn(mockJobs);

        mockMvc.perform(get("/jobseeker-page/displayAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockJobs.size()));

        verify(jobDetailsService, times(1)).displayAllJobDetail();
    }

    // This method should display a view for jobseekers with a list of all jobs.
    @Test
    @WithMockUser(username = "jobseeker@test.com", roles = "JOB-SEEKER")
    void showAllJobListsToJobSeeker_shouldReturnJobListsViewWithModelData() throws Exception {
        List<JobDetails> mockJobs = Arrays.asList(new JobDetails(), new JobDetails());
        when(jobDetailsService.displayAllJobDetail()).thenReturn(mockJobs);

        mockMvc.perform(get("/jobseeker-page/job-lists/displayAll"))
                .andExpect(status().isOk())
                .andExpect(view().name("job-lists-to-jobseeker"))
                .andExpect(model().attribute("jobLists", mockJobs))
                .andExpect(model().attribute("userEmail", "jobseeker@test.com"));
    }

    // This method displays the form to update a job. test for found scenarios.
    @Test
    @WithMockUser(roles = "EMPLOYER")
    void updateJobPage_shouldReturnUpdateJobViewIfJobFound() throws Exception {
        JobDetails mockJob = new JobDetails();
        when(jobDetailsService.getJobDetailsById(1)).thenReturn(mockJob);

        mockMvc.perform(get("/employer-page/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-job"))
                .andExpect(model().attribute("jobDetails", mockJob));
    }

    // This method displays the form to update a job. test for not-found scenarios.
    @Test
    @WithMockUser(roles = "EMPLOYER")
    void updateJobPage_shouldRedirectIfJobNotFound() throws Exception {
        when(jobDetailsService.getJobDetailsById(99)).thenReturn(null);

        mockMvc.perform(get("/employer-page/update/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("job-lists"));
    }

    // This method handles a POST request to update a job.
    @Test
    @WithMockUser(roles = "EMPLOYER")
    void updateJobDetails_shouldUpdateJobAndRedirect() throws Exception {
        JobDetails updatedJob = new JobDetails();
        updatedJob.setId(1);
        updatedJob.setTitle("Updated Title");
        when(jobDetailsService.updateJobDetails(any(JobDetails.class))).thenReturn(updatedJob);

        mockMvc.perform(post("/employer-page/update/1")
                        .flashAttr("jobDetails", updatedJob)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(jobDetailsService, times(1)).updateJobDetails(any(JobDetails.class));
    }

    // This method displays the confirmation page for deleting a job.
    @Test
    @WithMockUser(roles = "EMPLOYER")
    void showDeletePage_shouldReturnDeleteJobView() throws Exception {
        JobDetails mockJob = new JobDetails();
        when(jobDetailsService.getJobDetailsById(1)).thenReturn(mockJob);

        mockMvc.perform(get("/employer-page/delete/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete-job"))
                .andExpect(model().attribute("jobDetails", mockJob));
    }

    // This method handles a POST request to delete a job.
    @Test
    @WithMockUser(roles = "EMPLOYER")
    void deleteJobDetails_shouldDeleteJobAndReturnNoContent() throws Exception {
        doNothing().when(jobDetailsService).deleteJobDetails(1);

        mockMvc.perform(post("/employer-page/delete/1")
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(containsString("Job details deleted successfully!!!")));

        verify(jobDetailsService, times(1)).deleteJobDetails(1);
    }

    // Tests that the home-after-login view is returned.
    @Test
    @WithMockUser
    void showHomeAfterLoginPage_shouldReturnHomeAfterLoginView() throws Exception {
        mockMvc.perform(get("/home-after-login"))
                .andExpect(status().isOk())
                .andExpect(view().name("home-after-login"));
    }

    // Tests that the job-seeker-page view is returned.
    @Test
    @WithMockUser(roles = "JOB-SEEKER")
    void showJobSeekerPage_shouldReturnJobSeekerPageView() throws Exception {
        mockMvc.perform(get("/job-seeker-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("job-seeker-page"));
    }

    // Tests that the employer-page view is returned.
    @Test
    @WithMockUser(roles = "EMPLOYER")
    void showEmployerPage_shouldReturnEmployerPageView() throws Exception {
        mockMvc.perform(get("/employer-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("employer-page"));
    }
}

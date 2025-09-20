package com.example.project.one.jobConnect.controller;

import com.example.project.one.jobConnect.Application;
import com.example.project.one.jobConnect.document.JobDetails;
import com.example.project.one.jobConnect.document.Keyword;
import com.example.project.one.jobConnect.repository.JobDetailsRepository;
import com.example.project.one.jobConnect.service.impl.JobDetailsServiceImpl;
import com.example.project.one.jobConnect.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

@Controller
@AllArgsConstructor
@Tag(name = "JobController",
        description = "Handles job-related operations, such as create, read, update, delete.")
public class JobDetailsController {

    private JobDetailsServiceImpl jobDetailsService;
    private JobDetailsRepository jobDetailsRepository;


    @Operation(summary = "Access based on roles ",
            description = "Allow user to access to page by their roles.")
    @GetMapping("/home-after-login")
    public String showHomeAfterLoginPage(Model model){
        return "home-after-login";
    }

    @Operation(summary = "Jobseeker page ",
            description = "Access page by jobseeker to view jobs,apply jobs")
    @GetMapping("/job-seeker-page")
    public String showJobSeekerPage(Model model){
        return "job-seeker-page";
    }

    @Operation(summary = "Employer page ",
            description = "Access page by employer to create,update,delete jobs")
    @GetMapping("/employer-page")
    public String showEmployerPage(Model model){
        return "employer-page";
    }

    @Operation(summary = "Showing job creating page",
               description = "Display the job creation page to employer.")
    @GetMapping("/employer-page/create")
    public String showCreatePage(Model model){
        JobDetails job = new JobDetails();
        model.addAttribute("job", job);
        return "create-job";
    }
    @Operation(summary = "Get inputs page from employer",
               description = "Receive data of jobs from employer to storing in database.")
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    @PostMapping("/employer-page/create/save")
    public String createJobDetail( @ModelAttribute("job") JobDetails jobDetails, Principal principal){
        String employerEmail= principal.getName();
        jobDetails.setEmployerEmail(employerEmail);
        jobDetailsService.createJobDetails(jobDetails, employerEmail);
        return "redirect:/employer-page/create?success";
    }

    // Employer page displaying details
    @Operation(summary = "Display all jobs posted",
               description = "display the all jobs done by employer on employer page.")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
    )
    @GetMapping("/employer-page/displayAll")
    public ResponseEntity<List<JobDetails>> getJobDetailsAllByEmployer() {
        List<JobDetails> jobLists = jobDetailsService.displayAllJobDetail();
        return new ResponseEntity<>(jobLists, HttpStatus.OK);
    }

    @Operation(summary = "Display all jobs posted for modifying",
            description = "display the all jobs done by employer to employer for update or delete jobs.")
    @GetMapping("employer-page/job-lists/displayAll")
    public String showAllJobListsByEmployer(Model model){
        List<JobDetails> jobLists = jobDetailsService.displayAllJobDetail();

        model.addAttribute("jobLists", jobLists);
        model.addAttribute("totalCount", jobLists.size());

        return "job-lists-by-employer";
    }

    // Jobseeker page displaying details
    @Operation(summary = "Get Job lists from database",
               description = "Collect all details of jobs from database")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
    )
    @GetMapping("/jobseeker-page/displayAll")
    public ResponseEntity<List<JobDetails>> getJobDetailsAllToJobSeeker() {
        List<JobDetails> jobLists = jobDetailsService.displayAllJobDetail();
        return new ResponseEntity<>(jobLists, HttpStatus.OK);
    }

    @Operation(summary = "Showing the jobs posted",
               description = "Display the all Job lists to jobseeker page for view and apply")
    @GetMapping("jobseeker-page/job-lists/displayAll")
    public String showAllJobListsToJobSeeker(Model model){
        List<JobDetails> jobLists = jobDetailsService.displayAllJobDetail();

        model.addAttribute("jobLists", jobLists);

        model.addAttribute("totalCount", jobLists.size());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // This typically returns the username/email
        model.addAttribute("userEmail", userEmail);
        return "job-lists-to-jobseeker";
    }

    @Operation(summary = "Search jobs by keywords",
               description = "Allow jobseeker search jobs by title or location")
    @GetMapping("/search")
    public String searchJobs(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<JobDetails> jobs;

        if (keyword != null && !keyword.isEmpty()) {
            Pattern regex = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
            jobs = jobDetailsRepository.findByTitleRegexOrDescriptionRegex(regex, regex);
        } else {
            jobs = jobDetailsRepository.findAll();
        }

        model.addAttribute("jobs", jobs);
        model.addAttribute("keyword", keyword);

        return "job-lists-filter"; // show results in same page
    }

     @Operation(summary = "Get job id to update ",
                description = "Receive id of job and check that job exist or not.")
     @GetMapping("/employer-page/update/{id}")
    public String updateJobPage(@PathVariable("id") Integer id, Model model) {
         JobDetails jobDetails = jobDetailsService.getJobDetailsById(id);
         if (jobDetails != null) {
             model.addAttribute("jobDetails", jobDetails);
             return "update-job";
         } else {
             // Handle case where job is not found, e.g., redirect to the jobs list
             return "redirect:job-lists";
         }
     }

    @Operation(summary = "Jobs update ",
            description = "Enable option for employer to update of jobs by id")
    @PostMapping("/employer-page/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateJobDetails(@PathVariable("id") Integer id, @ModelAttribute JobDetails jobDetails){
        jobDetails.setId(id);

        JobDetails updateJobDetails = jobDetailsService.updateJobDetails(jobDetails);
        return "redirect:/employer-page/update?success";
    }

    @Operation(summary = "Get job id to delete ",
            description = "Receive id of job and check that job exist or not.")
    @GetMapping("/employer-page/delete/{id}")
    public String showDeletePage(@PathVariable("id") Integer id, Model model){
        JobDetails jobDetails = jobDetailsService.getJobDetailsById(id);
        model.addAttribute("jobDetails", jobDetails);
        return "delete-job";
    }

    @Operation(summary = "Jobs Deletion ",
            description = "Enable option for employer to delete of jobs by id")
    @PostMapping("/employer-page/delete/{id}")
    public ResponseEntity<String> deleteJobDetails(@PathVariable("id") Integer id){
        jobDetailsService.deleteJobDetails(id);
        return new ResponseEntity<>("Job details deleted successfully!!!", HttpStatus.NO_CONTENT);
    }

}

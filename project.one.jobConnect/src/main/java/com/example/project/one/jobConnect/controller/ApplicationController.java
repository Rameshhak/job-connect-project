package com.example.project.one.jobConnect.controller;

import com.example.project.one.jobConnect.document.Application;
import com.example.project.one.jobConnect.service.impl.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@AllArgsConstructor
@Tag(name = "ApplicationController",
        description = "Manages job application submissions, viewing, and status updates (accept/cancel).")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "Apply for a Job",
               description = "Allows a job seeker to submit an application for a specific job ID.")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
    )
    @PostMapping("/apply/{jobId}")
    @ResponseBody
    public ResponseEntity<Void> applyForJob(@PathVariable("jobId") Integer jobId,
                                            @RequestParam("userEmail") String userEmail) {
        applicationService.applyForJob(jobId, userEmail);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "View Arrived Applications",
               description = "Enables an employer to see a list of all applications submitted for their jobs.")
    @GetMapping("/employer/applications")
    public String viewApplications(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String employerEmail = authentication.getName();

        List<Application> applications = applicationService.getApplicationsByEmployer(employerEmail);
        model.addAttribute("applications", applications);
        return "applications-arrived";
    }

    @Operation(summary = "Accept an Application",
               description = "Lets an employer change the status of a specific application to accepted.")
    @PostMapping("/employer/accept/{id}")
    public String acceptApplication(@PathVariable("id") String applicationId) {
        applicationService.acceptApplication(applicationId);
        return "redirect:/employer-page/applications";
    }

    @Operation(summary = "Cancel an Application",
               description = "Allows an employer to change the status of an application to canceled or rejected.")
    @PostMapping("/employer/cancel/{id}")
    public String cancelApplication(@PathVariable("id") String applicationId) {
         applicationService.cancelApplication(applicationId);
        return "redirect:/employer/applications";
    }
}

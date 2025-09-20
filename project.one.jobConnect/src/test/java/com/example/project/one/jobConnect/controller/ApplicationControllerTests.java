package com.example.project.one.jobConnect.controller;

import com.example.project.one.jobConnect.document.Application;
import com.example.project.one.jobConnect.service.impl.ApplicationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationController.class)
public class ApplicationControllerTests {

      @Autowired
      private MockMvc mockMvc;

      @MockBean
      private ApplicationServiceImpl applicationService;

    @Test
    @WithMockUser(roles = "JOB-SEEKER", username = "peter")
    void applyForJob_shouldCallServiceAndReturnOk() throws Exception {
        Integer jobId = 123;
        String userEmail = "peter@gmail.com";

        mockMvc.perform(MockMvcRequestBuilders.post("/apply/{jobId}", jobId)
                        .param("userEmail", userEmail)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(applicationService, times(1)).applyForJob(eq(jobId), anyString());
    }

    @Test
    @WithMockUser(username = "employer@example.com")
    void viewApplications_shouldAddApplicationsToModel() throws Exception {
        String employerEmail = "employer@example.com";
        List<Application> mockApplications = Arrays.asList(new Application(), new Application());

        when(applicationService.getApplicationsByEmployer(employerEmail)).thenReturn(mockApplications);

        mockMvc.perform(MockMvcRequestBuilders.get("/employer/applications"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("applications-arrived"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("applications"))
                .andExpect(MockMvcResultMatchers.model().attribute("applications", mockApplications));

        verify(applicationService, times(1)).getApplicationsByEmployer(employerEmail);
    }

    @Test
    @WithMockUser(roles = "EMPLOYER" , username = "mark")
    void acceptApplication_shouldCallServiceAndRedirect() throws Exception {
        String applicationId = "app123";

        mockMvc.perform(MockMvcRequestBuilders.post("/employer/accept/{id}", applicationId)
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/employer-page/applications"));

        verify(applicationService, times(1)).acceptApplication(applicationId);
    }

    @Test
    @WithMockUser(roles = "EMPLOYER" , username = "mark")
    void cancelApplication_shouldCallServiceAndRedirect() throws Exception {
        String applicationId = "app456";

        mockMvc.perform(MockMvcRequestBuilders.post("/employer/cancel/{id}", applicationId)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/employer/applications"));

        verify(applicationService, times(1)).cancelApplication(applicationId);
    }

}

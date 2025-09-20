package com.example.project.one.jobConnect.service;

import com.example.project.one.jobConnect.document.Application;
import com.example.project.one.jobConnect.document.JobDetails;
import com.example.project.one.jobConnect.exception.ApplicationNotFoundException;
import com.example.project.one.jobConnect.repository.ApplicationRepository;
import com.example.project.one.jobConnect.service.impl.ApplicationServiceImpl;
import com.example.project.one.jobConnect.service.impl.JobDetailsServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceImplTests {

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private JobDetailsServiceImpl jobDetailsService;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private static JobDetails job;

    @BeforeAll
    static void setUp() {
        job = new JobDetails(101, "Java Developer", "Spring Boot job", "Chennai",
                "8LPA", "September 30th", "mark@gmail.com");
    }

    @Test
    void givenJobIdAndUserEmail_whenApplyForJob_thenReturnApplication() {

        // given
        String userEmail = "john@gmail.com";

        when(jobDetailsService.getJobDetailsById(101)).thenReturn(job);
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // action
        Application application = applicationService.applyForJob(101, userEmail);

        // then
        assertThat(application).isNotNull();
        assertThat(application.getJobId()).isEqualTo("101");
        assertThat(application.getEmployerEmail()).isEqualTo("mark@gmail.com");
        assertThat(application.getApplicantEmail()).isEqualTo("john@gmail.com");

        // verify repository was called once
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    void givenEmployerEmail_whenFindByEmployerEmail_thenReturnApplication() {
        // given
        String employerEmail = "mark@gmail.com";

        Application application = new Application();
        application.setJobId("102");
        application.setJobTitle("Python Developer");
        application.setEmployerEmail(employerEmail);
        application.setApplicantEmail("jacob@gmail.com");

        List<Application> applications = List.of(application);

        when(applicationRepository.findByEmployerEmail(employerEmail))
                .thenReturn(applications);

        // action
        List<Application> result = applicationService.getApplicationsByEmployer(employerEmail);

        // then
        assertThat(result).isNotNull();

        Application app = result.get(0);
        assertThat(app.getEmployerEmail()).isEqualTo("mark@gmail.com");
        assertThat(app.getJobTitle()).isEqualTo("Python Developer");

        // verify repository call
        verify(applicationRepository, times(1)).findByEmployerEmail(employerEmail);
    }

    @Test
    void givenApplicantEmail_whenFindByApplicantEmail_thenReturnApplication() {
        // given
        String applicantEmail = "john@gmail.com";

        Application application = new Application();
        application.setJobId("102");
        application.setJobTitle("Python Developer");
        application.setEmployerEmail("mark@gmail.com");
        application.setApplicantEmail(applicantEmail);

        List<Application> applications = List.of(application);

        when(applicationRepository.findByApplicantEmail(applicantEmail))
                .thenReturn(applications);

        // action
        List<Application> result = applicationService.getApplicationsByApplicant(applicantEmail);

        // then
        assertThat(result).isNotNull();

        Application app = result.get(0);
        assertThat(app.getApplicantEmail()).isEqualTo("john@gmail.com");
        assertThat(app.getJobTitle()).isEqualTo("Python Developer");

        // verify repository call
        verify(applicationRepository, times(1)).findByApplicantEmail(applicantEmail);
    }


    @Test
    void givenApplicationId_whenAcceptApplication_thenStatusUpdatedAndSaved() {
        // given
        String applicationId = "app-123";
        Application application = new Application();
        application.setId(applicationId);
        application.setStatus("ACCEPTED");

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));

        // when
        applicationService.acceptApplication(applicationId);

        // then
        assertThat(application).isNotNull();
        verify(applicationRepository, times(1)).save(application);
    }

    @Test
    void givenInvalidApplicationId_whenAcceptApplication_thenThrowException() {
        // given
        String applicationId = "app-404";
        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> applicationService.acceptApplication(applicationId))
                .isInstanceOf(ApplicationNotFoundException.class)
                .hasMessageContaining("Application with ID " + applicationId + " not found.");
    }

    @Test
    void givenApplicationId_whenCancelApplication_thenDeleteCalled() {
        // given
        String applicationId = "app-456";
        Application application = new Application();
        application.setId(applicationId);

         when(applicationRepository.findById(applicationId))
               .thenReturn(Optional.of(application));

         // when
         applicationService.cancelApplication(applicationId);

         // then
         verify(applicationRepository, times(1)).delete(application);
    }

     @Test
     void givenInvalidApplicationId_whenCancelApplication_thenThrowException() {
         // given
         String applicationId = "app-999";
         when(applicationRepository.findById(applicationId))
                 .thenReturn(Optional.empty());

         // then
         assertThatThrownBy(() -> applicationService.cancelApplication(applicationId))
                 .isInstanceOf(ApplicationNotFoundException.class)
                 .hasMessageContaining("Application with ID " + applicationId + " not found.");
         }
}

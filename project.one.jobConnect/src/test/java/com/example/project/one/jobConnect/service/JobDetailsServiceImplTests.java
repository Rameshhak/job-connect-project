package com.example.project.one.jobConnect.service;

import com.example.project.one.jobConnect.document.JobDetails;
import com.example.project.one.jobConnect.repository.JobDetailsRepository;
import com.example.project.one.jobConnect.service.impl.JobDetailsServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobDetailsServiceImplTests {

    @Mock
    private JobDetailsRepository jobDetailsRepository;

    @InjectMocks
    private JobDetailsServiceImpl jobDetailsService;

    private static JobDetails job;

    @BeforeAll
    public static void setup() {
        job = new JobDetails(1, "Java Developer", "Spring Boot job", "Chennai",
                "8LPA", "September 30th", "mark@gmail.com");
    }

    @Test
    void givenJobObject_whenSave_returnJobDetails() {

        // mock repository behavior
        when(jobDetailsRepository.save(any(JobDetails.class))).thenReturn(job);

        JobDetails savedJob = jobDetailsService.createJobDetails(job, "mark@gmail.com");

        assertThat(savedJob).isNotNull();
        assertThat(savedJob.getTitle()).isEqualTo("Java Developer");
    }

    @Test
    void givenId_whenGetJobDetailsById_thenReturnJob() {
        when(jobDetailsRepository.findById(1)).thenReturn(Optional.of(job));

        JobDetails found = jobDetailsService.getJobDetailsById(1);

        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Java Developer");
    }

    @Test
    void givenInvalidId_whenGetJobDetailsById_thenThrowException() {
        when(jobDetailsRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobDetailsService.getJobDetailsById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Id is not found");
    }

    @Test
    void givenKeyword_whenSearchJob_thenReturnFilteredJobs() {
        when(jobDetailsRepository.searchByKeyword("Java"))
                .thenReturn(List.of(job));

        List<JobDetails> result = jobDetailsService.searchJob("Java");

        assertThat(result).isNotNull();
        assertThat(result.get(0).getTitle()).isEqualTo("Java Developer");
    }

    @Test
    void givenEmptyKeyword_whenSearchJob_thenReturnAllJobs() {
        when(jobDetailsRepository.findAll())
                .thenReturn(List.of(job));

        List<JobDetails> result = jobDetailsService.searchJob(" ");

        assertThat(result).isNotNull();
        assertThat(result.size()).isGreaterThan(0);
    }
    @Test
    void whenDisplayAllJobDetail_thenReturnAllJobs() {
        when(jobDetailsRepository.findAll())
                .thenReturn(Arrays.asList(job));

        List<JobDetails> jobs = jobDetailsService.displayAllJobDetail();

        assertThat(jobs.size()).isNotEqualTo(0);
    }

    @Test
    void givenJob_whenUpdateJobDetails_thenReturnUpdatedJob() {
        JobDetails updatedJob = new JobDetails(1, "Java Developer", "Full stack",
                "Bangalore", "15LPA", "abc@gmail.com", "ABC Corp");

        when(jobDetailsRepository.findById(1)).thenReturn(Optional.of(job));
        when(jobDetailsRepository.save(any(JobDetails.class))).thenReturn(updatedJob);

        JobDetails result = jobDetailsService.updateJobDetails(updatedJob);

        assertThat(result.getTitle()).isEqualTo("Java Developer");
        assertThat(result.getLocation()).isEqualTo("Bangalore");
    }

    @Test
    void givenId_whenDeleteJobDetails_thenVerifyDeleteCalled() throws ParseException {
        doNothing().when(jobDetailsRepository).deleteById(1);

        jobDetailsService.deleteJobDetails(1);

        verify(jobDetailsRepository, times(1)).deleteById(1);

    }

}


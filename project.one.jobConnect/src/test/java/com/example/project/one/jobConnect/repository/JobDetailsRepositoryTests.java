package com.example.project.one.jobConnect.repository;

import com.example.project.one.jobConnect.document.JobDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class JobDetailsRepositoryTests {

    @Autowired
    private JobDetailsRepository jobDetailsRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        // Drop the entire database before each test
        mongoTemplate.getDb().drop();
    }

    @Test
    void testFindByLocationOrTitle() {
        // Given
        mongoTemplate.save(new JobDetails(1,"Software Engineer", "Develop software", "New York", "100000", "September 14th", "mark@gmai.com"));
        mongoTemplate.save(new JobDetails(2,"Data Analyst", "Analyze data", "London", "80000", "September 24th", "john@gmai.com"));
        mongoTemplate.save(new JobDetails(3,"UX Designer", "Design user experience", "New York", "90000", "November 14th", "mark@gmai.com"));

        // When
        List<JobDetails> jobs = jobDetailsRepository.findByLocationOrTitle("New York", "Data Analyst");

        // Then
        assertThat(jobs).hasSize(3);
        assertThat(jobs).extracting(JobDetails::getTitle)
                .containsExactlyInAnyOrder("Software Engineer", "Data Analyst", "UX Designer");
    }

    @Test
    void testFindByTitleRegexOrDescriptionRegex() {
        // Given
        mongoTemplate.save(new JobDetails(4,"Frontend Developer", "Build web interfaces", "Berlin", "75000", "September 14th", "jack@gmai.com"));
        mongoTemplate.save(new JobDetails(5,"Backend Engineer", "Manage server logic", "Berlin", "95000", "September 24th", "mark@gmai.com"));
        mongoTemplate.save(new JobDetails(6,"Full Stack Developer", "Work on both ends", "Berlin", "110000", "September 25th", "mark@gmai.com"));

        Pattern devPattern = Pattern.compile("developer", Pattern.CASE_INSENSITIVE);
        Pattern engineerPattern = Pattern.compile("engineer", Pattern.CASE_INSENSITIVE);

        // When
        List<JobDetails> jobs = jobDetailsRepository.findByTitleRegexOrDescriptionRegex(devPattern, engineerPattern);

        // Then
        assertThat(jobs).hasSize(2);
        assertThat(jobs).extracting(JobDetails::getTitle)
                .containsExactlyInAnyOrder("Frontend Developer", "Full Stack Developer"); // Corrected assertion
    }

    @Test
    void testSearchByKeyword() {
        // Given
        mongoTemplate.save(new JobDetails(7,"Cloud Engineer", "Manage AWS infrastructure", "Seattle", "120000", "September 26th", "mark@gmai.com"));
        mongoTemplate.save(new JobDetails(8,"Data Scientist", "Analyze large datasets", "Boston", "130000", "September 30th", "mark@gmai.com"));
        mongoTemplate.save(new JobDetails(9,"System Administrator", "Monitor servers", "New York", "85000", "September 29th", "john@gmai.com"));

        // When
        List<JobDetails> jobs = jobDetailsRepository.searchByKeyword("cloud");

        // Then
        assertThat(jobs).hasSize(1);
        assertThat(jobs.get(0).getTitle()).isEqualTo("Cloud Engineer");
    }
}

package com.example.project.one.jobConnect.repository;

import com.example.project.one.jobConnect.document.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
public class ApplicationRepositoryTests {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    void findByEmployerEmailTest(){
        Application app = new Application();
        app.setId("1");
        app.setJobId("101");
        app.setJobTitle("Java Developer");
        app.setEmployerEmail("jackop@gmail.com");
        app.setApplicantEmail("dave@gmail.com");
        app.setStatus("ACCEPTED");
        applicationRepository.save(app);

        List<Application> getEmail = applicationRepository.findByEmployerEmail("jackop@gmail.com");

        assertThat(getEmail).isNotNull();
        assertThat(app.getJobId()).isEqualTo("101");
        assertThat(app.getEmployerEmail()).isEqualTo("jackop@gmail.com");
    }

    @Test
    void findByApplicantEmailTest(){
        // given
        Application app = new Application();
        app.setId("2");
        app.setJobId("102");
        app.setJobTitle("Python Developer");
        app.setEmployerEmail("anna@gmail.com");
        app.setApplicantEmail("jack@gmail.com");
        app.setStatus("ACCEPTED");
        applicationRepository.save(app);

        // action
        List<Application> getEmail = applicationRepository.findByApplicantEmail("jack@gmail.com");

        // then
        assertThat(getEmail).isNotNull();
        assertThat(app.getJobId()).isEqualTo("102");
    }

}

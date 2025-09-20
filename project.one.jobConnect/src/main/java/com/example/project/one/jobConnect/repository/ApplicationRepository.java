package com.example.project.one.jobConnect.repository;

import com.example.project.one.jobConnect.document.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByEmployerEmail(String email);
    List<Application> findByApplicantEmail(String email);

    public default void setStatus(String accepted){}

}


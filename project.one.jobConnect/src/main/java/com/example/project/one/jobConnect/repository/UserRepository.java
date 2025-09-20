package com.example.project.one.jobConnect.repository;

import com.example.project.one.jobConnect.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User,String> {

    User findByEmail(String email);
}

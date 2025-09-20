package com.example.project.one.jobConnect.repository;

import com.example.project.one.jobConnect.document.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void getUser_whenFindByEmail_ReturnUser(){
        // Given
        User user = new User();
        user.setId("1");
        user.setName("Jack");
        user.setEmail("jack@gmail.com");
        user.setPassword("jack");
        user.setLocation("London");
        user.setRole("ROLE_EMPLOYER");

        userRepository.save(user);

        // act
        User findSavedUser = userRepository.findByEmail("jack@gmail.com");

        // assert
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo("1");
    }

}

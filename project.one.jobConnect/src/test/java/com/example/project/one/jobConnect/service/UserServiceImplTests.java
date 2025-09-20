package com.example.project.one.jobConnect.service;

import com.example.project.one.jobConnect.document.User;
import com.example.project.one.jobConnect.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.project.one.jobConnect.config.SecurityConfig.passwordEncoder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImplTests userService;

    @Test
    void givenUserObject_whenSave_returnSavedUser(){
        // given
        User newUser = new User();
        newUser.setId("U1");
        newUser.setName("Steven");
        newUser.setEmail("steven@gmail.com");
        newUser.setPassword("steven");
        newUser.setLocation("New york");
        newUser.setRole("ROLE_JOB-SEEKER");

        // action
        User savedUser = userRepository.save(newUser);

        // then
        assertThat(newUser).isNotNull();
        assertThat(newUser.getId()).isEqualTo("U1");
        assertThat(newUser.getRole()).isNotEmpty();
    }

    @Test
    void givenUser_whenFindByEmail_returnUserObject(){
        // given
        User newUser = new User();
        newUser.setId("U2");
        newUser.setName("Mark");
        newUser.setEmail("mark@gmail.com");
        newUser.setPassword("mark");
        newUser.setLocation("New york");
        newUser.setRole("ROLE_EMPLOYER");
        userRepository.save(newUser);

        // action
        User getUser = userRepository.findByEmail("mark@gmail.com");

        // then
        assertThat(newUser).isNotNull();
        assertThat(newUser.getEmail()).isNotEmpty();
    }
}

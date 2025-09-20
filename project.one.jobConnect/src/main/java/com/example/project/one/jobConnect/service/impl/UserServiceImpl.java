package com.example.project.one.jobConnect.service.impl;

import com.example.project.one.jobConnect.document.User;
import com.example.project.one.jobConnect.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void createUser(User user){
        user.setName(user.getName());
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setLocation(user.getLocation());
        userRepository.save(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail( email);
    }

}

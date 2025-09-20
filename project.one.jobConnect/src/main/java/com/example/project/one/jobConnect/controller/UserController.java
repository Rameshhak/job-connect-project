package com.example.project.one.jobConnect.controller;

import com.example.project.one.jobConnect.document.User;
import com.example.project.one.jobConnect.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@Tag(name = "UserController",
        description = "Manages user-related actions like registration, login, and profile updates.")
public class UserController {

    private UserServiceImpl userService;

    @Operation(summary = "Allows clients see home page")
    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @Operation(summary = "Allows clients register their details page")
    @GetMapping("/register")
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @Operation(summary = "Allows client details save to database")
    @PostMapping("/register/save")
    public String registerSavedPage(@ModelAttribute("user") User user, Model model) {
        userService.createUser(user);
        return "redirect:/register?success";
    }

    @Operation(summary = "Allow users to login and verify")
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
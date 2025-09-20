package com.example.project.one.jobConnect.controller;

import com.example.project.one.jobConnect.document.User;
import com.example.project.one.jobConnect.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTests {

     @Autowired
     private MockMvc mockMvc;
     @MockBean
     private UserServiceImpl userService;

     @Test
     @WithMockUser(username = "mark" , roles = "EMPLOYER")
     void homePage_shouldReturnHomePageView() throws Exception {
         mockMvc.perform(get("/home"))
                 .andExpect(status().isOk())
                 .andExpect(view().name("home"));
     }

     @Test
     @WithMockUser(username = "mark" , roles = "EMPLOYER")
     void createUser_shouldReturnRegisterPageViewAndUserToModel() throws Exception {
         mockMvc.perform(get("/register"))
                 .andExpect(status().isOk())
                 .andExpect(view().name("register"))
                 .andExpect(model().attributeExists("user"))
                 .andExpect(model().attribute("user", new User()));
     }

     @Test
     @WithMockUser(username = "mark", roles = "EMPLOYER")
     void registerPage_shouldReturnRegisteredPageViewAndRedirectPage() throws Exception {
          User user = new User();
          user.setId("U1");
          user.setName("mark");
          user.setEmail("mark@gmail.com");
          user.setPassword("mark");
          user.setLocation("Chennai");
          user.setRole("ROLE_EMPLOYER");

          mockMvc.perform(
                   post("/register/save")
                           .flashAttr("user", user)
                           .with(csrf())
                  )
                  .andExpect(status().is3xxRedirection())
                  .andExpect(redirectedUrl("/register?success"));

          // Verify that the createUser method was called once with the user object
          verify(userService, times(1)).createUser(user);
     }

     @Test
     @WithMockUser(username = "jackson", roles = "JOB-SEEKER")
     void loginPage_shouldReturnLoginPageView() throws Exception {
          mockMvc.perform(get("/login"))
                  .andExpect(status().isOk())
                  .andExpect(content().contentType("text/html;charset=UTF-8"))
                  .andExpect(content().string(containsString("Please sign in")));
     }
}

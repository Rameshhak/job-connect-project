package com.example.project.one.jobConnect.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService UserDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Publicly accessible pages first
                        .requestMatchers("/home", "/home-after-login", "/register/**", "/css/**", "/js/apply").permitAll()
                        .requestMatchers("/search").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // More specific, role-based pages next
                        .requestMatchers("/employer-page/delete/**").hasRole("EMPLOYER")
                        .requestMatchers("/employer/accept/**", "/employer/cancel/**").hasRole("EMPLOYER")

                        // General role-based pages
                        .requestMatchers("/jobseeker-page/**", "/job-seeker-page/**").hasRole("JOB-SEEKER")
                        .requestMatchers("/employer-page/**", "/employer/**", "/application-arrived").hasRole("EMPLOYER")

                        // Pages accessible to multiple roles
                        .requestMatchers("/employer-page/applications", "/apply/**", "/job-lists").hasAnyRole("EMPLOYER", "JOB-SEEKER")

                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/home-after-login", true)
                                .failureUrl("/login?error=true")
                                .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return httpSecurity.build();
    }

}


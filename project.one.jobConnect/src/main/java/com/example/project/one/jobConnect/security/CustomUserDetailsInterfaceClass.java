package com.example.project.one.jobConnect.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface CustomUserDetailsInterfaceClass {

    Collection<? extends GrantedAuthority> getAuthority();
}

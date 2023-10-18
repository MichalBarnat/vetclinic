package com.powtorka.vetclinic.config;

import com.powtorka.vetclinic.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManagerBuilder auth;

    public SecurityConfig(UserService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        try {
            auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure AuthenticationManagerBuilder", e);
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(req -> req.getServletPath().startsWith("/user") && (req.getMethod().equals("GET") || req.getMethod().equals("POST"))).permitAll()
                        .requestMatchers(req -> req.getServletPath().equals("/doctor/deleteAll") && req.getMethod().equals("DELETE")).hasRole("ADMIN")
                        .requestMatchers(req -> req.getServletPath().equals("/doctor") && req.getMethod().equals("GET")).hasAnyRole("USER", "ADMIN")
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

}

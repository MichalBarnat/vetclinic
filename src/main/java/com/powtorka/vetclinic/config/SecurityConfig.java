package com.powtorka.vetclinic.config;

import com.powtorka.vetclinic.exceptions.security.MyAccessDeniedHandler;
import com.powtorka.vetclinic.exceptions.security.MyAuthenticationEntryPoint;
import com.powtorka.vetclinic.security.UserPermissions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(req -> req.getServletPath().startsWith("/user")).permitAll()
                        .requestMatchers(req -> req.getMethod().equals("GET")).hasAuthority(UserPermissions.READ.name())
                        .requestMatchers(req -> req.getMethod().equals("POST")
                                        || req.getMethod().equals("PUT")
                                        || req.getMethod().equals("PATCH")).hasAuthority(UserPermissions.WRITE.name())
                        .requestMatchers(req -> req.getMethod().equals("DELETE")).hasAuthority(UserPermissions.DELETE.name())

                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(authenticationEntryPoint()));

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new MyAuthenticationEntryPoint();
    }

}







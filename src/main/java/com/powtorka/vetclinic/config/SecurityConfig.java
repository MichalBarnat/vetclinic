package com.powtorka.vetclinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(req -> req.getServletPath().startsWith("/user")).permitAll()
                        .requestMatchers(req ->
                                req.getMethod().equals("POST")
                                        || req.getMethod().equals("PUT")
                                        || req.getMethod().equals("PATCH")
                                        || req.getMethod().equals("DELETE")).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



//DŁUZSZA WERSJA TEGO SAMEGO:
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        //USER
//                        .requestMatchers(req -> req.getServletPath().startsWith("/user") && (req.getMethod().equals("GET") || req.getMethod().equals("POST"))).permitAll()
//                        //DOCTOR:
//                        .requestMatchers(req -> req.getServletPath().equals("/doctor")
//                                && req.getMethod().equals("POST")  || req.getMethod().equals("DELETE")
//                                || req.getMethod().equals("PUT") || req.getMethod().equals("PATCH")).hasAnyRole("ADMIN")
//                        .requestMatchers(req -> req.getServletPath().equals("/doctor/deleteAll") && req.getMethod().equals("DELETE")).hasRole("ADMIN")
//                        //PATIENT:
//                        .requestMatchers(req -> req.getServletPath().equals("/patient")
//                                && req.getMethod().equals("POST")  || req.getMethod().equals("DELETE")
//                                || req.getMethod().equals("PUT") || req.getMethod().equals("PATCH")).hasAnyRole("ADMIN")
//                        .requestMatchers(req -> req.getServletPath().equals("/patient/deleteAll") && req.getMethod().equals("DELETE")).hasRole("ADMIN")
//                        //APPOINTMENT:
//                        .requestMatchers(req -> req.getServletPath().equals("/appointment")
//                                && req.getMethod().equals("POST")  || req.getMethod().equals("DELETE")
//                                || req.getMethod().equals("PUT") || req.getMethod().equals("PATCH")).hasAnyRole("ADMIN")
//                        .requestMatchers(req -> req.getServletPath().equals("/appointment/deleteAll") && req.getMethod().equals("DELETE")).hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}




//PRÓBA UZYCIA @PREAUTHORIZE W KONTROLERACH ::
//import org.springframework.context.annotation.Bean;
//        import org.springframework.context.annotation.Configuration;
//        import org.springframework.security.config.Customizer;
//        import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//        import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//        import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//        import org.springframework.security.crypto.password.PasswordEncoder;
//        import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // prePostEnabled default ON
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}






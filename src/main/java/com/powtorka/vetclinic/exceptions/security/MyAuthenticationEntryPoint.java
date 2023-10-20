package com.powtorka.vetclinic.exceptions.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.exceptions.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;



    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorMessage message = ErrorMessage.builder()
                .dateTime(LocalDateTime.now())
                .code(UNAUTHORIZED.value())
                .status(UNAUTHORIZED.name())
                .message("Unauthorized : " + request.getMethod())
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .build();
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }
}

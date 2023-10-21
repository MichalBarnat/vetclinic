package com.powtorka.vetclinic.exceptions.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.exceptions.ErrorMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorMessage message = ErrorMessage.builder()
                .dateTime(LocalDateTime.now())
                .code(FORBIDDEN.value())
                .status(FORBIDDEN.getReasonPhrase())
                .message(accessDeniedException.getMessage())
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .build();
        response.setStatus(FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(message));
    }
}

package com.powtorka.vetclinic.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ErrorMessage> doctorNotFoundException(DoctorNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .dateTime(LocalDateTime.now())
                .code(NOT_FOUND.value())
                .status(NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .build(), NOT_FOUND);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorMessage> patientNotFoundException(PatientNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .dateTime(LocalDateTime.now())
                .code(NOT_FOUND.value())
                .status(NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .build(), NOT_FOUND);
    }



    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");

        for (ConstraintViolation<?> violation : violations) {
            errorMessage.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append("; ");
        }

        return new ResponseEntity<>(ErrorMessage.builder()
                .dateTime(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(errorMessage.toString())
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorMessage> appointmentNotFoundException(AppointmentNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .dateTime(LocalDateTime.now())
                .code(NOT_FOUND.value())
                .status(NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .build(), NOT_FOUND);
    }

    @ExceptionHandler(AppointmentIsNotAvailableExpcetion.class)
    public ResponseEntity<ErrorMessage> appointmentNotAvailableException(AppointmentIsNotAvailableExpcetion ex, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .dateTime(LocalDateTime.now())
                .code(UNPROCESSABLE_ENTITY.value())
                .status(UNPROCESSABLE_ENTITY.getReasonPhrase())
                .message(ex.getMessage())
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .build(), UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {

        String message = "Data integrity violation: " + ex.getMostSpecificCause().getMessage();

        return new ResponseEntity<>(ErrorMessage.builder()
                .dateTime(LocalDateTime.now())
                .code(CONFLICT.value())
                .status(CONFLICT.getReasonPhrase())
                .message(message)
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .build(), CONFLICT);
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorMessage> accessDeniedExceptionHandler(AccessDeniedException ex, HttpServletRequest httpServletRequest) {
//        return new ResponseEntity<>(ErrorMessage.builder()
//                .dateTime(LocalDateTime.now())
//                .code(FORBIDDEN.value())
//                .status(FORBIDDEN.getReasonPhrase())
//                .message(ex.getMessage())
//                .uri(httpServletRequest.getRequestURI())
//                .method(httpServletRequest.getMethod())
//                .build(), FORBIDDEN);
//    }
//
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<ErrorMessage> authenticationExceptionHandler(AuthenticationException ex, HttpServletRequest httpServletRequest) {
//        return new ResponseEntity<>(ErrorMessage.builder()
//                .dateTime(LocalDateTime.now())
//                .code(UNAUTHORIZED.value())
//                .status(UNAUTHORIZED.getReasonPhrase())
//                .message(ex.getMessage())
//                .uri(httpServletRequest.getRequestURI())
//                .method(httpServletRequest.getMethod())
//                .build(), UNAUTHORIZED);
//    }




}

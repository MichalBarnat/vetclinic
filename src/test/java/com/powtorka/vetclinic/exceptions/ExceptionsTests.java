package com.powtorka.vetclinic.exceptions;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionsTests {

    @Test
    public void doctorNotFoundException_constructor_withNoArgs_shouldCreateException() {
        Exception exception = new DoctorNotFoundException();

        assertNull(exception.getMessage());
    }

    @Test
    public void doctorNotFoundException_constructor_withMessage_shouldCreateExceptionWithMessage() {
        String expectedMessage = "Test message";

        Exception exception = new DoctorNotFoundException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void patientNotFoundException_constructor_withNoArgs_shouldCreateException() {
        Exception exception = new PatientNotFoundException();

        assertNull(exception.getMessage());
    }

    @Test
    public void patientNotFoundException_constructor_withMessage_shouldCreateExceptionWithMessage() {
        String expectedMessage = "Test message";

        Exception exception = new PatientNotFoundException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void appointmentNotFoundException_constructor_withNoArgs_shouldCreateException() {
        Exception exception = new AppointmentNotFoundException();

        assertNull(exception.getMessage());
    }

    @Test
    public void appointmentNotFoundException_constructor_withMessage_shouldCreateExceptionWithMessage() {
        String expectedMessage = "Test message";

        Exception exception = new AppointmentNotFoundException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void appointmentIsNotAvailableException_constructor_withNoArgs_shouldCreateException() {
        Exception exception = new AppointmentIsNotAvailableExpcetion();

        assertNull(exception.getMessage());
    }

    @Test
    public void appointmentIsNotAvailableException_constructor_withMessage_shouldCreateExceptionWithMessage() {
        String expectedMessage = "Test message";

        Exception exception = new AppointmentIsNotAvailableExpcetion(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void invalidPatientAgeException_constructor_withNoArgs_shouldCreateException() {
        Exception exception = new InvalidPatientAgeException();

        assertNull(exception.getMessage());
    }

    @Test
    public void invalidPatientAgeException_constructor_withMessage_shouldCreateExceptionWithMessage() {
        String expectedMessage = "Test message";

        Exception exception = new InvalidPatientAgeException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    public void testErrorMessage() {
        LocalDateTime dateTime = LocalDateTime.now();
        int code = 404;
        String status = "Not Found";
        String message = "Doctor not found";
        String uri = "/api/doctor/1";
        String method = "GET";

        ErrorMessage errorMessage = ErrorMessage.builder()
                .dateTime(dateTime)
                .code(code)
                .status(status)
                .message(message)
                .uri(uri)
                .method(method)
                .build();

        assertEquals(dateTime, errorMessage.getDateTime());
        assertEquals(code, errorMessage.getCode());
        assertEquals(status, errorMessage.getStatus());
        assertEquals(message, errorMessage.getMessage());
        assertEquals(uri, errorMessage.getUri());
        assertEquals(method, errorMessage.getMethod());
    }




}

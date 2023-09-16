package com.powtorka.vetclinic.exceptions;

public class AppointmentIsNotAvailableException extends RuntimeException{
    public AppointmentIsNotAvailableException() {
    }

    public AppointmentIsNotAvailableException(String message) {
        super(message);
    }
}

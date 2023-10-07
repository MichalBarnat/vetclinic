package com.powtorka.vetclinic.exceptions;

public class AppointmentIsNotAvailableExpcetion extends RuntimeException{
    public AppointmentIsNotAvailableExpcetion() {
    }

    public AppointmentIsNotAvailableExpcetion(String message) {
        super(message);
    }
}

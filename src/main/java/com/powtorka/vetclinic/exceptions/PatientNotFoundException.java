package com.powtorka.vetclinic.exceptions;

public class PatientNotFoundException extends RuntimeException{
    public PatientNotFoundException() {
    }

    public PatientNotFoundException(String message) {
        super(message);
    }
}

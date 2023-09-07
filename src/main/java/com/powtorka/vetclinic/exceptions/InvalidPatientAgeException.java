package com.powtorka.vetclinic.exceptions;

public class InvalidPatientAgeException extends RuntimeException{
    public InvalidPatientAgeException() {
    }

    public InvalidPatientAgeException(String message) {
        super(message);
    }
}

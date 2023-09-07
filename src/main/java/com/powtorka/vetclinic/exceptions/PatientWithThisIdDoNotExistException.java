package com.powtorka.vetclinic.exceptions;

public class PatientWithThisIdDoNotExistException extends RuntimeException{
    public PatientWithThisIdDoNotExistException() {
    }

    public PatientWithThisIdDoNotExistException(String message) {
        super(message);
    }
}

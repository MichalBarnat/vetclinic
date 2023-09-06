package com.powtorka.vetclinic.exceptions;

public class DoctorWithThisIdDoNotExistException extends RuntimeException{
    public DoctorWithThisIdDoNotExistException() {
    }

    public DoctorWithThisIdDoNotExistException(String message) {
        super(message);
    }
}

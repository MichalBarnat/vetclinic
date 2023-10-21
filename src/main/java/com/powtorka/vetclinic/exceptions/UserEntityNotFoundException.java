package com.powtorka.vetclinic.exceptions;

public class UserEntityNotFoundException extends RuntimeException {
    public UserEntityNotFoundException() {
    }

    public UserEntityNotFoundException(String message) {
        super(message);
    }
}

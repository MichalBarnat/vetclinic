package com.powtorka.vetclinic.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException() {
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}

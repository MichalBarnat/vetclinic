package com.powtorka.vetclinic.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserPermissions {
    READ,
    WRITE,
    DELETE
}

package com.powtorka.vetclinic.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.powtorka.vetclinic.security.UserPermissions.READ;
import static com.powtorka.vetclinic.security.UserPermissions.WRITE;

@Getter
@AllArgsConstructor
public enum UserRole {

    USER(new HashSet<>(Arrays.asList(READ))),
    ADMIN(new HashSet<>(Arrays.asList(READ, WRITE)));

    private final Set<UserPermissions> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

}

package com.powtorka.vetclinic.repository;

import com.powtorka.vetclinic.model.security.role.ERole;
import com.powtorka.vetclinic.model.security.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}

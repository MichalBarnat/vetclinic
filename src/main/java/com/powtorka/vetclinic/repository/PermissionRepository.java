package com.powtorka.vetclinic.repository;

import com.powtorka.vetclinic.model.security.permission.EPermission;
import com.powtorka.vetclinic.model.security.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(EPermission name);
}

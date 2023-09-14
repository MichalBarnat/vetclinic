package com.powtorka.vetclinic.repository;

import com.powtorka.vetclinic.model.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findAll(Pageable pageable);

    List<Patient> findByAgeGreaterThan(int age);
}
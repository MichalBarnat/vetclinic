package com.powtorka.vetclinic.repository;

import com.powtorka.vetclinic.model.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}

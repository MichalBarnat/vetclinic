package com.powtorka.vetclinic.repository;

import com.powtorka.vetclinic.model.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}

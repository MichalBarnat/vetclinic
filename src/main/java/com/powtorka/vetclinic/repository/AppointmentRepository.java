package com.powtorka.vetclinic.repository;

import com.powtorka.vetclinic.model.appointment.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findAll(Pageable pageable);

    List<Appointment> findAllByPatientId(Long patientId);

    List<Appointment> findAllByDoctorId(Long doctorId);

}
